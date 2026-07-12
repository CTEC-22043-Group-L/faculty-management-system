package model.dao;

import model.DatabaseConnection;
import model.Degree;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DegreeDAO {
    public List<Degree> getAllDegrees() {
        List<Degree> degrees = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.department_id, d.no_of_students, dep.name as dep_name " +
                     "FROM degrees d LEFT JOIN departments dep ON d.department_id = dep.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Degree degree = new Degree(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("department_id"),
                        rs.getInt("no_of_students")
                );
                degree.setDepartmentName(rs.getString("dep_name"));
                degrees.add(degree);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return degrees;
    }

    // Finds a degree by name (case-insensitive), used when a Student types their
    // degree name directly into their own profile instead of picking from a dropdown.
    public Degree getDegreeByName(String name) {
        for (Degree d : getAllDegrees()) {
            if (d.getName().equalsIgnoreCase(name.trim())) return d;
        }
        return null;
    }

    public boolean addDegree(Degree d) {
        String sql = "INSERT INTO degrees (name, department_id, no_of_students) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setInt(2, d.getDepartmentId());
            stmt.setInt(3, d.getNoOfStudents());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDegree(Degree d) {
        String sql = "UPDATE degrees SET name=?, department_id=?, no_of_students=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setInt(2, d.getDepartmentId());
            stmt.setInt(3, d.getNoOfStudents());
            stmt.setInt(4, d.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDegree(int id) {
        String sql = "DELETE FROM degrees WHERE id=?";
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
