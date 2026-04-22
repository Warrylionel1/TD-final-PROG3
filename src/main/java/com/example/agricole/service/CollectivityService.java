package com.example.agricole.service;

import com.example.agricole.dto.AssignIdentityRequest;
import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.CollectivityNotFoundException;
import com.example.agricole.exception.ConflictException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.MemberRepository;
import com.example.agricole.validator.CollectivityStructureValidator;
import com.example.agricole.validator.MemberExperienceValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MemberExperienceValidator experienceValidator;
    private final CollectivityStructureValidator structureValidator;

    public CollectivityService(
            CollectivityRepository collectivityRepository,
            MemberRepository memberRepository,
            MemberExperienceValidator experienceValidator,
            CollectivityStructureValidator structureValidator
    ) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.experienceValidator = experienceValidator;
        this.structureValidator = structureValidator;
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivity> requests) {

        List<Collectivity> result = new ArrayList<>();

        for (CreateCollectivity req : requests) {

            if (!req.isFederationApproval()) {
                throw new BadRequestException("Federation approval required");
            }

            if (req.getStructure() == null) {
                throw new BadRequestException("Structure is required");
            }

            List<Member> members = memberRepository.findByIds(req.getMembers());

            if (members.size() != req.getMembers().size()) {
                throw new MemberNotFoundException("Some members not found");
            }

            if (members.size() < 10) {
                throw new BadRequestException("Minimum 10 members required");
            }

            long experiencedMembers = members.stream()
                    .filter(experienceValidator::has6MonthsExperience)
                    .count();

            if (experiencedMembers < 5) {
                throw new BadRequestException("At least 5 experienced members required");
            }

            structureValidator.validateStructure(req, members);

            Collectivity collectivity = new Collectivity();
            collectivity.setId(UUID.randomUUID().toString());
            collectivity.setLocation(req.getLocation());
            collectivity.setMembers(members);
            collectivity.setFederationApproval(true);

            collectivityRepository.saveWithRelations(collectivity, req.getStructure());

            Collectivity saved = collectivityRepository.findById(collectivity.getId());

            result.add(saved);
        }

        return result;
    }

    public Collectivity assignIdentity(String collectivityId, AssignIdentityRequest request) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId);
        if (collectivity == null) {
            throw new CollectivityNotFoundException(collectivityId);
        }
        if (collectivity.hasAssignedIdentity()) {
            throw new ConflictException("Cette collectivité possède déjà un numéro et un nom");
        }
        if (collectivityRepository.existsByNumber(request.getNumber())) {
            throw new ConflictException("Le numéro " + request.getNumber() + " est déjà utilisé");
        }
        if (collectivityRepository.existsByName(request.getName())) {
            throw new ConflictException("Le nom " + request.getName() + " est déjà utilisé");
        }
        collectivityRepository.updateNumberAndName(collectivityId, request.getNumber(), request.getName());
        return collectivityRepository.findById(collectivityId);
    }
}