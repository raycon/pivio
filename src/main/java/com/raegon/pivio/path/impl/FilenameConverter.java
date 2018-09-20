package com.raegon.pivio.path.impl;

import com.raegon.pivio.media.Media;
import com.raegon.pivio.path.Converter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class FilenameConverter implements Converter {

    private String pattern;

    private String postfix;

    private DateTimeZone sourceZone;

    private DateTimeZone targetZone;

    @Override
    public Map<Path, Path> convert(Map<Path, Path> map) {
        Map<Path, Path> result = new HashMap<>();
        map.forEach((source, target) -> {
            int order = 0;
            Path path;
            do {
                path = convert(target, order++);
            } while (result.containsValue(path));
            result.put(source, path);
        });
        return result;
    }

    private Path convert(Path source, int order) {
        DateTime dateTime = Media.get(source).getDateTime(sourceZone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withZone(targetZone);
        String orderfix = (order > 0) ? "_" + order : "";
        String basename = fmt.print(dateTime) + orderfix + postfix;
        String extension = FilenameUtils.getExtension(source.toString());
        return source.getParent().resolve(basename + "." + extension);
    }

}
