package com.example.agricole.repository;

import com.example.agricole.entity.Member;
import com.example.agricole.enums.Gender;
import com.example.agricole.enums.MemberOccupation;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepository {

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Member member) {

        String sql = """
            INSERT INTO member (
                id, first_name, last_name, birth_date, gender,
                address, profession, phone_number, email,
                occupation, join_date
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getId());
            ps.setString(2, member.getFirstName());
            ps.setString(3, member.getLastName());

            if (member.getBirthDate() != null) {
                ps.setDate(4, Date.valueOf(member.getBirthDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            if (member.getGender() != null) {
                ps.setString(5, member.getGender().name());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setString(6, member.getAddress());
            ps.setString(7, member.getProfession());
            ps.setString(8, member.getPhoneNumber());
            ps.setString(9, member.getEmail());

            if (member.getOccupation() != null) {
                ps.setString(10, member.getOccupation().name());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }

            if (member.getJoinDate() != null) {
                ps.setDate(11, Date.valueOf(member.getJoinDate()));
            } else {
                ps.setDate(11, Date.valueOf(LocalDate.now())); // sécurité
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving member", e);
        }
    }

    public List<Member> findByIds(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = String.join(",", ids.stream().map(id -> "?").toList());

        String sql = "SELECT * FROM member WHERE id IN (" + placeholders + ")";

        List<Member> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setString(i + 1, ids.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(mapRowToMember(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching members by ids", e);
        }

        return result;
    }

    public Member findById(String id) {

        String sql = "SELECT * FROM member WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToMember(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding member", e);
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

    public List<Member> findByCollectivityId(String collectivityId) {

        String sql = """
        SELECT
            m.id,
            m.first_name,
            m.last_name,
            m.birth_date,
            m.gender,
            m.address,
            m.profession,
            m.phone_number,
            m.email,
            m.occupation,
            m.join_date
        FROM member m
        INNER JOIN collectivity_member cm
            ON cm.member_id = m.id
        WHERE cm.collectivity_id = ?
    """;

        List<Member> members = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                members.add(mapRowToMember(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching members by collectivity", e);
        }

        return members;
    }
}