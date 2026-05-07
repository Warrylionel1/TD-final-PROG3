package com.example.agricole.repository;

import com.example.agricole.entity.MembershipFee;
import com.example.agricole.enums.ActivityStatus;
import com.example.agricole.enums.Frequency;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MembershipFeeRepository {
    private final DataSource dataSource;

    public MembershipFeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MembershipFee> findByCollectivityId(String collectivityId) {
        String sql = "SELECT * FROM membership_fee WHERE collectivity_id = ?";
        List<MembershipFee> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching membership fees", e);
        }
        return list;
    }

    public MembershipFee save(MembershipFee fee) {
        if (fee.getId() == null) {
            fee.setId(UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO membership_fee (id, collectivity_id, eligible_from, frequency, amount, label, status) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fee.getId());
            ps.setString(2, fee.getCollectivityId());
            ps.setDate(3, Date.valueOf(fee.getEligibleFrom()));
            ps.setString(4, fee.getFrequency().name());
            ps.setDouble(5, fee.getAmount());
            ps.setString(6, fee.getLabel());
            ps.setString(7, fee.getStatus().name());
            ps.executeUpdate();
            return fee;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving membership fee", e);
        }
    }

    public MembershipFee findById(String id) {
        String sql = "SELECT * FROM membership_fee WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding membership fee", e);
        }
    }

    private MembershipFee mapRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setId(rs.getString("id"));
        fee.setCollectivityId(rs.getString("collectivity_id"));
        fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
        fee.setFrequency(Frequency.valueOf(rs.getString("frequency")));
        fee.setAmount(rs.getDouble("amount"));
        fee.setLabel(rs.getString("label"));
        fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
        return fee;
    }
}