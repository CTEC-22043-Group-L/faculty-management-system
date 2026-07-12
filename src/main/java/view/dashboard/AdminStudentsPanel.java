package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import view.components.RoundedButton;
import view.components.FormDialog;
import model.dao.StudentDAO;
import model.dao.DegreeDAO;
import model.dao.UserDAO;
import model.Student;
import model.Degree;
import model.User;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

public class AdminStudentsPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private DegreeDAO degreeDAO;
    private UserDAO userDAO;
    private List<Student> currentList;
    private RoundedButton btnEdit, btnDelete;

    public AdminStudentsPanel() {
        studentDAO = new StudentDAO();
        degreeDAO = new DegreeDAO();
        userDAO = new UserDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Students");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        RoundedButton btnAdd = new RoundedButton("Add new", 15, new Color(138, 43, 226), Color.WHITE);
        btnEdit = new RoundedButton("Edit", 15, Color.LIGHT_GRAY, Color.DARK_GRAY);
        btnDelete = new RoundedButton("Delete", 15, Color.LIGHT_GRAY, Color.DARK_GRAY);

        btnAdd.setPreferredSize(new Dimension(120, 35));
        btnEdit.setPreferredSize(new Dimension(120, 35));
        btnDelete.setPreferredSize(new Dimension(120, 35));
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        topPanel.add(buttonPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Full Name", "Student ID", "Degree", "Email", "Mobile Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable();
        table.setModel(tableModel);

        table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            boolean hasSelection = table.getSelectedRow() != -1;
            btnEdit.setEnabled(hasSelection);
            btnDelete.setEnabled(hasSelection);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 1));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        RoundedButton btnRefresh = new RoundedButton("Refresh", 15, new Color(138, 43, 226), Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(300, 40));
        btnRefresh.addActionListener(e -> loadData());
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> handleAdd());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        currentList = studentDAO.getAllStudents();
        for (Student s : currentList) {
            tableModel.addRow(new Object[]{
                    s.getFullName(), s.getStudentIdStr(), s.getDegreeName(), s.getEmail(), s.getMobileNumber()
            });
        }
    }

    private List<Degree> getDegrees() {
        return degreeDAO.getAllDegrees();
    }

    private String[] degreeNames(List<Degree> degrees) {
        String[] names = new String[degrees.size()];
        for (int i = 0; i < degrees.size(); i++) names[i] = degrees.get(i).getName();
        return names;
    }

    // Builds the "Linked Login Account" dropdown options: a "-- No login account --"
    // placeholder followed by usernames of Student-role accounts not yet linked to a
    // profile (plus the currently-linked account, if editing one).
    private List<User> unlinkedUsersCache;
    private String[] loginAccountOptions(int includeUserId) {
        unlinkedUsersCache = userDAO.getUnlinkedUsersByRole("Student", "students", includeUserId);
        String[] options = new String[unlinkedUsersCache.size() + 1];
        options[0] = "-- No login account --";
        for (int i = 0; i < unlinkedUsersCache.size(); i++) options[i + 1] = unlinkedUsersCache.get(i).getUsername();
        return options;
    }

    // selectedIndex is the index chosen in the dropdown built by loginAccountOptions().
    // 0 means "no account"; anything else maps back into unlinkedUsersCache.
    private int resolveSelectedUserId(int selectedIndex) {
        if (selectedIndex <= 0) return 0;
        return unlinkedUsersCache.get(selectedIndex - 1).getId();
    }

    private void handleAdd() {
        List<Degree> degrees = getDegrees();
        String[] loginOptions = loginAccountOptions(-1);

        Object[] result = FormDialog.showFormWithTwoDropdowns(this, "Add Student",
                new String[]{"Full Name", "Student ID", "Email", "Mobile Number"}, null,
                "Degree", degreeNames(degrees), 0,
                "Linked Login Account", loginOptions, 0);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int degIndex = (int) result[1];
        int loginIndex = (int) result[2];

        if (values[0].isEmpty() || values[1].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name and Student ID are required.");
            return;
        }

        // If Admin picked an existing login account here, the Student row is linked
        // immediately (user_id set) - this covers the case where the student already
        // signed up before Admin got around to adding their full profile.
        int userId = resolveSelectedUserId(loginIndex);
        Student s = new Student(0, userId, values[1], values[0], degrees.get(degIndex).getId(), values[2], values[3]);
        if (studentDAO.addStudent(s)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Student ID might already exist.");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Student s = currentList.get(row);

        List<Degree> degrees = getDegrees();
        int currentDegIndex = 0;
        for (int i = 0; i < degrees.size(); i++) {
            if (degrees.get(i).getId() == s.getDegreeId()) { currentDegIndex = i; break; }
        }

        String[] loginOptions = loginAccountOptions(s.getUserId());
        int currentLoginIndex = 0;
        for (int i = 0; i < unlinkedUsersCache.size(); i++) {
            if (unlinkedUsersCache.get(i).getId() == s.getUserId()) { currentLoginIndex = i + 1; break; }
        }

        Object[] result = FormDialog.showFormWithTwoDropdowns(this, "Edit Student",
                new String[]{"Full Name", "Student ID", "Email", "Mobile Number"},
                new String[]{s.getFullName(), s.getStudentIdStr(), s.getEmail(), s.getMobileNumber()},
                "Degree", degreeNames(degrees), currentDegIndex,
                "Linked Login Account", loginOptions, currentLoginIndex);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int degIndex = (int) result[1];
        int loginIndex = (int) result[2];

        s.setFullName(values[0]);
        s.setStudentIdStr(values[1]);
        s.setEmail(values[2]);
        s.setMobileNumber(values[3]);
        s.setDegreeId(degrees.get(degIndex).getId());
        s.setUserId(resolveSelectedUserId(loginIndex));

        if (studentDAO.updateStudent(s)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student.");
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Student s = currentList.get(row);

        if (!FormDialog.confirmDelete(this, "\"" + s.getFullName() + "\"")) return;

        if (studentDAO.deleteStudent(s.getId())) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete student.");
        }
    }
}
