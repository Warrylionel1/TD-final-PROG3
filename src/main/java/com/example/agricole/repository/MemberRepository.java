package com.example.agricole.repository;

import com.example.agricole.entity.Member;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
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
            INSERT INTO member (first_name, last_name, email, occupation)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getOccupation().name());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                member.setId(rs.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving member", e);
        }
    }

    public List<Member> findByIds(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String inSql = String.join(",", ids.stream().map(id -> "?").toList());

        String sql = "SELECT * FROM member WHERE id IN (" + inSql + ")";

        List<Member> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setString(i + 1, ids.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Member m = new Member();
                m.setId(rs.getString("id"));
                m.setFirstName(rs.getString("first_name"));
                m.setLastName(rs.getString("last_name"));
                m.setEmail(rs.getString("email"));

                result.add(m);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching members by ids", e);
        }

        return result;
    }

}