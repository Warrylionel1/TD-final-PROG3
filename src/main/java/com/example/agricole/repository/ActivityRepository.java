package com.example.agricole.repository;

import com.example.agricole.entity.Activity;
import com.example.agricole.enums.ActivityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {

    private final DataSource dataSource;

    public void saveAll(List<Activity> activities) {

        String sql = """
            INSERT INTO collectivity_activity (
                id,
                collectivity_id,
                label,
                activity_type,
                executive_date,
                week_ordinal,
                day_of_week
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Activity a : activities) {

                ps.setString(1, a.getId());
                ps.setString(2, a.getCollectivityId());
                ps.setString(3, a.getLabel());
                ps.setString(4, a.getActivityType().name());

                if (a.getExecutiveDate() != null) {
                    ps.setDate(5, Date.valueOf(a.getExecutiveDate()));
                } else {
                    ps.setNull(5, Types.DATE);
                }

                if (a.getWeekOrdinal() != null) {
                    ps.setInt(6, a.getWeekOrdinal());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                if (a.getDayOfWeek() != null) {
                    ps.setString(7, a.getDayOfWeek());
                } else {
                    ps.setNull(7, Types.VARCHAR);
                }

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting activities", e);
        }
    }

    public List<Activity> findByCollectivityId(String collectivityId) {

        String sql = """
        SELECT id,
               collectivity_id,
               label,
               activity_type,
               executive_date,
               week_ordinal,
               day_of_week
        FROM collectivity_activity
        WHERE collectivity_id = ?
        ORDER BY executive_date NULLS LAST
    """;

        List<Activity> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Activity a = new Activity();
                a.setId(rs.getString("id"));
                a.setCollectivityId(rs.getString("collectivity_id"));
                a.setLabel(rs.getString("label"));
                a.setActivityType(ActivityType.valueOf(rs.getString("activity_type")));

                Date execDate = rs.getDate("executive_date");
                if (execDate != null) {
                    a.setExecutiveDate(execDate.toLocalDate());
                }

                int weekOrdinal = rs.getInt("week_ordinal");
                if (!rs.wasNull()) {
                    a.setWeekOrdinal(weekOrdinal);
                }

                a.setDayOfWeek(rs.getString("day_of_week"));

                result.add(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching activities", e);
        }

        return result;
    }
}