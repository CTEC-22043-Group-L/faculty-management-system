package view.components;

import javax.swing.*;
import java.awt.*;

// A small reusable helper for building simple Add/Edit popup forms,
// used by all the Admin*Panel classes so each one doesn't have to
// hand-build its own JOptionPane form layout.
public class FormDialog {

    // Shows a form with only plain text fields.
    // Returns the entered values in the same order as `labels`, or null if the user cancelled.
    public static String[] showForm(Component parent, String title, String[] labels, String[] initialValues) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            fields[i] = new JTextField(initialValues != null && initialValues[i] != null ? initialValues[i] : "");
            panel.add(fields[i]);
        }
        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        String[] values = new String[labels.length];
        for (int i = 0; i < labels.length; i++) values[i] = fields[i].getText().trim();
        return values;
    }

    // Shows a form with text fields PLUS one dropdown (e.g. picking a Department by name).
    // Returns Object[]{ String[] textValues, Integer selectedDropdownIndex }, or null if cancelled.
    public static Object[] showFormWithDropdown(Component parent, String title,
                                                 String[] textLabels, String[] initialTextValues,
                                                 String dropdownLabel, String[] dropdownOptions, int selectedIndex) {
        if (dropdownOptions.length == 0) {
            JOptionPane.showMessageDialog(parent,
                    "No " + dropdownLabel + " options exist yet. Please add one first.",
                    "Nothing to select", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        JPanel panel = new JPanel(new GridLayout(textLabels.length + 1, 2, 10, 10));
        JTextField[] fields = new JTextField[textLabels.length];
        for (int i = 0; i < textLabels.length; i++) {
            panel.add(new JLabel(textLabels[i]));
            fields[i] = new JTextField(initialTextValues != null && initialTextValues[i] != null ? initialTextValues[i] : "");
            panel.add(fields[i]);
        }
        panel.add(new JLabel(dropdownLabel));
        JComboBox<String> combo = new JComboBox<>(dropdownOptions);
        if (selectedIndex >= 0 && selectedIndex < dropdownOptions.length) combo.setSelectedIndex(selectedIndex);
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        String[] values = new String[textLabels.length];
        for (int i = 0; i < textLabels.length; i++) values[i] = fields[i].getText().trim();
        return new Object[]{values, combo.getSelectedIndex()};
    }

    // Shows a form with text fields PLUS two dropdowns: one for a structural field
    // (Department/Degree) and one for optionally linking an existing login account.
    // Returns Object[]{ String[] textValues, Integer selectedFirstDropdownIndex, Integer selectedSecondDropdownIndex },
    // or null if cancelled.
    public static Object[] showFormWithTwoDropdowns(Component parent, String title,
                                                     String[] textLabels, String[] initialTextValues,
                                                     String firstDropdownLabel, String[] firstDropdownOptions, int firstSelectedIndex,
                                                     String secondDropdownLabel, String[] secondDropdownOptions, int secondSelectedIndex) {
        if (firstDropdownOptions.length == 0) {
            JOptionPane.showMessageDialog(parent,
                    "No " + firstDropdownLabel + " options exist yet. Please add one first.",
                    "Nothing to select", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        JPanel panel = new JPanel(new GridLayout(textLabels.length + 2, 2, 10, 10));
        JTextField[] fields = new JTextField[textLabels.length];
        for (int i = 0; i < textLabels.length; i++) {
            panel.add(new JLabel(textLabels[i]));
            fields[i] = new JTextField(initialTextValues != null && initialTextValues[i] != null ? initialTextValues[i] : "");
            panel.add(fields[i]);
        }

        panel.add(new JLabel(firstDropdownLabel));
        JComboBox<String> combo1 = new JComboBox<>(firstDropdownOptions);
        if (firstSelectedIndex >= 0 && firstSelectedIndex < firstDropdownOptions.length) combo1.setSelectedIndex(firstSelectedIndex);
        panel.add(combo1);

        panel.add(new JLabel(secondDropdownLabel));
        JComboBox<String> combo2 = new JComboBox<>(secondDropdownOptions);
        if (secondSelectedIndex >= 0 && secondSelectedIndex < secondDropdownOptions.length) combo2.setSelectedIndex(secondSelectedIndex);
        panel.add(combo2);

        int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        String[] values = new String[textLabels.length];
        for (int i = 0; i < textLabels.length; i++) values[i] = fields[i].getText().trim();
        return new Object[]{values, combo1.getSelectedIndex(), combo2.getSelectedIndex()};
    }

    public static boolean confirmDelete(Component parent, String itemDescription) {
        int result = JOptionPane.showConfirmDialog(parent,
                "Are you sure you want to delete " + itemDescription + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
