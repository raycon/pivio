package com.raegon.pivio.media.impl;

import com.raegon.pivio.media.Media;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Video extends Media {

    public Video(Path path) {
        super(path);
    }

    @Override
    public DateTime getDateTime(DateTimeZone zone) {
        try {
            return new DateTime(Files.getLastModifiedTime(getPath()).toMillis(), zone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}