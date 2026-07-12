package model.dao;

import model.DatabaseConnection;
import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, l.full_name as lecturer_name FROM courses c LEFT JOIN lecturers l ON c.lecturer_id = l.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getInt("lecturer_id")
                );
                course.setLecturerName(rs.getString("lecturer_name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Courses a specific lecturer is teaching - used by the Lecturer dashboard.
    public List<Course> getCoursesByLecturerId(int lecturerId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getInt("lecturer_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public boolean addCourse(Course c) {
        String sql = "INSERT INTO courses (course_code, course_name, credits, lecturer_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getCourseCode());
            stmt.setString(2, c.getCourseName());
            stmt.setInt(3, c.getCredits());
            if (c.getLecturerId() > 0) stmt.setInt(4, c.getLecturerId()); else stmt.setNull(4, Types.INTEGER);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course c) {
        String sql = "UPDATE courses SET course_code=?, course_name=?, credits=?, lecturer_id=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getCourseCode());
            stmt.setString(2, c.getCourseName());
            stmt.setInt(3, c.getCredits());
            if (c.getLecturerId() > 0) stmt.setInt(4, c.getLecturerId()); else stmt.setNull(4, Types.INTEGER);
            stmt.setInt(5, c.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id=?";
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
