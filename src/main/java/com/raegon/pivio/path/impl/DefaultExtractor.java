package com.raegon.pivio.path.impl;

import com.raegon.pivio.media.Media;
import com.raegon.pivio.path.Extractor;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DefaultExtractor implements Extractor {

    private Path source;

    public Map<Path, Path> extract() {
        Map<Path, Path> map = new LinkedHashMap<>();
        List<Path> sources = getSources(source);
        for (Path source : sources) {
            map.put(source, source);
        }
        return map;
    }

    private List<Path> getSources(Path path) {
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
