package model.dao;

import model.DatabaseConnection;
import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.name as degree_name FROM students s LEFT JOIN degrees d ON s.degree_id = d.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_id_str"),
                        rs.getString("full_name"),
                        rs.getInt("degree_id"),
                        rs.getString("email"),
                        rs.getString("mobile_number")
                );
                student.setDegreeName(rs.getString("degree_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Looks for  the Student profile linked to a given login (users.id)
    // Uses the Student dashboard to show "my" profile/timetable/courses after login.
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT s.*, d.name as degree_name FROM students s LEFT JOIN degrees d ON s.degree_id = d.id WHERE s.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("student_id_str"),
                        rs.getString("full_name"),
                        rs.getInt("degree_id"),
                        rs.getString("email"),
                        rs.getString("mobile_number")
                );
                student.setDegreeName(rs.getString("degree_name"));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No linked student profile yet - ex. account just self-registered
    }

    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (user_id, student_id_str, full_name, degree_id, email, mobile_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (s.getUserId() > 0) stmt.setInt(1, s.getUserId()); else stmt.setNull(1, Types.INTEGER);
            stmt.setString(2, s.getStudentIdStr());
            stmt.setString(3, s.getFullName());
            if (s.getDegreeId() > 0) stmt.setInt(4, s.getDegreeId()); else stmt.setNull(4, Types.INTEGER);
            stmt.setString(5, s.getEmail());
            stmt.setString(6, s.getMobileNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET student_id_str=?, full_name=?, degree_id=?, email=?, mobile_number=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getStudentIdStr());
            stmt.setString(2, s.getFullName());
            if (s.getDegreeId() > 0) stmt.setInt(3, s.getDegreeId()); else stmt.setNull(3, Types.INTEGER);
            stmt.setString(4, s.getEmail());
            stmt.setString(5, s.getMobileNumber());
            stmt.setInt(6, s.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
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
