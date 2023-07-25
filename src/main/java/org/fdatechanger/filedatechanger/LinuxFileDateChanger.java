package org.fdatechanger.filedatechanger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class LinuxFileDateChanger implements FileDateChanger {
    @Override
    public void changeDate(File file, LocalDateTime dateTime) {
        if (!file.exists()) throw new IllegalArgumentException(String.format("%s does not exist", file.getName()));
        if (!file.canRead()) throw new IllegalArgumentException(String.format("%s is not readable", file.getName()));

        // We need to use the YYYY-MM-DD HH:MM format for the 'touch' command
        var formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // Generate a random string to use as a temporary file name
        var uuid = UUID.randomUUID().toString().substring(0, 6);
        var tempPath = System.getProperty("java.io.tmpdir");
        var tempFile = new File(tempPath, uuid).toPath();

        // Create a temp file with the desired timestamp
        var tempFileCommand = String.format("/usr/bin/touch -t %s %s", formattedDateTime, tempFile);
        executeCommand(tempFileCommand);

        // Apply the timestamp from the temp file to the original file
        var touchWithReferenceCommand = String.format("/usr/bin/touch -r %s %s", tempFile, file.getAbsolutePath());
        executeCommand(touchWithReferenceCommand);

        // Delete the temporary file
        var removeTempFileCommand = String.format("/bin/rm %s", tempFile);
        executeCommand(removeTempFileCommand);

        // Command to change 'last modification' and 'last accessed' timestamps for the original file
        var command = String.format("/usr/bin/touch -t %s %s", formattedDateTime, file.getAbsolutePath());
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
