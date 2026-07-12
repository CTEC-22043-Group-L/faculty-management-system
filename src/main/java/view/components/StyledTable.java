package view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class StyledTable extends JTable {
    
    public StyledTable() {
        super();
        setupStyles();
    }

    private void setupStyles() {
        setRowHeight(40);
        setShowGrid(true);
        setGridColor(new Color(138, 43, 226)); // Purple grid lines
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(new Color(50, 50, 50));
        setSelectionBackground(new Color(230, 210, 255));
        setSelectionForeground(Color.BLACK);
        
        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(100, 45));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(138, 43, 226)); // Purple header text
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);
        setDefaultRenderer(String.class, centerRenderer);
        setDefaultRenderer(Integer.class, centerRenderer);
        
        // Remove borders from header
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }
}
