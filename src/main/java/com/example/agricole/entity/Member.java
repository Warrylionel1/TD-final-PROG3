package com.example.agricole.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String id;
    private MemberInformation information;
    private List<Member> referees;
}
