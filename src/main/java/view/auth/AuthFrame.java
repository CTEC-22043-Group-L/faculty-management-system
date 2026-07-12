package view.auth;

import view.components.*;
import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthFrame extends JFrame {
    private JPanel rightPanel;
    private CardLayout cardLayout;
    private AuthController controller;

    public AuthFrame() {
        controller = new AuthController(this);
        setTitle("Faculty Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Left Panel (Purple)
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // Right Panel (White with CardLayout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);

        rightPanel.add(createSignInPanel(), "SignIn");
        rightPanel.add(createSignUpPanel(), "SignUp");

        add(rightPanel, BorderLayout.CENTER);

        cardLayout.show(rightPanel, "SignIn");
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(350, 500));
        panel.setBackground(new Color(138, 43, 226)); // Purple
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add empty space at top
        panel.add(Box.createRigidArea(new Dimension(0, 100)));

        // Mock Graduation Cap Icon using Unicode for now
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel1 = new JLabel("Faculty Management");
        titleLabel1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel1.setForeground(Color.WHITE);
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel2 = new JLabel("System");
        titleLabel2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel2.setForeground(Color.WHITE);
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(titleLabel1);
        panel.add(titleLabel2);

        panel.add(Box.createVerticalGlue());

        JLabel footer1 = new JLabel("Faculty of Computing & Technology");
        footer1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        footer1.setForeground(Color.WHITE);
        footer1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel footer2 = new JLabel("Manage your academic journey");
        footer2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer2.setForeground(Color.WHITE);
        footer2.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(footer1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(footer2);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        return panel;
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header Tabs
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblSignIn = new JLabel("Sign In");
        lblSignIn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSignIn.setForeground(new Color(138, 43, 226));
        lblSignIn.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(138, 43, 226))); // Underline

        JLabel lblSignUp = new JLabel("Sign Up");
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSignUp.setForeground(Color.GRAY);
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "SignUp");
            }
        });

        headerPanel.add(lblSignIn);
        headerPanel.add(lblSignUp);
        headerPanel.setMaximumSize(new Dimension(400, 40));

        // Form Fields
        panel.add(headerPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        addLabel(panel, "Username");
        RoundedTextField txtUsername = new RoundedTextField(20, 15);
        txtUsername.setMaximumSize(new Dimension(300, 35));
        panel.add(txtUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        addLabel(panel, "Password");
        RoundedPasswordField txtPassword = new RoundedPasswordField(20, 15);
        txtPassword.setMaximumSize(new Dimension(300, 35));
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        addLabel(panel, "Role");
        RoleTogglePanel rolePanel = new RoleTogglePanel();
        rolePanel.setMaximumSize(new Dimension(300, 30));
        panel.add(rolePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        RoundedButton btnLogin = new RoundedButton("Sign In", 15, new Color(138, 43, 226), Color.WHITE);
        btnLogin.setMaximumSize(new Dimension(300, 40));
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String role = rolePanel.getSelectedRole();
            controller.handleLogin(username, password, role);
        });
        panel.add(btnLogin);

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Header Tabs
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblSignIn = new JLabel("Sign In");
        lblSignIn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSignIn.setForeground(Color.GRAY);
        lblSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "SignIn");
            }
        });

        JLabel lblSignUp = new JLabel("Sign Up");
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSignUp.setForeground(new Color(138, 43, 226));
        lblSignUp.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(138, 43, 226)));

        headerPanel.add(lblSignIn);
        headerPanel.add(lblSignUp);
        headerPanel.setMaximumSize(new Dimension(400, 40));

        panel.add(headerPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        addLabel(panel, "Username");
        RoundedTextField txtUsername = new RoundedTextField(20, 15);
        txtUsername.setMaximumSize(new Dimension(300, 35));
        panel.add(txtUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        addLabel(panel, "Password");
        RoundedPasswordField txtPassword = new RoundedPasswordField(20, 15);
        txtPassword.setMaximumSize(new Dimension(300, 35));
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        addLabel(panel, "Confirm Password");
        RoundedPasswordField txtConfirmPassword = new RoundedPasswordField(20, 15);
        txtConfirmPassword.setMaximumSize(new Dimension(300, 35));
        panel.add(txtConfirmPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        addLabel(panel, "Role");
        RoleTogglePanel rolePanel = new RoleTogglePanel();
        rolePanel.setMaximumSize(new Dimension(300, 30));
        panel.add(rolePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        RoundedButton btnRegister = new RoundedButton("Sign Up", 15, new Color(138, 43, 226), Color.WHITE);
        btnRegister.setMaximumSize(new Dimension(300, 40));
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            String role = rolePanel.getSelectedRole();

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return;
            }
            controller.handleRegistration(username, password, role);
        });
        panel.add(btnRegister);

        return panel;
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(138, 43, 226));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(Color.WHITE);
        wrapper.setMaximumSize(new Dimension(300, 20));
        wrapper.add(label);
        panel.add(wrapper);
    }
}
