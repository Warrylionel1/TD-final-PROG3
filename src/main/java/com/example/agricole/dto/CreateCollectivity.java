package com.example.agricole.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCollectivity {
    private String location;
    private List<String> members;
    private boolean federationApproval;
    private CreateCollectivityStructure structure;
}
