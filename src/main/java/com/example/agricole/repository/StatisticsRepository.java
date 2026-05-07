package com.example.agricole.repository;

import com.example.agricole.dto.MemberDescription;
import com.example.agricole.dto.MembershipFeeRaw;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class StatisticsRepository {

    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberDescription> getMembers(String collectivityId) {

        String sql = """
            SELECT m.id, m.first_name, m.last_name, m.email, m.occupation
            FROM member m
            JOIN collectivity_member cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
        """;

        List<MemberDescription> members = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                members.add(new MemberDescription(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("occupation")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching members", e);
        }

        return members;
    }

    public Map<String, Double> getPaymentsByMember(
            String collectivityId,
            LocalDate from,
            LocalDate to) {

        String sql = """
            SELECT mp.member_id, COALESCE(SUM(mp.amount), 0) AS total
            FROM member_payment mp
            JOIN membership_fee mf ON mp.membership_fee_id = mf.id
            WHERE mf.collectivity_id = ?
              AND mp.creation_date BETWEEN ? AND ?
            GROUP BY mp.member_id
        """;

        Map<String, Double> payments = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.put(
                        rs.getString("member_id"),
                        rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching payments", e);
        }

        return payments;
    }

    public List<MembershipFeeRaw> getActiveMembershipFees(String collectivityId) {

        String sql = """
            SELECT id, amount, frequency, eligible_from
            FROM membership_fee
            WHERE collectivity_id = ?
              AND status = 'ACTIVE'
        """;

        List<MembershipFeeRaw> fees = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                fees.add(new MembershipFeeRaw(
                        rs.getString("id"),
                        rs.getDouble("amount"),
                        rs.getString("frequency"),
                        rs.getDate("eligible_from").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching membership fees", e);
        }

        return fees;
    }

    public long countNewMembers(
            String collectivityId,
            LocalDate from,
            LocalDate to) {

        String sql = """
            SELECT COUNT(m.id)
            FROM member m
            JOIN collectivity_member cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
              AND m.join_date BETWEEN ? AND ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error counting new members", e);
        }
    }
}