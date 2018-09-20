package com.raegon.pivio.path.impl;

import com.raegon.pivio.path.Transporter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
public class DefaultTransporter implements Transporter {

    @Override
    public void transfer(Map<Path, Path> map) {
        map.forEach(this::transfer);
    }

    private void transfer(Path source, Path target) {
        log.info("Transfer {} > {}", source, target);
        try {
            Path parent = target.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.move(source, target);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
