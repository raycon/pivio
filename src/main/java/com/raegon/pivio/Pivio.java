package com.raegon.pivio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Pivio {

    private Extractor extractor;

    private Renamer renamer;

    private Transporter transporter;

    private Executor executor;

    public void preview() {
        log.info("preview");
    }

    public void execute() {
        log.info("execute");
    }

}
