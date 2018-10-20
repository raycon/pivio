package com.raegon.pivio.media;

import com.raegon.pivio.media.impl.Picture;
import com.raegon.pivio.media.impl.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.nio.file.Path;

@Data
@EqualsAndHashCode
public abstract class Media {

    private final Path path;

    protected Media(Path path) {
        this.path = path;
    }

    public static Media get(Path path) {
        String extension = FilenameUtils.getExtension(path.getFileName().toString());
        switch (extension) {
            case "mp4":
            case "MP4":
            case "mov":
            case "MOV":
                return new Video(path);
            case "jpg":
            case "JPG":
                return new Picture(path);
            default:
                throw new RuntimeException("Unsupport extensions");
        }
    }

    public static boolean isMedia(Path path) {
        try {
            Media.get(path);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    public abstract DateTime getDateTime(DateTimeZone zone);

}
