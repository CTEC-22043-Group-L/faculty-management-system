package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.RoundedButton;
import view.components.RoundedTextField;
import model.User;
import model.Student;
import model.Degree;
import model.dao.StudentDAO;
import model.dao.DegreeDAO;
import java.util.List;

public class StudentProfilePanel extends JPanel {

    private StudentDAO studentDAO = new StudentDAO();
    private DegreeDAO degreeDAO = new DegreeDAO();
    private Student student; // the real logged-in student's row, or null if no profile linked yet
    private RoundedTextField txtFullName, txtStudentId, txtEmail, txtMobile;
    private JComboBox<String> cmbDegree;
    private List<Degree> degreeList;

    public StudentProfilePanel(User currentUser) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel title = new JLabel("Profile Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 40)));

        student = studentDAO.getStudentByUserId(currentUser.getId());
        degreeList = degreeDAO.getAllDegrees();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        txtFullName = addField(formPanel, gbc, "Full Name", 0, student != null ? student.getFullName() : "");
        txtStudentId = addField(formPanel, gbc, "Student ID", 1, student != null ? student.getStudentIdStr() : "");
        cmbDegree = addDegreeDropdown(formPanel, gbc, 2, student != null ? student.getDegreeId() : 0);
        txtEmail = addField(formPanel, gbc, "Email", 3, student != null ? student.getEmail() : "");
        txtMobile = addField(formPanel, gbc, "Mobile Number", 4, student != null ? student.getMobileNumber() : "");

        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 40)));

        RoundedButton btnSave = new RoundedButton("Save changes", 15, new Color(138, 43, 226), Color.WHITE);
        btnSave.setMaximumSize(new Dimension(300, 40));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(btnSave);

        if (student == null) {
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
        String studentIdText = txtStudentId.getText().trim();

        if (studentIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
            return;
        }

        // Degree is stored as a foreign key (degree_id) rapidly. The dropdown is
        // built directly from degreeList, so the selected index maps straight according to the
        // matching Degree's id - no free-text lookup are needed anymore.
        int resolvedDegreeId = 0;
        int selected = cmbDegree.getSelectedIndex();
        if (selected > 0 && selected - 1 < degreeList.size()) {
            resolvedDegreeId = degreeList.get(selected - 1).getId();
        }

        student.setFullName(txtFullName.getText().trim());
        student.setStudentIdStr(studentIdText);
        student.setDegreeId(resolvedDegreeId);
        student.setEmail(txtEmail.getText().trim());
        student.setMobileNumber(txtMobile.getText().trim());

        if (studentDAO.updateStudent(student)) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile. Student ID might already be taken.");
        }
    }

    private RoundedTextField addField(JPanel formPanel, GridBagConstraints gbc, String label, int row, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(138, 43, 226));

        RoundedTextField txt = new RoundedTextField(20, 15);
        txt.setText(value != null ? value : "");

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

    // Builds the Degree dropdown: "Select Degree" followed by every existing
    // Degree's name, with the student's current degree pre-selected if they have one.
    private JComboBox<String> addDegreeDropdown(JPanel formPanel, GridBagConstraints gbc, int row, int currentDegreeId) {
        JLabel lbl = new JLabel("Degree");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(138, 43, 226));

        String[] options = new String[degreeList.size() + 1];
        options[0] = "-- Select Degree --";
        int selectIndex = 0;
        for (int i = 0; i < degreeList.size(); i++) {
            options[i + 1] = degreeList.get(i).getName();
            if (degreeList.get(i).getId() == currentDegreeId) selectIndex = i + 1;
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
