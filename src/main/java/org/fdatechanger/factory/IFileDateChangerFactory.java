package org.fdatechanger.factory;

import org.fdatechanger.filedatechanger.FileDateChanger;

public interface IFileDateChangerFactory {
    FileDateChanger createForOS(String osName);
}
