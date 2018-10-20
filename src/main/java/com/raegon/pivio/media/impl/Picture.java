package com.raegon.pivio.media.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.raegon.pivio.media.Media;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.nio.file.Path;

public class Picture extends Media {

    public Picture(Path path) {
        super(path);
    }

    @Override
    public DateTime getDateTime(DateTimeZone zone) {
        Metadata meta = getMetadata();
        ExifSubIFDDirectory exifDir = meta.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        return new DateTime(exifDir.getDateOriginal().getTime(), zone);
    }

    private Metadata getMetadata() {
        try {
            return ImageMetadataReader.readMetadata(getPath().toFile());
        } catch (ImageProcessingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
