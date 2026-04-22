package com.example.agricole.repository;

import com.example.agricole.dto.CreateCollectivityStructure;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.entity.CollectivityStructure;
import com.example.agricole.entity.Member;
import com.example.agricole.enums.Gender;
import com.example.agricole.enums.MemberOccupation;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        } catch (Exception e) {
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

        String sql = "SELECT * FROM collectivity WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Collectivity c = new Collectivity();
            c.setId(rs.getString("id"));
            c.setLocation(rs.getString("location"));
            c.setFederationApproval(rs.getBoolean("federation_approval"));
            c.setNumber(rs.getString("number"));
            c.setName(rs.getString("name"));

            c.setMembers(getMembers(conn, id));
            c.setStructure(getStructure(conn, id));

            return c;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding collectivity", e);
        }
    }

    private List<Member> getMembers(Connection conn, String collectivityId) throws SQLException {

        String sql = """
            SELECT m.* FROM member m
            JOIN collectivity_member cm ON m.id = cm.member_id
            WHERE cm.collectivity_id = ?
        """;

        List<Member> members = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                members.add(mapRowToMember(rs));
            }
        }

        return members;
    }

    private CollectivityStructure getStructure(Connection conn, String collectivityId) throws SQLException {

        String sql = "SELECT * FROM collectivity_structure WHERE collectivity_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            CollectivityStructure structure = new CollectivityStructure();

            structure.setPresident(getMemberById(conn, rs.getString("president_id")));
            structure.setVicePresident(getMemberById(conn, rs.getString("vice_president_id")));
            structure.setTreasurer(getMemberById(conn, rs.getString("treasurer_id")));
            structure.setSecretary(getMemberById(conn, rs.getString("secretary_id")));

            return structure;
        }
    }

    private Member getMemberById(Connection conn, String id) throws SQLException {

        String sql = "SELECT * FROM member WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToMember(rs);
            }

            return null;
        }
    }

    private Member mapRowToMember(ResultSet rs) throws SQLException {

        Member m = new Member();

        m.setId(rs.getString("id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));

        Date birthDate = rs.getDate("birth_date");
        if (birthDate != null) {
            m.setBirthDate(birthDate.toLocalDate());
        }

        String gender = rs.getString("gender");
        if (gender != null) {
            m.setGender(Gender.valueOf(gender));
        }

        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getString("phone_number"));
        m.setEmail(rs.getString("email"));

        String occupation = rs.getString("occupation");
        if (occupation != null) {
            m.setOccupation(MemberOccupation.valueOf(occupation));
        }

        Date joinDate = rs.getDate("join_date");
        if (joinDate != null) {
            m.setJoinDate(joinDate.toLocalDate());
        }

        return m;
    }
}