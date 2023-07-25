package org.fdatechanger.filedatechanger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WindowsFileDateChanger implements FileDateChanger {
    @Override
    public void changeDate(File file, LocalDateTime dateTime) {
        if (!file.exists()) throw new IllegalArgumentException(String.format("%s does not exist", file.getName()));
        if (!file.canRead()) throw new IllegalArgumentException(String.format("%s is not readable", file.getName()));

        var formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        var creationCommand = String.format("(Get-Item \"%s\").creationTime = \"%s\"",
                file.getAbsolutePath(),
                formattedDateTime);

        var modificationCommand = String.format("(Get-Item \"%s\").lastwritetime = \"%s\"",
                file.getAbsolutePath(),
                formattedDateTime);

        executeCommand(creationCommand);
        executeCommand(modificationCommand);
    }

    private void executeCommand(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[]{"powershell.exe", command});
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Unable to change file date");
            throw new RuntimeException(e);
        }
    }
}
