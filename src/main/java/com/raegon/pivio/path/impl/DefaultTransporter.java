package com.raegon.pivio.path.impl;

import com.raegon.pivio.path.Transporter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Data
@Slf4j
public class DefaultTransporter implements Transporter {

    @Override
    public void transfer(Map<Path, Path> map) {
        map.forEach((source, target) -> {
            try {
                transfer(source, target);
            } catch (IOException e) {
                log.error("Unable to transfer, {}, {}", source, e.getMessage());
            }
        });
    }

    private void transfer(Path source, Path target) throws IOException {
        if (Files.exists(target)) {
            throw new RuntimeException("File exists, " + target.toString());
        }
        // Create directory
        Path parent = target.getParent();
        if (!Files.exists(parent)) {
            log.info("+ {}", parent);
            Files.createDirectories(parent);
        }
        // Move
        log.info("> {} > {}", source, target);
        Files.move(source, target);
    }

}
