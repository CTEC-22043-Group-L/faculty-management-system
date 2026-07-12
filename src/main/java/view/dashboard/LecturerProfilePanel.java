package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.RoundedButton;
import view.components.RoundedTextField;
import model.User;
import model.Lecturer;
import model.Department;
import model.dao.LecturerDAO;
import model.dao.DepartmentDAO;
import java.util.List;

// This class provides the UI for viewing and editing a lecturer's profile
public class LecturerProfilePanel extends JPanel {

    private LecturerDAO lecturerDAO = new LecturerDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private Lecturer lecturer;
    private RoundedTextField txtFullName, txtEmail, txtMobile;
    private JComboBox<String> cmbDepartment;
    private List<Department> departmentList;

    public LecturerProfilePanel(User currentUser) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel title = new JLabel("Profile Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 40)));

        lecturer = lecturerDAO.getLecturerByUserId(currentUser.getId());
        departmentList = departmentDAO.getAllDepartments();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        txtFullName = addField(formPanel, gbc, "Full Name", 0, lecturer != null ? lecturer.getFullName() : "");
        cmbDepartment = addDepartmentDropdown(formPanel, gbc, 1, lecturer != null ? lecturer.getDepartmentId() : 0);
        txtEmail = addField(formPanel, gbc, "Email", 2, lecturer != null ? lecturer.getEmail() : "");
        txtMobile = addField(formPanel, gbc, "Mobile Number", 3, lecturer != null ? lecturer.getMobileNumber() : "");

        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 40)));

        RoundedButton btnSave = new RoundedButton("Save changes", 15, new Color(138, 43, 226), Color.WHITE);
        btnSave.setMaximumSize(new Dimension(300, 40));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(btnSave);

        if (lecturer == null) {
            btnSave.setEnabled(false);
            JLabel notice = new JLabel("No profile found yet. Please contact Admin to link your account.");
            notice.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            notice.setForeground(Color.GRAY);
            notice.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(notice);
        } else {
            btnSave.addActionListener(e -> handleSave());
        }
    }

    private void handleSave() {
       
        int resolvedDepartmentId = 0;
        int selected = cmbDepartment.getSelectedIndex();
        if (selected > 0 && selected - 1 < departmentList.size()) {
            resolvedDepartmentId = departmentList.get(selected - 1).getId();
        }

        lecturer.setFullName(txtFullName.getText().trim());
        lecturer.setDepartmentId(resolvedDepartmentId);
        lecturer.setEmail(txtEmail.getText().trim());
        lecturer.setMobileNumber(txtMobile.getText().trim());

        if (lecturerDAO.updateLecturer(lecturer)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.");
        }
    }

    private RoundedTextField addField(JPanel formPanel, GridBagConstraints gbc, String label, int row, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(138, 43, 226));

        RoundedTextField txt = new RoundedTextField(20, 15);
        txt.setText(value != null ? value : "");

        txt.setPreferredSize(new Dimension(txt.getPreferredSize().width, 40));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        formPanel.add(txt, gbc);

        return txt;
    }

    
    private JComboBox<String> addDepartmentDropdown(JPanel formPanel, GridBagConstraints gbc, int row, int currentDepartmentId) {
        JLabel lbl = new JLabel("Department");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(138, 43, 226));

        String[] options = new String[departmentList.size() + 1];
        options[0] = "-- Select Department --";
        int selectIndex = 0;
        for (int i = 0; i < departmentList.size(); i++) {
            options[i + 1] = departmentList.get(i).getName();
            if (departmentList.get(i).getId() == currentDepartmentId) selectIndex = i + 1;
        }

        JComboBox<String> combo = new JComboBox<>(options);
        combo.setSelectedIndex(selectIndex);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 1));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        formPanel.add(combo, gbc);

        return combo;
    }
}
