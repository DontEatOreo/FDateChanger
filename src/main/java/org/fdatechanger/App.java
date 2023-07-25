package org.fdatechanger;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import org.fdatechanger.factory.FileDateChangerFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    private JButton fileChooserButton;
    private JPanel datePickerPanel;
    private JPanel timePickerPanel;
    private JPanel mainPanel;
    private JButton changeDateButton;
    private final DatePicker datePicker = new DatePicker();
    private final TimePicker timePicker = new TimePicker();
    private final FileModifier fileModifier;

    public App() {
        this.fileModifier = new FileModifier(new FileDateChangerFactory());
        fileChooserButton.addActionListener(e -> pickFiles());

        changeDateButton.addActionListener(e -> {
            updateFilesDateTime();
            fileModifier.changeFiles();
        });

        datePickerPanel.setLayout(new BorderLayout());
        datePickerPanel.add(datePicker);

        timePickerPanel.setLayout(new BorderLayout());
        timePickerPanel.add(timePicker);
    }

    public void pickFiles() {
        var fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true); // Allow multiple file selection

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) return;

        var selectedFiles = fileChooser.getSelectedFiles(); // Get multiple selected files
        fileModifier.addFiles(selectedFiles);
    }

    public void updateFilesDateTime() {
        LocalDate date = datePicker.getDate();
        LocalTime time = timePicker.getTime();
        if(date == null || time == null) {
            JOptionPane.showMessageDialog(null,
                    "Please select a date and time before changing files.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        fileModifier.updateFilesDateTime(date, time);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Unable to set LookAndFeel");
            }
            JFrame frame = new JFrame("File Date Changer");
            App app = new App();
            frame.setContentPane(app.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
