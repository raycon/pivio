package com.raegon.pivio.path.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.raegon.pivio.media.Media;
import com.raegon.pivio.path.Extractor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DefaultExtractor implements Extractor {

    private Path sourceDirectory;

    public Map<Path, Path> extract() {
        log.info("Extract {}", sourceDirectory);
        Map<Path, Path> map = new LinkedHashMap<>();
        List<Path> paths = getPaths(sourceDirectory);
        for (Path path : paths) {
            map.put(path, path);
        }
        return map;
    }

    private List<Path> getPaths(Path path) {
        try {
            return Files.walk(path)
                    .filter(p -> !Files.isDirectory(p))
                    .filter(Media::isMedia)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
