package model.dao;

import model.DatabaseConnection;
import model.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // Real enrolled courses and grades for one student, replacing the hardcoded
    // These are the sample rows that we used to have hard-coded inside
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, c.course_code, c.course_name, c.credits " +
                     "FROM enrollments e JOIN courses c ON e.course_id = c.id " +
                     "WHERE e.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enrollment en = new Enrollment(
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("grade")
                );
                en.setCourseCode(rs.getString("course_code"));
                en.setCourseName(rs.getString("course_name"));
                en.setCredits(rs.getInt("credits"));
                enrollments.add(en);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public boolean enrollStudent(int studentId, int courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id, grade) VALUES (?, ?, NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGrade(int studentId, int courseId, String grade) {
        String sql = "UPDATE enrollments SET grade=? WHERE student_id=? AND course_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, grade);
            stmt.setInt(2, studentId);
            stmt.setInt(3, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
