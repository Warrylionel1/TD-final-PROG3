package com.example.agricole.repository;

import com.example.agricole.dto.CreateActivityMemberAttendance;
import com.example.agricole.enums.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ActivityAttendanceRepository {

    private final DataSource dataSource;

    public void saveAll(String activityId, List<CreateActivityMemberAttendance> requests) {

        String sql = """
            INSERT INTO activity_member_attendance
            (id, activity_id, member_id, attendance_status)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (CreateActivityMemberAttendance req : requests) {

                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, activityId);
                ps.setString(3, req.getMemberIdentifier());
                ps.setString(4, req.getAttendanceStatus().name());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving attendance", e);
        }
    }

    public List<AttendanceStatus> findByActivityIdAndMember(String activityId, String memberId) {

        String sql = """
            SELECT attendance_status
            FROM activity_member_attendance
            WHERE activity_id = ? AND member_id = ?
        """;

        List<AttendanceStatus> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setString(2, memberId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(AttendanceStatus.valueOf(rs.getString("attendance_status")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
