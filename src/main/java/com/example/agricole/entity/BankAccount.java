package com.example.agricole.entity;

import com.example.agricole.enums.Bank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankAccount extends FinancialAccount {
    private String holderName;
    private Bank bankName;
    private int bankCode;
    private int bankBranchCode;
    private long bankAccountNumber;
    private int bankAccountKey;
}