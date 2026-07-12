package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import view.components.RoundedButton;
import view.components.FormDialog;
import model.dao.CourseDAO;
import model.dao.LecturerDAO;
import model.Course;
import model.Lecturer;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;

public class AdminCoursesPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO;
    private LecturerDAO lecturerDAO;
    private List<Course> currentList;
    private RoundedButton btnEdit, btnDelete;

    public AdminCoursesPanel() {
        courseDAO = new CourseDAO();
        lecturerDAO = new LecturerDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Courses");
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

        String[] columns = {"Course code", "Course name", "Credits", "Lecturer"};
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
        currentList = courseDAO.getAllCourses();
        for (Course c : currentList) {
            tableModel.addRow(new Object[]{
                    c.getCourseCode(), c.getCourseName(), c.getCredits(), c.getLecturerName()
            });
        }
    }

    private List<Lecturer> getLecturers() {
        return lecturerDAO.getAllLecturers();
    }

    private String[] lecturerNames(List<Lecturer> lecturers) {
        String[] names = new String[lecturers.size()];
        for (int i = 0; i < lecturers.size(); i++) names[i] = lecturers.get(i).getFullName();
        return names;
    }

    private void handleAdd() {
        List<Lecturer> lecturers = getLecturers();
        Object[] result = FormDialog.showFormWithDropdown(this, "Add Course",
                new String[]{"Course Code", "Course Name", "Credits"}, null,
                "Lecturer", lecturerNames(lecturers), 0);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int lecIndex = (int) result[1];

        if (values[0].isEmpty() || values[1].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course code and name are required.");
            return;
        }
        int credits;
        try {
            credits = values[2].isEmpty() ? 0 : Integer.parseInt(values[2]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a number.");
            return;
        }

        Course c = new Course(0, values[0], values[1], credits, lecturers.get(lecIndex).getId());
        if (courseDAO.addCourse(c)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Course added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course. Course code might already exist.");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Course c = currentList.get(row);

        List<Lecturer> lecturers = getLecturers();
        int currentLecIndex = 0;
        for (int i = 0; i < lecturers.size(); i++) {
            if (lecturers.get(i).getId() == c.getLecturerId()) { currentLecIndex = i; break; }
        }

        Object[] result = FormDialog.showFormWithDropdown(this, "Edit Course",
                new String[]{"Course Code", "Course Name", "Credits"},
                new String[]{c.getCourseCode(), c.getCourseName(), String.valueOf(c.getCredits())},
                "Lecturer", lecturerNames(lecturers), currentLecIndex);
        if (result == null) return;

        String[] values = (String[]) result[0];
        int lecIndex = (int) result[1];

        int credits;
        try {
            credits = values[2].isEmpty() ? 0 : Integer.parseInt(values[2]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a number.");
            return;
        }

        c.setCourseCode(values[0]);
        c.setCourseName(values[1]);
        c.setCredits(credits);
        c.setLecturerId(lecturers.get(lecIndex).getId());

        if (courseDAO.updateCourse(c)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Course updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update course.");
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Course c = currentList.get(row);

        if (!FormDialog.confirmDelete(this, "\"" + c.getCourseCode() + "\"")) return;

        if (courseDAO.deleteCourse(c.getId())) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete course.");
        }
    }
}
