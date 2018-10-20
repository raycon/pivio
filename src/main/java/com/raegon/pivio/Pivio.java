package com.raegon.pivio;

import java.nio.file.Path;
import java.util.Map;

import com.raegon.pivio.path.impl.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Pivio {

    private DefaultExtractor extractor;

    private FilenameConverter filenameConverter;

    private DirectoryConverter directoryConverter;

    private DuplicateConverter duplicateConverter;

    private DefaultTransporter transporter;

    public void preview() {
        print(convert(extract()));
    }

    public void execute() {
        transporter.transfer(convert(extract()));
    }

    private Map<Path, Path> extract() {
        return extractor.extract();
    }

    private Map<Path, Path> convert(Map<Path, Path> map) {
        map = filenameConverter.convert(map);
        map = directoryConverter.convert(map);
        map = duplicateConverter.convert(map);
        return map;
    }
    
    private void print(Map<Path, Path> map) {
        map.forEach((k, v) -> {
            log.info(k + " > " + v);
        });
    }

}
