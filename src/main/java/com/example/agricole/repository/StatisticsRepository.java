package com.example.agricole.repository;

import com.example.agricole.dto.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {

    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(
            String collectivityId, LocalDate from, LocalDate to) {

        String sql = """
            SELECT
                m.id,
                m.first_name,
                m.last_name,
                m.email,
                m.occupation,

                COALESCE((
                    SELECT SUM(mp.amount)
                    FROM member_payment mp
                    JOIN membership_fee mf ON mp.membership_fee_id = mf.id
                    WHERE mp.member_id = m.id
                      AND mf.collectivity_id = ?
                      AND mp.creation_date BETWEEN ? AND ?
                ), 0) AS earned_amount,

                GREATEST(
                    (
                        SELECT COALESCE(SUM(mf2.amount), 0)
                        FROM membership_fee mf2
                        WHERE mf2.collectivity_id = ?
                          AND mf2.status = 'ACTIVE'
                          AND mf2.eligible_from <= ?
                    ) -
                    COALESCE((
                        SELECT SUM(mp2.amount)
                        FROM member_payment mp2
                        JOIN membership_fee mf3 ON mp2.membership_fee_id = mf3.id
                        WHERE mp2.member_id = m.id
                          AND mf3.collectivity_id = ?
                          AND mf3.status = 'ACTIVE'
                          AND mf3.eligible_from <= ?
                    ), 0),
                0) AS unpaid_amount

            FROM member m
            WHERE m.id IN (
                SELECT cm.member_id FROM collectivity_member cm
                WHERE cm.collectivity_id = ?
            )
            ORDER BY m.last_name, m.first_name
        """;

        List<CollectivityLocalStatistics> stats = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ps.setString(4, collectivityId);
            ps.setDate(5, Date.valueOf(to));
            ps.setString(6, collectivityId);
            ps.setDate(7, Date.valueOf(to));
            ps.setString(8, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MemberDescription desc = new MemberDescription(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("occupation")
                );

                double earned = rs.getDouble("earned_amount");
                double unpaid = rs.getDouble("unpaid_amount");

                stats.add(new CollectivityLocalStatistics(desc, earned, unpaid));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Local statistics error", e);
        }

        return stats;
    }

    public long countNewMembers(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT COUNT(DISTINCT cm.member_id)
            FROM collectivity_member cm
            JOIN member m ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
              AND m.join_date BETWEEN ? AND ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0L;

        } catch (SQLException e) {
            throw new RuntimeException("Error counting new members", e);
        }
    }

    public double totalActiveRequired(String collectivityId, LocalDate to) {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM membership_fee
            WHERE collectivity_id = ?
              AND status = 'ACTIVE'
              AND eligible_from <= ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;

        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total required", e);
        }
    }

    public long countUpToDateMembers(String collectivityId, LocalDate to, double totalRequired) {
        String sql = """
            SELECT COUNT(*) FROM (
                SELECT m.id,
                    COALESCE((
                        SELECT SUM(mp.amount)
                        FROM member_payment mp
                        JOIN membership_fee mf ON mp.membership_fee_id = mf.id
                        WHERE mp.member_id = m.id
                          AND mf.collectivity_id = ?
                          AND mf.status = 'ACTIVE'
                          AND mf.eligible_from <= ?
                    ), 0) AS total_paid
                FROM member m
                WHERE m.id IN (
                    SELECT cm.member_id FROM collectivity_member cm
                    WHERE cm.collectivity_id = ?
                )
            ) sub
            WHERE total_paid >= ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setDouble(4, totalRequired);

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0L;

        } catch (SQLException e) {
            throw new RuntimeException("Error counting up-to-date members", e);
        }
    }
}