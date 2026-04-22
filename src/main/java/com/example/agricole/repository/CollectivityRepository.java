package com.example.agricole.repository;

import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.Member;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;

@Repository
public class CollectivityRepository {

    private final DataSource dataSource;

    public CollectivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Collectivity collectivity) {
        if (collectivity.getId() == null) {
            collectivity.setId(java.util.UUID.randomUUID().toString());
        }
        String sql = "INSERT INTO collectivity (id, location, federation_approval) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivity.getId());
            ps.setString(2, collectivity.getLocation());
            ps.setBoolean(3, collectivity.isFederationApproval());
            ps.executeUpdate();
            insertMembersRelation(conn, collectivity);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving collectivity", e);
        }
    }

    public void updateNumberAndName(String id, String number, String name) {
        String sql = "UPDATE collectivity SET number = ?, name = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ps.setString(2, name);
            ps.setString(3, id);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Collectivity not found for update");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating collectivity identity", e);
        }
    }

    public boolean existsByNumber(String number) {
        String sql = "SELECT 1 FROM collectivity WHERE number = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking number existence", e);
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM collectivity WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking name existence", e);
        }
    }

    public Collectivity findById(String id) {
        String sql = "SELECT id, location, federation_approval, number, name FROM collectivity WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Collectivity c = new Collectivity();
                c.setId(rs.getString("id"));
                c.setLocation(rs.getString("location"));
                c.setFederationApproval(rs.getBoolean("federation_approval"));
                c.setNumber(rs.getString("number"));
                c.setName(rs.getString("name"));
                return c;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding collectivity", e);
        }
    }

    private void insertMembersRelation(Connection conn, Collectivity collectivity) throws SQLException {
        if (collectivity.getMembers() == null || collectivity.getMembers().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO collectivity_member (collectivity_id, member_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Member member : collectivity.getMembers()) {
                ps.setString(1, collectivity.getId());
                ps.setString(2, member.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}