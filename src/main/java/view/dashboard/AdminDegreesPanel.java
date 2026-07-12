package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import view.components.RoundedButton;
import view.components.FormDialog;
import model.dao.DegreeDAO;
import model.dao.DepartmentDAO;
import model.Degree;
import model.Department;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

public class AdminDegreesPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private DegreeDAO degreeDAO;
    private DepartmentDAO departmentDAO;
    private List<Degree> currentList;
    private RoundedButton btnEdit, btnDelete;

    public AdminDegreesPanel() {
        degreeDAO = new DegreeDAO();
        departmentDAO = new DepartmentDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Degrees");
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

        String[] columns = {"Degree", "Department", "No of Students"};
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
        currentList = degreeDAO.getAllDegrees();
        for (Degree d : currentList) {
            tableModel.addRow(new Object[]{
                    d.getName(), d.getDepartmentName(), d.getNoOfStudents()
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

    private void handleAdd() {
        List<Department> departments = getDepartments();
        Object[] result = FormDialog.showFormWithDropdown(this, "Add Degree",
                new String[]{"Name", "No of Students"}, null,
                "Department", departmentNames(departments), 0);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int deptIndex = (int) result[1];

        if (values[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Degree name is required.");
            return;
        }
        int noOfStudents;
        try {
            noOfStudents = values[1].isEmpty() ? 0 : Integer.parseInt(values[1]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "No of Students must be a number.");
            return;
        }

        Degree d = new Degree(0, values[0], departments.get(deptIndex).getId(), noOfStudents);
        if (degreeDAO.addDegree(d)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Degree added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add degree.");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Degree d = currentList.get(row);

        List<Department> departments = getDepartments();
        int currentDeptIndex = 0;
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == d.getDepartmentId()) { currentDeptIndex = i; break; }
        }

        Object[] result = FormDialog.showFormWithDropdown(this, "Edit Degree",
                new String[]{"Name", "No of Students"},
                new String[]{d.getName(), String.valueOf(d.getNoOfStudents())},
                "Department", departmentNames(departments), currentDeptIndex);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int deptIndex = (int) result[1];

        int noOfStudents;
        try {
            noOfStudents = values[1].isEmpty() ? 0 : Integer.parseInt(values[1]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "No of Students must be a number.");
            return;
        }

        d.setName(values[0]);
        d.setNoOfStudents(noOfStudents);
        d.setDepartmentId(departments.get(deptIndex).getId());

        if (degreeDAO.updateDegree(d)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Degree updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update degree.");
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Degree d = currentList.get(row);

        if (!FormDialog.confirmDelete(this, "\"" + d.getName() + "\"")) return;

        if (degreeDAO.deleteDegree(d.getId())) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to delete. It may still be referenced by a Student.");
        }
    }
}
