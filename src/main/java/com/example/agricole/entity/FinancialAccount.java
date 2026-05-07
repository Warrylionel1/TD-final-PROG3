package com.example.agricole.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CashAccount.class, name = "CASH"),
        @JsonSubTypes.Type(value = MobileBankingAccount.class, name = "MOBILE_BANKING"),
        @JsonSubTypes.Type(value = BankAccount.class, name = "BANK")
})
public abstract class FinancialAccount {
    private String id;
    private String collectivityId;
    private double amount;
}
