package com.example.agricole.repository;

import com.example.agricole.entity.CollectivityTransaction;
import com.example.agricole.entity.FinancialAccount;
import com.example.agricole.entity.Member;
import com.example.agricole.enums.PaymentMode;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {
    private final DataSource dataSource;
    private final FinancialAccountRepository financialAccountRepository;
    private final MemberRepository memberRepository;

    public TransactionRepository(DataSource dataSource,
                                 FinancialAccountRepository financialAccountRepository,
                                 MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.financialAccountRepository = financialAccountRepository;
        this.memberRepository = memberRepository;
    }

    public CollectivityTransaction save(CollectivityTransaction tx) {
        if (tx.getId() == null) {
            tx.setId(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO collectivity_transaction (id, collectivity_id, creation_date, amount, payment_mode, account_credited_id, member_debited_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tx.getId());
            ps.setString(2, tx.getCollectivityId());
            ps.setDate(3, Date.valueOf(tx.getCreationDate()));
            ps.setDouble(4, tx.getAmount());
            ps.setString(5, tx.getPaymentMode().name());
            ps.setString(6, tx.getAccountCredited().getId());
            ps.setString(7, tx.getMemberDebited().getId());
            ps.executeUpdate();
            return tx;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    public List<CollectivityTransaction> findByCollectivityIdAndDateRange(String collectivityId, LocalDate from, LocalDate to) {
        String sql = "SELECT * FROM collectivity_transaction WHERE collectivity_id = ? AND creation_date BETWEEN ? AND ? ORDER BY creation_date";
        List<CollectivityTransaction> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CollectivityTransaction tx = new CollectivityTransaction();
                tx.setId(rs.getString("id"));
                tx.setCollectivityId(rs.getString("collectivity_id"));
                tx.setCreationDate(rs.getDate("creation_date").toLocalDate());
                tx.setAmount(rs.getDouble("amount"));
                tx.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));

                String accountId = rs.getString("account_credited_id");
                FinancialAccount acc = financialAccountRepository.findById(accountId)
                        .orElseThrow(() -> new RuntimeException("Account not found for tx"));
                tx.setAccountCredited(acc);

                String memberId = rs.getString("member_debited_id");
                Member member = memberRepository.findById(memberId);
                if (member == null) throw new RuntimeException("Member not found for tx");
                tx.setMemberDebited(member);

                list.add(tx);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transactions", e);
        }
        return list;
    }
}