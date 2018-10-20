package com.raegon.pivio;
import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.joda.time.DateTimeZone;

import com.raegon.pivio.path.impl.DefaultExtractor;
import com.raegon.pivio.path.impl.DefaultTransporter;
import com.raegon.pivio.path.impl.DirectoryConverter;
import com.raegon.pivio.path.impl.DuplicateConverter;
import com.raegon.pivio.path.impl.FilenameConverter;

public class PivioApplication {

    public static void main(String[] args) throws ParseException {

        // Read Options
        Options options = new Options();
        options.addOption("s", "source", true, "source directory path");
        options.addOption("t", "target", true, "target directory path");
        options.addOption("postfix", true, "rename postfix");
        options.addOption("h", "help", false, "print this message");
        options.addOption("preview", false, "preview");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.getOptions().length == 0 || cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar pivio.jar", options);
            System.exit(1);
        }

        String source = cmd.getOptionValue("s");
        String target = cmd.getOptionValue("t");
        String postfix = cmd.getOptionValue("postfix", "");

        // Pivio
        DefaultExtractor extractor = new DefaultExtractor();
        extractor.setSourceDirectory(Paths.get(source));

        FilenameConverter nameConverter = new FilenameConverter();
        nameConverter.setPattern("yyyyMMdd_HHmmss");
        nameConverter.setPostfix(postfix);
        nameConverter.setSourceZone(DateTimeZone.getDefault());
        nameConverter.setTargetZone(DateTimeZone.getDefault());

        DirectoryConverter directoryConverter = new DirectoryConverter();
        directoryConverter.setTargetDirectory(Paths.get(target));
        directoryConverter.setPattern("yyyy" + File.separator + "yyyy-MM-dd");
        directoryConverter.setSourceZone(DateTimeZone.getDefault());
        directoryConverter.setTargetZone(DateTimeZone.getDefault());

        DuplicateConverter duplicateConverter = new DuplicateConverter();
        duplicateConverter.setDuplicateDirectory(Paths.get(source).resolve("duplicate"));

        DefaultTransporter transporter = new DefaultTransporter();

        Pivio pivio = new Pivio();
        pivio.setExtractor(extractor);
        pivio.setFilenameConverter(nameConverter);
        pivio.setDirectoryConverter(directoryConverter);
        pivio.setDuplicateConverter(duplicateConverter);
        pivio.setTransporter(transporter);

        if (cmd.hasOption("preview")) {
            pivio.preview();
        } else {
            pivio.execute();
        }

    }

}
