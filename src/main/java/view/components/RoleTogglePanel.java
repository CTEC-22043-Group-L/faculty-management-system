package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoleTogglePanel extends JPanel {
    private RoundedButton btnAdmin;
    private RoundedButton btnStudent;
    private RoundedButton btnLecturer;
    private String selectedRole = "Admin";

    public RoleTogglePanel() {
        setLayout(new GridLayout(1, 3, 10, 0));
        setOpaque(false);
        
        Color purple = new Color(138, 43, 226);
        Color lightGray = new Color(220, 220, 220);
        
        btnAdmin = new RoundedButton("Admin", 20, purple, Color.WHITE);
        btnStudent = new RoundedButton("Student", 20, lightGray, Color.WHITE);
        btnLecturer = new RoundedButton("Lecturer", 20, lightGray, Color.WHITE);
        
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoundedButton source = (RoundedButton) e.getSource();
                selectedRole = source.getText();
                updateButtons(purple, lightGray);
            }
        };
        
        btnAdmin.addActionListener(listener);
        btnStudent.addActionListener(listener);
        btnLecturer.addActionListener(listener);
        
        add(btnAdmin);
        add(btnStudent);
        add(btnLecturer);
    }
    
    private void updateButtons(Color active, Color inactive) {
        btnAdmin.setRestingBackground(selectedRole.equals("Admin") ? active : inactive);
        btnStudent.setRestingBackground(selectedRole.equals("Student") ? active : inactive);
        btnLecturer.setRestingBackground(selectedRole.equals("Lecturer") ? active : inactive);
    }
    
    public String getSelectedRole() {
        return selectedRole;
    }
}
