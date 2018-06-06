package com.raegon.pivio;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class PivioTest {

    @Test
    public void testRename() throws IOException {
        String directory = "src/test/resources/sample";
        Pivio pivio = new Pivio();

        pivio.rename(directory);
        fail("Not yet implemented");
    }

}
