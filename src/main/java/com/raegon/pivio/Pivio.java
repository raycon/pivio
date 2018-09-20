package com.raegon.pivio;

import com.raegon.pivio.path.impl.DefaultTransporter;
import com.raegon.pivio.path.impl.MediaExtractor;
import com.raegon.pivio.path.impl.FilenameConverter;
import com.raegon.pivio.path.impl.DirectoryConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Pivio {

    private MediaExtractor extractor;

    private FilenameConverter renamer;

    private DirectoryConverter transporter;

    private DefaultTransporter executor;

    public void preview() {
        log.info("preview");
    }

    public void execute() {
        log.info("execute");
    }

}
