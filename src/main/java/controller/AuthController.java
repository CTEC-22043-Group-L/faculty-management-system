package controller;

import model.User;
import model.Student;
import model.Lecturer;
import model.dao.UserDAO;
import model.dao.StudentDAO;
import model.dao.LecturerDAO;
import view.auth.AuthFrame;
import view.dashboard.DashboardFrame;

import javax.swing.*;

public class AuthController {
    private AuthFrame authFrame;
    private UserDAO userDAO;
    private StudentDAO studentDAO;
    private LecturerDAO lecturerDAO;

    public AuthController(AuthFrame authFrame) {
        this.authFrame = authFrame;
        this.userDAO = new UserDAO();
        this.studentDAO = new StudentDAO();
        this.lecturerDAO = new LecturerDAO();
    }

    public void handleLogin(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(authFrame, "Please enter username and password.");
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            if (user.getRole().equals(role)) {
                JOptionPane.showMessageDialog(authFrame, "Login successful as " + role + "!");
                authFrame.dispose();

                // Open dashboard and pass the logged-in user
                SwingUtilities.invokeLater(() -> {
                    DashboardFrame dashboard = new DashboardFrame(user);
                    dashboard.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(authFrame, "Role mismatch. You are not a " + role + ".");
            }
        } else {
            JOptionPane.showMessageDialog(authFrame, "Invalid credentials.");
        }
    }

    public void handleRegistration(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(authFrame, "Please enter username and password.");
            return;
        }

        User newUser = new User(0, username, password, role);
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            // Create a student or lecturer
            if (role.equals("Student")) {
                Student stub = new Student(0, newUser.getId(), "PENDING-" + newUser.getId(), username, 0, "", "");
                studentDAO.addStudent(stub);
            } else if (role.equals("Lecturer")) {
                Lecturer stub = new Lecturer(0, newUser.getId(), username, 0, "", "");
                lecturerDAO.addLecturer(stub);
            }
            JOptionPane.showMessageDialog(authFrame, "Registration successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(authFrame, "Registration failed. Username might already exist.");
        }
    }
}
