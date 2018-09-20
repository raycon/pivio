package com.raegon.pivio.path.impl;

import com.raegon.pivio.path.Resolver;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class DefaultResolver implements Resolver {

    private Path duplicateDirectory;

    private Path conflictDirectory;

    @Override
    public Path resolve(Path source, Path target) {
        try {
            if (Files.exists(target)) {
                if (isSame(source, target)) {
                    return duplicateDirectory.resolve(source.getFileName());
                }
                return conflictDirectory.resolve(source.getFileName());
            }
            return target;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isSame(Path source, Path target) throws IOException {
        return Files.size(source) == Files.size(target);
    }

}
