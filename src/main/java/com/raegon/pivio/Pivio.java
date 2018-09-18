package com.raegon.pivio;

import com.raegon.pivio.media.Media;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Pivio {

    private Path source;

    private Path target;

    private String postfix;

    private String dateFormat;

    private DateTimeZone sourceZone = DateTimeZone.getDefault();

    private DateTimeZone targetZone = DateTimeZone.getDefault();

    public void preview() {
        log.info("preview");
    }

    public void execute() {
        log.info("execute");
    }

    public Map<Path, Path> convert(List<Path> sources) {
        Map<Path, Path> map = new HashMap<>();
        for (Path source : sources) {
            map.put(source, convert(source));
        }
        return map;
    }

    private Map<Path, Path> resolve(Map<Path, Path> map) {
        Map<Path, List<Path>> group = map.entrySet().stream().collect(
                Collectors.groupingBy(
                        Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));

        return map;
    }

    public Path convert(Path path) {
        Media media = Media.get(path);
        DateTime dateTime = media.getDateTime(sourceZone);
        Path parent = getParent(dateTime);
        String basename = getBasename(dateTime);
        if (postfix != null) {
            basename = basename + "_" + postfix;
        }
        String extension = FilenameUtils.getExtension(path.toString());
        return parent.resolve(basename + "." + extension);
    }

    public Path getParent(DateTime dateTime) {
        String pattern = "yyyy" + File.pathSeparator + "MM";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        String directory = formatter.print(dateTime);
        return target.resolve(directory);
    }

    public String getBasename(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat).withZone(targetZone);
        return formatter.print(dateTime);
    }

}
