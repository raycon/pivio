package com.raegon.pivio;

import com.raegon.pivio.media.Media;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Converter {

    private Path sourceDirectory;

    private Path targetDirectory;

    private String postfix;

    private String directoryPattern;

    private String filenamePattern;

    private DateTimeZone sourceZone = DateTimeZone.getDefault();

    private DateTimeZone targetZone = DateTimeZone.getDefault();

    Map<Path, Path> getMap() {
        Map<Path, Path> map = new HashMap<>();
        List<Path> sources = getSources();
        for (Path source : sources) {
            int order = 0;
            Path target;
            do {
                target = convert(source, order++);
            } while(map.containsValue(target));
            map.put(source, target);
        }
        return map;
    }

    private List<Path> getSources() {
        try {
            return Files.walk(sourceDirectory)
                    .filter(p -> !Files.isDirectory(p))
                    .filter(Media::isMedia)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Path convert(Path source, int order) {
        // Date
        DateTime dateTime = Media.get(source).getDateTime(sourceZone);
        // Directory
        String directory = getDirectory(dateTime);
        // Filename
        String basename = getBasename(dateTime);
        if (order > 0) {
            basename += "_" + order;
        }
        basename += postfix;
        // Extension
        String extension = FilenameUtils.getExtension(source.toString());
        // Resolve
        return targetDirectory.resolve(directory).resolve(basename + "." + extension);
    }

    private String getDirectory(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(directoryPattern).withZone(targetZone);
        return formatter.print(dateTime);
    }

    private String getBasename(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(filenamePattern).withZone(targetZone);
        return formatter.print(dateTime);
    }

}
