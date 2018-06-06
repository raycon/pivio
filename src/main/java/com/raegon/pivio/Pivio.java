package com.raegon.pivio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Pivio {

	TimeZone timeZone;

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void rename(String directory) throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
			paths.filter(Files::isRegularFile).limit(10).forEach(p -> {
				try {
					renameFile(p);
				} catch (ImageProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public Path renameFile(Path path) throws ImageProcessingException, IOException {
		String ext = FilenameUtils.getExtension(path.getFileName().toString());
		if ("mp4".equalsIgnoreCase(ext)) {
			return renameVideo(path);
		} else if ("jpg".equalsIgnoreCase(ext)) {
			return renamePhoto(path);
		} else {
			
		}

		return null;
	}

	private Path renamePhoto(Path path) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				System.out.format("[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
			}
		}
		
		ExifSubIFDDirectory exifDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		DateTime original = new DateTime(exifDir.getDateOriginal(DateTimeZone.forID("Asia/Seoul").toTimeZone()).getTime());
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd_HHmmss").withZone(DateTimeZone.forID("Asia/Seoul"));
		System.out.println(formatter.print(original));
		
		ExifIFD0Directory dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		String model = dir.getString(ExifIFD0Directory.TAG_MODEL);
		System.out.println(model);
		
		return null;
	}

	private Path renameVideo(Path path) throws ImageProcessingException, IOException {
		DateTime modified = new DateTime(Files.getLastModifiedTime(path).toMillis(), DateTimeZone.UTC);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.forID("Asia/Seoul"));
		System.out.println(formatter.print(modified));
		return null;
	}

}
