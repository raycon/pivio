import com.raegon.pivio.*;
import com.raegon.pivio.path.impl.DefaultTransporter;
import com.raegon.pivio.path.impl.MediaExtractor;
import com.raegon.pivio.path.impl.FilenameConverter;
import com.raegon.pivio.path.impl.DirectoryConverter;
import org.apache.commons.cli.*;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.nio.file.Paths;

public class PivioApplication {

    public static void main(String[] args) throws ParseException {

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

        MediaExtractor extractor = new MediaExtractor();
        extractor.setSource(Paths.get(source));

        FilenameConverter renamer = new FilenameConverter();
        renamer.setPattern("yyyyMMdd_HHmmss");
        renamer.setPostfix(postfix);
        renamer.setSourceZone(DateTimeZone.getDefault());
        renamer.setTargetZone(DateTimeZone.getDefault());

        DirectoryConverter transporter = new DirectoryConverter();
        transporter.setTarget(Paths.get(target));
        transporter.setPattern("yyyy"+ File.pathSeparator +"MM"+ File.pathSeparator +"yyyy-MM-dd");
        transporter.setSourceZone(DateTimeZone.getDefault());
        transporter.setTargetZone(DateTimeZone.getDefault());

        DefaultTransporter executor = new DefaultTransporter();

        Pivio pivio = new Pivio();
        pivio.setExtractor(extractor);
        pivio.setRenamer(renamer);
        pivio.setTransporter(transporter);
        pivio.setExecutor(executor);

        if (cmd.hasOption("preview")) {
            pivio.preview();
        } else {
            pivio.execute();
        }

    }

}
