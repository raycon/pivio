package com.raegon.pivio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Transporter {

    public static final String PREFIX_PATTERN = "yyyyMMdd";

    public static final String DIRECTORY_PATTERN = "yyyy-MM-dd";

    private static final DateTimeFormatter PREFIX_FMT = DateTimeFormat.forPattern(PREFIX_PATTERN);

    private static final DateTimeFormatter DIRECTORY_FMT =
            DateTimeFormat.forPattern(DIRECTORY_PATTERN);

    private static final PathMatcher PREFIX_MATCHER =
            FileSystems.getDefault().getPathMatcher("regex:\\d{8}.*");

    public static Map<Path, Path> analysis(Path source, Path target) throws IOException {
        Map<Path, Path> map = new HashMap<>();
        Files.walk(source)
                .filter(Files::isRegularFile)
                .filter(p -> PREFIX_MATCHER.matches(p.getFileName()))
                .forEach(left -> {
                    Path right = getTargetPath(target, left.getFileName().toString());
                    map.put(left, right);
                });
        return map;
    }

    public static Path getTargetPath(Path target, String filename) {
        DateTime date = getDateTime(filename);
        Path directory = getDateDirectory(target, date);
        List<Path> exists = getExistDateDirectories(target, directory);
        if (exists.size() == 1) {
            directory = exists.get(0);
        }
        return directory.resolve(filename);
    }

    public static DateTime getDateTime(String filename) {
        String prefix = filename.substring(0, PREFIX_PATTERN.length());
        return PREFIX_FMT.parseDateTime(prefix);
    }

    public static Path getDateDirectory(Path root, DateTime date) {
        Path year = root.resolve(String.valueOf(date.getYear()));
        Path directory = Paths.get(DIRECTORY_FMT.print(date));
        return year.resolve(directory);
    }

    public static List<Path> getExistDateDirectories(Path root, Path directory) {
        Path year = directory.getParent();
        try {
            return Files.list(year).filter(Files::isDirectory).filter(dir -> {
                return dir.toString().startsWith(directory.toString());
            }).collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<Path>();
        }
    }

}
