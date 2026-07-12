package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int radius;
    private Color hoverBackgroundColor;
    private Color normalBackgroundColor;

    public RoundedButton(String text, int radius, Color bgColor, Color fgColor) {
        super(text);
        this.radius = radius;
        this.normalBackgroundColor = bgColor;
        this.hoverBackgroundColor = bgColor.brighter();
        
        setForeground(fgColor);
        setBackground(bgColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackgroundColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalBackgroundColor);
            }
        });
    }

    // Updates the button's "resting" color (used when the mouse is not hovering).
    // Needed so external code (e.g. RoleTogglePanel) can change which state
    // a toggle button rests in, instead of it always snapping back to the
    // color it was originally constructed with.
    public void setRestingBackground(Color bgColor) {
        this.normalBackgroundColor = bgColor;
        this.hoverBackgroundColor = bgColor.brighter();
        setBackground(bgColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        
        super.paintComponent(g);
        g2.dispose();
    }
}
