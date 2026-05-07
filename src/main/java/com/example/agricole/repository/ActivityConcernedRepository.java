package com.example.agricole.repository;

import com.example.agricole.enums.MemberOccupation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityConcernedRepository {

    private final DataSource dataSource;

    public void saveAll(String activityId, List<MemberOccupation> occupations) {

        String sql = """
            INSERT INTO activity_member_concerned (activity_id, occupation)
            VALUES (?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (MemberOccupation occ : occupations) {
                ps.setString(1, activityId);
                ps.setString(2, occ.name());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberOccupation> findByActivityId(String activityId) {

        String sql = """
            SELECT occupation
            FROM activity_member_concerned
            WHERE activity_id = ?
        """;

        List<MemberOccupation> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(MemberOccupation.valueOf(rs.getString("occupation")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}