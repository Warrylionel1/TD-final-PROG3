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

        String sql = "INSERT INTO collectivity (location, federation_approval) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, collectivity.getLocation());
            ps.setBoolean(2, collectivity.isFederationApproval());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                collectivity.setId(rs.getString(1));
            }

            insertMembersRelation(conn, collectivity);

        } catch (SQLException e) {
            throw new RuntimeException("Error saving collectivity", e);
        }
    }

    public Collectivity findById(String id) {

        String sql = "SELECT * FROM collectivity WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Collectivity c = new Collectivity();
                c.setId(rs.getString("id"));
                c.setLocation(rs.getString("location"));
                c.setFederationApproval(rs.getBoolean("federation_approval"));
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