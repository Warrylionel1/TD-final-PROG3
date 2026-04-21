package com.example.agricole.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collectivity {

    private String id;
    private String location;
    private boolean federationApproval;
    private CollectivityStructure structure;
    private List<Member> members;
}