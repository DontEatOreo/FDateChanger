package org.fdatechanger;

import org.fdatechanger.factory.IFileDateChangerFactory;
import org.fdatechanger.filedatechanger.FileDateChanger;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class FileModifier {
    private final HashMap<File, LocalDateTime> files;
    private final IFileDateChangerFactory fileDateChangerFactory;

    public FileModifier(IFileDateChangerFactory fileDateChangerFactory) {
        this.fileDateChangerFactory = fileDateChangerFactory;
        this.files = new HashMap<>();
    }

    public void addFiles(File[] selectedFiles) {
        for (File file : selectedFiles) {
            files.put(file, null); // initially, set datetime to null
        }
    }

    public void updateFilesDateTime(LocalDate date, LocalTime time) {
        var localDateTime = LocalDateTime.of(date, time);
        files.replaceAll((file, dateTime) -> localDateTime);
    }

    public void changeFiles() {
        String osName = System.getProperty("os.name").toLowerCase();

        files.forEach((file, dateTime) -> {
            if (dateTime == null) {
                JOptionPane.showMessageDialog(null,
                        "Please select a date and time before changing files.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                FileDateChanger changer = fileDateChangerFactory.createForOS(osName);
                changer.changeDate(file, dateTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
