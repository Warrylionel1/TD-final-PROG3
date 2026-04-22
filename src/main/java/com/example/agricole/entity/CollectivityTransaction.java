package com.example.agricole.entity;

import com.example.agricole.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityTransaction {
    private String id;
    private String collectivityId;
    private LocalDate creationDate;
    private double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;
}