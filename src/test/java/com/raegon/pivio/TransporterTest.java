package com.raegon.pivio;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

public class TransporterTest {

    private static final Path SOURCE = Paths.get("pivio", "source");
    private static final Path TARGET = Paths.get("pivio", "target");

    @BeforeClass
    public static void setup() throws IOException {
        delete(Paths.get("pivio"));
        Files.createDirectories(SOURCE);
        Files.createDirectories(TARGET);
    }

    @Test
    public void analysis() throws IOException {
        // given
        Path image1 = SOURCE.resolve("inbox").resolve("20180101_010101.jpg");
        Path image2 = SOURCE.resolve("inbox").resolve("phone").resolve("20180102_010101.jpg");
        Path image3 = SOURCE.resolve("inbox").resolve("2018-01-01_010102.jpg");
        Files.createDirectories(image2.getParent());
        Files.createFile(image1);
        Files.createFile(image2);
        Files.createFile(image3);

        Path exist = TARGET.resolve("2018").resolve("2018-01-01 subject");
        Files.createDirectories(exist);
        // when
        Map<Path, Path> map = Transporter.analysis(SOURCE, TARGET);
        // then
        assertThat(map.size()).isEqualTo(2);
        System.out.println(map);
    }

    @Test
    public void getTargetPath() throws IOException {
        // given
        String filename = "20180101_010101.jpg";
        Path exist = TARGET.resolve("2018").resolve("2018-01-01 subject");
        Files.createDirectories(exist);
        // when
        Path target = Transporter.getTargetPath(TARGET, filename);
        // then
        assertThat(target.toString()).isEqualTo(exist.resolve(filename).toString());
    }

    @Test
    public void getDateTime() {
        // when
        DateTime dt = Transporter.getDateTime("20180102_010101.jpg");
        // then
        assertThat(dt.getYear()).isEqualTo(2018);
        assertThat(dt.getMonthOfYear()).isEqualTo(01);
        assertThat(dt.getDayOfMonth()).isEqualTo(02);
    }

    @Test
    public void getDateDirectory() throws IOException {
        // given
        Path expect = TARGET.resolve("2018").resolve("2018-01-02");
        DateTime dt = new DateTime(2018, 1, 2, 0, 0);
        // when
        Path path = Transporter.getDateDirectory(TARGET, dt);
        // then
        assertThat(path.toString()).isEqualTo(expect.toString());
    }

    @Test
    public void getExistDateDirectoriesSuccess() throws IOException {
        // given
        Path exist = TARGET.resolve("2018").resolve("2018-01-01 subject");
        Files.createDirectories(exist);
        // when
        List<Path> paths = Transporter.getExistDateDirectories(TARGET,
                TARGET.resolve("2018").resolve("2018-01-01"));
        // then
        assertThat(paths.size()).isEqualTo(1);
        assertThat(paths.get(0).toString()).isEqualTo(exist.toString());
    }

    @Test
    public void getExistDateDirectoriesFailed() {
        // when
        List<Path> paths = Transporter.getExistDateDirectories(TARGET,
                TARGET.resolve("2018").resolve("2018-01-02"));
        // then
        assertThat(paths.size()).isEqualTo(0);
    }

    public static void delete(Path path) throws IOException {
        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }

}
