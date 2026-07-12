package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import view.components.RoundedButton;
import view.components.FormDialog;
import model.dao.DepartmentDAO;
import model.Department;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

public class AdminDepartmentsPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private DepartmentDAO departmentDAO;
    private List<Department> currentList; 
    private RoundedButton btnEdit, btnDelete;

    public AdminDepartmentsPanel() {
        departmentDAO = new DepartmentDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Departments");
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

        String[] columns = {"Name", "HOD", "Degree", "No of Staff"};
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
        currentList = departmentDAO.getAllDepartments();
        List<model.Degree> allDegrees = new model.dao.DegreeDAO().getAllDegrees();
        for (Department d : currentList) {
            StringBuilder degreeNames = new StringBuilder();
            for (model.Degree deg : allDegrees) {
                if (deg.getDepartmentId() == d.getId()) {
                    if (degreeNames.length() > 0) degreeNames.append(", ");
                    degreeNames.append(deg.getName());
                }
            }
            tableModel.addRow(new Object[]{
                    d.getName(), d.getHodName(), degreeNames.toString(), d.getNoOfStaff()
            });
        }
    }

    private void handleAdd() {
        String[] values = FormDialog.showForm(this, "Add Department",
                new String[]{"Name", "HOD Name", "No of Staff"}, null);
        if (values == null) return;

        if (values[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department name is required.");
            return;
        }
        int staff;
        try {
            staff = values[2].isEmpty() ? 0 : Integer.parseInt(values[2]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "No of Staff must be a number.");
            return;
        }

        Department d = new Department(0, values[0], values[1], staff);
        if (departmentDAO.addDepartment(d)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Department added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add department.");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Department d = currentList.get(row);

        String[] values = FormDialog.showForm(this, "Edit Department",
                new String[]{"Name", "HOD Name", "No of Staff"},
                new String[]{d.getName(), d.getHodName(), String.valueOf(d.getNoOfStaff())});
        if (values == null) return;

        int staff;
        try {
            staff = values[2].isEmpty() ? 0 : Integer.parseInt(values[2]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "No of Staff must be a number.");
            return;
        }

        d.setName(values[0]);
        d.setHodName(values[1]);
        d.setNoOfStaff(staff);

        if (departmentDAO.updateDepartment(d)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Department updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update department.");
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Department d = currentList.get(row);

        if (!FormDialog.confirmDelete(this, "\"" + d.getName() + "\"")) return;

        if (departmentDAO.deleteDepartment(d.getId())) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to delete. It may still be referenced by a Degree or Lecturer.");
        }
    }
}
