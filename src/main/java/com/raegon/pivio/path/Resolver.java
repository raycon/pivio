package com.raegon.pivio.path;

import java.nio.file.Path;

public interface Resolver {

    Path resolve(Path source, Path target);

}
