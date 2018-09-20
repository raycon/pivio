package com.raegon.pivio.path.impl;

import com.raegon.pivio.path.Converter;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ExistFileConverter implements Converter {

    private Path duplicateDirectory;

    private Path conflictDirectory;

    @Override
    public Map<Path, Path> convert(Map<Path, Path> map) {
        Map<Path, Path> result = new LinkedHashMap<>();
        map.forEach((source, target) -> {
            Path path = convert(source, target);
            if (path != null) {
                result.put(source, path);
            }
        });
        return result;
    }

    public Path convert(Path source, Path target) {
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

    public boolean isSame(Path source, Path target) throws IOException {
        return Files.size(source) == Files.size(target);
    }
}
