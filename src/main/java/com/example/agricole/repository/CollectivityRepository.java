package com.example.agricole.repository;

import com.example.agricole.dto.CreateCollectivityStructure;
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

    public void saveWithRelations(Collectivity collectivity, CreateCollectivityStructure structure) {
        String insertCollectivity = """
            INSERT INTO collectivity (id, location, specialty, creation_date, federation_approval)
            VALUES (?, ?, ?, CURRENT_DATE, ?)
        """;
        String insertRelation = """
            INSERT INTO collectivity_member (collectivity_id, member_id)
            VALUES (?, ?)
        """;
        String insertStructure = """
            INSERT INTO collectivity_structure
            (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertCollectivity)) {
                ps.setString(1, collectivity.getId());
                ps.setString(2, collectivity.getLocation());
                ps.setString(3, "Default");
                ps.setBoolean(4, collectivity.isFederationApproval());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(insertRelation)) {
                for (Member m : collectivity.getMembers()) {
                    ps.setString(1, collectivity.getId());
                    ps.setString(2, m.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(insertStructure)) {
                ps.setString(1, collectivity.getId());
                ps.setString(2, structure.getPresident());
                ps.setString(3, structure.getVicePresident());
                ps.setString(4, structure.getTreasurer());
                ps.setString(5, structure.getSecretary());
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving collectivity with relations", e);
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
}