package view.dashboard;

import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import model.User;
import model.Lecturer;
import model.Course;
import model.dao.LecturerDAO;
import model.dao.CourseDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;

// Fixed the issue where lecturers could see the student's enrolled courses page
// This page shows the courses that the lecturer is teaching
public class LecturerCoursesPanel extends JPanel {

    public LecturerCoursesPanel(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Courses Teaching");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Course code", "Course name", "Credits"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        Lecturer lecturer = new LecturerDAO().getLecturerByUserId(currentUser.getId());
        if (lecturer != null) {
            List<Course> courses = new CourseDAO().getCoursesByLecturerId(lecturer.getId());
            for (Course c : courses) {
                tableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredits()});
            }
        }

        StyledTable table = new StyledTable();
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 1));
        add(scrollPane, BorderLayout.CENTER);

        if (lecturer == null) {
            JLabel notice = new JLabel("No profile found yet. Please contact Admin to link your account.", SwingConstants.CENTER);
            notice.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            notice.setForeground(Color.GRAY);
            add(notice, BorderLayout.SOUTH);
        }
    }
}
