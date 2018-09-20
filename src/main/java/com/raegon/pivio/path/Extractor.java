package com.raegon.pivio.path;

import java.nio.file.Path;
import java.util.Map;

public interface Extractor {

    Map<Path, Path> extract(Path path);

}
