package org.fdatechanger.filedatechanger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MacFileDateChanger implements FileDateChanger {
    @Override
    public void changeDate(File file, LocalDateTime dateTime) {
        if (!file.exists()) throw new IllegalArgumentException(String.format("%s does not exist", file.getName()));
        if (!file.canRead()) throw new IllegalArgumentException(String.format("%s is not readable", file.getName()));

        /*
        -d date   # creation date
        -m date   # modification date
        setfile -d -m "MM/DD/YYYY HH:MM:SS" path/to/file

        Therefore, we need to use the following date format: MM/DD/YYYY HH:MM:SS
         */

        var formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        System.out.println(formattedDateTime);

        // Command to change 'last modification' and 'last accessed' timestamps for the original file
        var command = String.format("setfile -d \"%s\" -m \"%s\" %s", formattedDateTime, formattedDateTime, file.getAbsolutePath());
        executeCommand(command);
    }

    private void executeCommand(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Unable to change file date");
            throw new RuntimeException(e);
        }
    }
}
