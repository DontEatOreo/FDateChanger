package org.fdatechanger.factory;

import exceptions.UnsupportedOSException;
import org.fdatechanger.filedatechanger.FileDateChanger;
import org.fdatechanger.filedatechanger.LinuxFileDateChanger;
import org.fdatechanger.filedatechanger.MacFileDateChanger;
import org.fdatechanger.filedatechanger.WindowsFileDateChanger;

public class FileDateChangerFactory implements IFileDateChangerFactory {
    @Override
    public FileDateChanger createForOS(String osName) {
        if (osName.contains("win")) return new WindowsFileDateChanger();
        else if (osName.contains("mac")) return new MacFileDateChanger();
        else if (osName.contains("linux")) return new LinuxFileDateChanger();
        else throw new UnsupportedOSException("Unsupported OS: " + osName);
    }
}
