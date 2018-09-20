package com.raegon.pivio.path;

import java.nio.file.Path;
import java.util.Map;

public interface Converter {

    Map<Path, Path> convert(Map<Path, Path> map);

}
