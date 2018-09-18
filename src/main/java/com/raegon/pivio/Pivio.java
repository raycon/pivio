package com.raegon.pivio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Pivio {

    private Converter converter;

    private Executor executor;

    public void preview() {
        log.info("preview");
        converter.getMap().forEach((key, value) -> log.info("{} >> {}", key, value));
    }

    public void execute() {
        log.info("execute");
        executor.execute(converter.getMap());
    }

}
