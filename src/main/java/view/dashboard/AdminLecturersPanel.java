package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import view.components.RoundedButton;
import view.components.FormDialog;
import model.dao.LecturerDAO;
import model.dao.DepartmentDAO;
import model.dao.UserDAO;
import model.Lecturer;
import model.Department;
import model.User;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

public class AdminLecturersPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private LecturerDAO lecturerDAO;
    private DepartmentDAO departmentDAO;
    private UserDAO userDAO;
    private List<Lecturer> currentList;
    private RoundedButton btnEdit, btnDelete;

    public AdminLecturersPanel() {
        lecturerDAO = new LecturerDAO();
        departmentDAO = new DepartmentDAO();
        userDAO = new UserDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Lecturers");
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

        String[] columns = {"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"};
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
        currentList = lecturerDAO.getAllLecturers();
        for (Lecturer l : currentList) {
            tableModel.addRow(new Object[]{
                    l.getFullName(), l.getDepartmentName(), l.getCoursesTeaching(), l.getEmail(), l.getMobileNumber()
            });
        }
    }

    private List<Department> getDepartments() {
        return departmentDAO.getAllDepartments();
    }

    private String[] departmentNames(List<Department> departments) {
        String[] names = new String[departments.size()];
        for (int i = 0; i < departments.size(); i++) names[i] = departments.get(i).getName();
        return names;
    }

    // Builds the Linked Login Account"
    // placeholder followed by usernames of Lecturer-role accounts
    private List<User> unlinkedUsersCache;
    private String[] loginAccountOptions(int includeUserId) {
        unlinkedUsersCache = userDAO.getUnlinkedUsersByRole("Lecturer", "lecturers", includeUserId);
        String[] options = new String[unlinkedUsersCache.size() + 1];
        options[0] = "-- No login account --";
        for (int i = 0; i < unlinkedUsersCache.size(); i++) options[i + 1] = unlinkedUsersCache.get(i).getUsername();
        return options;
    }

    // selectedIndex is the index chosen in the dropdown
    // 0 means no account
    private int resolveSelectedUserId(int selectedIndex) {
        if (selectedIndex <= 0) return 0;
        return unlinkedUsersCache.get(selectedIndex - 1).getId();
    }

    private void handleAdd() {
        List<Department> departments = getDepartments();
        String[] loginOptions = loginAccountOptions(-1);

        Object[] result = FormDialog.showFormWithTwoDropdowns(this, "Add Lecturer",
                new String[]{"Full Name", "Email", "Mobile Number"}, null,
                "Department", departmentNames(departments), 0,
                "Linked Login Account", loginOptions, 0);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int deptIndex = (int) result[1];
        int loginIndex = (int) result[2];

        if (values[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name is required.");
            return;
        }

        //  Admin picked an existing login account here the Lecturer row is linked
        // signed up before Admin got around to adding their full profile.
        int userId = resolveSelectedUserId(loginIndex);
        Lecturer l = new Lecturer(0, userId, values[0], departments.get(deptIndex).getId(), values[1], values[2]);
        if (lecturerDAO.addLecturer(l)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Lecturer added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add lecturer.");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Lecturer l = currentList.get(row);

        List<Department> departments = getDepartments();
        int currentDeptIndex = 0;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == l.getDepartmentId()) { currentDeptIndex = i; break; }
        }

        String[] loginOptions = loginAccountOptions(l.getUserId());
        int currentLoginIndex = 0;
        for (int i = 0; i < unlinkedUsersCache.size(); i++) {
            if (unlinkedUsersCache.get(i).getId() == l.getUserId()) { currentLoginIndex = i + 1; break; }
        }

        Object[] result = FormDialog.showFormWithTwoDropdowns(this, "Edit Lecturer",
                new String[]{"Full Name", "Email", "Mobile Number"},
                new String[]{l.getFullName(), l.getEmail(), l.getMobileNumber()},
                "Department", departmentNames(departments), currentDeptIndex,
                "Linked Login Account", loginOptions, currentLoginIndex);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int deptIndex = (int) result[1];
        int loginIndex = (int) result[2];

        l.setFullName(values[0]);
        l.setEmail(values[1]);
        l.setMobileNumber(values[2]);
        l.setDepartmentId(departments.get(deptIndex).getId());
        l.setUserId(resolveSelectedUserId(loginIndex));

        if (lecturerDAO.updateLecturer(l)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Lecturer updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update lecturer.");
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Lecturer l = currentList.get(row);

        if (!FormDialog.confirmDelete(this, "\"" + l.getFullName() + "\"")) return;

        if (lecturerDAO.deleteLecturer(l.getId())) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to delete. It may still be referenced by a Course.");
        }
    }
}