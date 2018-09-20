package com.raegon.pivio.path.impl;

import com.raegon.pivio.path.Converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExistDirectoryConverter implements Converter {

    @Override
    public Map<Path, Path> convert(Map<Path, Path> map) {
        Map<Path, Path> result = new LinkedHashMap<>();
        map.forEach((source, target) -> result.put(source, convert(target)));
        return result;
    }

    private Path convert(Path target) {
        List<Path> directories = getExistDirectories(target.getParent());
        if (directories.size() == 1) {
            return directories.get(0).resolve(target.getFileName());
        } else {
            return target;
        }
    }

    private List<Path> getExistDirectories(Path directory) {
        Path parent = directory.getParent();
        try {
            return Files.list(parent)
                    .filter(Files::isDirectory)
                    .filter(dir -> dir.toString().startsWith(directory.toString()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
