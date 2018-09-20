package com.raegon.pivio.path.impl;

import com.raegon.pivio.media.Media;
import com.raegon.pivio.path.Converter;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DirectoryConverter implements Converter {

    private Path target;

    private String pattern;

    private DateTimeZone sourceZone = DateTimeZone.getDefault();

    private DateTimeZone targetZone = DateTimeZone.getDefault();

    @Override
    public Map<Path, Path> convert(Map<Path, Path> map) {
        Map<Path, Path> result = new LinkedHashMap<>();
        map.forEach((source, target) -> {
            result.put(source, convert(source, target));
        });
        return result;
    }

    private Path convert(Path source, Path target) {
        DateTime dateTime = Media.get(source).getDateTime(sourceZone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withZone(targetZone);
        Path parent = target.resolve(fmt.print(dateTime));

        // Find current datetime directories with same prefix
        List<Path> directories = getExistDirectories(parent);
        if (directories.size() == 1) {
            return directories.get(0).resolve(source.getFileName());
        } else {
            return parent.resolve(source.getFileName());
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
