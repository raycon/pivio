package com.raegon.pivio.path;

import java.nio.file.Path;
import java.util.Map;

public interface Transporter {

    void transfer(Map<Path, Path> map);

}
