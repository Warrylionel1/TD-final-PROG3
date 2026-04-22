package com.example.agricole.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collectivity {
    private String id;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;
    private boolean federationApproval;
    private String number;
    private String name;

    public boolean hasAssignedIdentity() {
        return number != null && name != null;
    }
}