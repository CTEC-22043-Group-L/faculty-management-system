package view.dashboard;
import javax.swing.*;
import java.awt.*;
import view.components.StyledTable;
import model.User;
import model.Student;
import model.Enrollment;
import model.dao.StudentDAO;
import model.dao.EnrollmentDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class StudentCoursesPanel extends JPanel {

    public StudentCoursesPanel(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Courses Enrolled");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Course code", "Course name", "Credits", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        Student student = new StudentDAO().getStudentByUserId(currentUser.getId());
        if (student != null) {
            List<Enrollment> enrollments = new EnrollmentDAO().getEnrollmentsByStudentId(student.getId());
            for (Enrollment en : enrollments) {
                tableModel.addRow(new Object[]{
                        en.getCourseCode(), en.getCourseName(), en.getCredits(),
                        en.getGrade() != null ? en.getGrade() : "-"
                });
            }
        }

        StyledTable table = new StyledTable();
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 1));
        add(scrollPane, BorderLayout.CENTER);

        if (student == null) {
            JLabel notice = new JLabel("No profile found yet. Please contact Admin to link your account.", SwingConstants.CENTER);
            notice.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            notice.setForeground(Color.GRAY);
            add(notice, BorderLayout.SOUTH);
        }
    }
}
