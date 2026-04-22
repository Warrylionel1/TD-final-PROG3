package com.example.agricole.service;

import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import com.example.agricole.exception.MemberNotFoundException;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.MemberRepository;
import com.example.agricole.validator.CollectivityStructureValidator;
import com.example.agricole.validator.MemberExperienceValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                throw new BadRequestException(
                        "At least 5 members with 6 months experience required"
                );
            }

            structureValidator.validateStructure(req, members);

            Collectivity collectivity = new Collectivity();
            collectivity.setLocation(req.getLocation());
            collectivity.setMembers(members);
            collectivity.setFederationApproval(true);

            collectivityRepository.save(collectivity);

            result.add(collectivity);
        }

        return result;
    }
}