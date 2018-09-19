package com.raegon.pivio;

import com.raegon.pivio.media.Media;
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
public class Extractor {

    private Path source;

    private Map<Path, Path> extract() {
        Map<Path, Path> map = new LinkedHashMap<>();
        List<Path> sources = getSources();
        for (Path source : sources) {
            map.put(source, source);
        }
        return map;
    }

    private List<Path> getSources() {
        try {
            return Files.walk(source)
                    .filter(p -> !Files.isDirectory(p))
                    .filter(Media::isMedia)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
