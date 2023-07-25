package org.fdatechanger.filedatechanger;

import java.io.File;
import java.time.LocalDateTime;

public interface FileDateChanger {
    void changeDate(File file, LocalDateTime dateTime) throws Exception;
}
