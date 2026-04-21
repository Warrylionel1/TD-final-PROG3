package com.example.agricole.dto;

import com.example.agricole.entity.MemberInformation;
import lombok.Data;

import java.util.List;

@Data
public class CreateMember extends MemberInformation {
    private String collectivityIdentifier;
    private List<String> referees;
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;
}
