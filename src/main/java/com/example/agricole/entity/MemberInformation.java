package com.example.agricole.entity;

import com.example.agricole.enums.Gender;
import com.example.agricole.enums.MemberOccupation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInformation {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private int phoneNumber;
    private String email;
    private MemberOccupation occupation;
}
