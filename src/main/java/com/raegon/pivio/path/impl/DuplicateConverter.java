package com.raegon.pivio.path.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.raegon.pivio.path.Converter;

import lombok.Data;

@Data
public class DuplicateConverter implements Converter {

    private Path duplicateDirectory;

    @Override
    public Map<Path, Path> convert(Map<Path, Path> map) {
        Map<Path, Path> result = new LinkedHashMap<>();
        map.forEach((source, target) -> {
            result.put(source, convert(target));
        });
        return result;
    }

    private Path convert(Path path) {
        // TODO: Compare size and hash
        if (Files.exists(path)) {
            return duplicateDirectory.resolve(path.getFileName());
        } else {
            return path;
        }
    }

}
