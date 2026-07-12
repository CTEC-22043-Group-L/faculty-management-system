package view.dashboard;

import model.User;
import view.components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Faculty Management System - " + user.getRole() + " Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        initializeViews();

        add(contentPanel, BorderLayout.CENTER);
        
        // Show first default view based on role
        if (currentUser.getRole().equals("Admin")) {
            cardLayout.show(contentPanel, "Students");
        } else {
            cardLayout.show(contentPanel, "Profile");
        }
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, 600));
        panel.setBackground(new Color(138, 43, 226)); // Purple
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome text
        JLabel lblWelcome = new JLabel("Welcome, " + (currentUser.getRole().equals("Admin") ? "Admin" : currentUser.getUsername()));
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblWelcome);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Navigation Buttons based on Role
        if (currentUser.getRole().equals("Admin")) {
            addNavButton(panel, "Students");
            addNavButton(panel, "Lecturers");
            addNavButton(panel, "Courses");
            addNavButton(panel, "Departments");
            addNavButton(panel, "Degrees");
        } else {
            // Student or Lecturer
            addNavButton(panel, "Profile Details", "Profile");
            addNavButton(panel, "Time table", "Timetable");
            String coursesLabel = currentUser.getRole().equals("Lecturer") ? "Courses Teaching" : "Courses Enrolled";
            addNavButton(panel, coursesLabel, "Courses");
        }

        panel.add(Box.createVerticalGlue());

        // Logout Button
        JButton btnLogout = new JButton("<html><span style='font-size:20px;'>&#10162;</span></html>"); // Logout Icon approximation
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(new Color(138, 43, 226));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new view.auth.AuthFrame().setVisible(true);
            });
        });
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout);
        panel.add(logoutPanel);

        return panel;
    }
    
    private void addNavButton(JPanel sidebar, String text) {
        addNavButton(sidebar, text, text);
    }

    private void addNavButton(JPanel sidebar, String text, String cardName) {
    RoundedButton btn = new RoundedButton(text, 12, Color.WHITE, Color.GRAY);
    btn.setMaximumSize(new Dimension(240, 40));
    btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    btn.setHorizontalAlignment(SwingConstants.CENTER);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    btn.addActionListener((ActionEvent e) -> {
        cardLayout.show(contentPanel, cardName);
    });

    sidebar.add(btn);
    sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
}

    private void initializeViews() {
        if (currentUser.getRole().equals("Admin")) {
            contentPanel.add(new AdminDegreesPanel(), "Degrees");
            contentPanel.add(new AdminDepartmentsPanel(), "Departments");
            contentPanel.add(new AdminCoursesPanel(), "Courses");
            contentPanel.add(new AdminLecturersPanel(), "Lecturers");
            contentPanel.add(new AdminStudentsPanel(), "Students");
        } else if (currentUser.getRole().equals("Lecturer")) {
            contentPanel.add(new LecturerProfilePanel(currentUser), "Profile");
            contentPanel.add(new StudentTimetablePanel(), "Timetable"); // static mock timetable; no timetable table exists in the schema yet
            contentPanel.add(new LecturerCoursesPanel(currentUser), "Courses");
        } else {
            contentPanel.add(new StudentProfilePanel(currentUser), "Profile");
            contentPanel.add(new StudentTimetablePanel(), "Timetable");
            contentPanel.add(new StudentCoursesPanel(currentUser), "Courses");
        }
    }
}
