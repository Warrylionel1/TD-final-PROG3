package com.example.agricole.validator;

import com.example.agricole.dto.CreateCollectivity;
import com.example.agricole.entity.Member;
import com.example.agricole.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CollectivityStructureValidator {

    public void validateStructure(CreateCollectivity req, List<Member> members) {

        if (req.getStructure() == null) {
            throw new BadRequestException("Structure is required");
        }

        List<String> required = List.of(
                req.getStructure().getPresident(),
                req.getStructure().getVicePresident(),
                req.getStructure().getTreasurer(),
                req.getStructure().getSecretary()
        );

        if (required.stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException("Structure incomplete");
        }

        for (String id : required) {

            boolean exists = members.stream()
                    .anyMatch(m -> m.getId().equals(id));

            if (!exists) {
                throw new BadRequestException(
                        "Structure members must belong to collectivity"
                );
            }
        }
    }
}