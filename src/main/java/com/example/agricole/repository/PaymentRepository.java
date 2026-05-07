package com.example.agricole.repository;

import com.example.agricole.entity.FinancialAccount;
import com.example.agricole.entity.MemberPayment;
import com.example.agricole.enums.PaymentMode;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public class PaymentRepository {
    private final DataSource dataSource;
    private final FinancialAccountRepository financialAccountRepository;

    public PaymentRepository(DataSource dataSource, FinancialAccountRepository financialAccountRepository) {
        this.dataSource = dataSource;
        this.financialAccountRepository = financialAccountRepository;
    }

    public MemberPayment save(MemberPayment payment) {
        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, account_credited_id, creation_date) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, payment.getId());
            ps.setString(2, payment.getMemberId());
            ps.setString(3, payment.getMembershipFeeId());
            ps.setDouble(4, payment.getAmount());
            ps.setString(5, payment.getPaymentMode().name());
            ps.setString(6, payment.getAccountCredited().getId());
            ps.setDate(7, Date.valueOf(payment.getCreationDate()));
            ps.executeUpdate();

            FinancialAccount acc = financialAccountRepository.findById(payment.getAccountCredited().getId())
                    .orElseThrow(() -> new RuntimeException("Account not found after save"));
            payment.setAccountCredited(acc);
            return payment;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
    }
}