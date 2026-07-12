package view.dashboard;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import view.components.StyledTable;

public class StudentTimetablePanel extends JPanel {
    
    public StudentTimetablePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Time table");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(138, 43, 226));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        Object[][] data = {
            {"08.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
            {"10.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
            {"INTERVAL", "Interval", "Interval", "Interval", "Interval", "Interval"}, // Simulated cell merge
            {"01.00", "SE", "OOP", "SE", "SE", "SE"},
            {"03.00", "SE", "OOP", "SE", "SE", "SE"}
        };
        
        StyledTable table = new StyledTable();
        table.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        // Custom renderer to color the Interval row purple
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (row == 2) { // Interval row
                    c.setBackground(new Color(138, 43, 226));
                    c.setForeground(Color.WHITE);
                    if (column == 0) {
                        ((JLabel)c).setText(""); // Hide the time column text for interval if you want it to look merged
                    } else if (column == 3) {
                        ((JLabel)c).setText("Interval");
                    } else {
                        ((JLabel)c).setText("");
                    }
                    setFont(new Font("Segoe UI", Font.BOLD, 16));
                } else {
                    c.setBackground(isSelected ? new Color(230, 210, 255) : Color.WHITE);
                    c.setForeground(new Color(50, 50, 50));
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(138, 43, 226), 1));
        add(scrollPane, BorderLayout.CENTER);
    }
}
