package model.dao;

import model.DatabaseConnection;
import model.Lecturer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT l.*, d.name as dep_name FROM lecturers l LEFT JOIN departments d ON l.department_id = d.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Lecturer lecturer = new Lecturer(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getInt("department_id"),
                        rs.getString("email"),
                        rs.getString("mobile_number")
                );
                lecturer.setDepartmentName(rs.getString("dep_name"));
                lecturer.setCoursesTeaching(getCoursesForLecturer(conn, rs.getInt("id")));
                lecturers.add(lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    // Get the lecturer profile using the user ID after login
    // Used to display lecturer details and courses in the dashboard
    public Lecturer getLecturerByUserId(int userId) {
        String sql = "SELECT l.*, d.name as dep_name FROM lecturers l LEFT JOIN departments d ON l.department_id = d.id WHERE l.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Lecturer lecturer = new Lecturer(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getInt("department_id"),
                        rs.getString("email"),
                        rs.getString("mobile_number")
                );
                lecturer.setDepartmentName(rs.getString("dep_name"));
                lecturer.setCoursesTeaching(getCoursesForLecturer(conn, rs.getInt("id")));
                return lecturer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    private String getCoursesForLecturer(Connection conn, int lecturerId) throws SQLException {
        String sql = "SELECT course_code FROM courses WHERE lecturer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            List<String> courses = new ArrayList<>();
            while(rs.next()) {
                courses.add(rs.getString("course_code"));
            }
            return String.join(", ", courses);
        }
    }

    public boolean addLecturer(Lecturer l) {
        String sql = "INSERT INTO lecturers (user_id, full_name, department_id, email, mobile_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (l.getUserId() > 0) stmt.setInt(1, l.getUserId()); else stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, l.getFullName());
            if (l.getDepartmentId() > 0) stmt.setInt(3, l.getDepartmentId()); else stmt.setNull(3, Types.INTEGER);
            stmt.setString(4, l.getEmail());
            stmt.setString(5, l.getMobileNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLecturer(Lecturer l) {
        String sql = "UPDATE lecturers SET full_name=?, department_id=?, email=?, mobile_number=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getFullName());
            if (l.getDepartmentId() > 0) stmt.setInt(2, l.getDepartmentId()); else stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, l.getEmail());
            stmt.setString(4, l.getMobileNumber());
            stmt.setInt(5, l.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLecturer(int id) {
        String sql = "DELETE FROM lecturers WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
