package com.raegon.pivio;

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
        log.info("preview");
    }

    public void execute() {
        log.info("execute");
    }

}
