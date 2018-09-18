import com.raegon.pivio.Converter;
import com.raegon.pivio.Executor;
import com.raegon.pivio.Pivio;
import org.apache.commons.cli.*;

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

        Converter converter = new Converter();
        converter.setSourceDirectory(Paths.get(source));
        converter.setTargetDirectory(Paths.get(target));
        converter.setDirectoryPattern("yyyy" + File.pathSeparator + "MM");
        converter.setFilenamePattern("yyyyMMdd_HHmmss");
        converter.setPostfix(postfix);

        Pivio pivio = new Pivio();
        pivio.setConverter(converter);
        pivio.setExecutor(new Executor());

        if (cmd.hasOption("preview")) {
            pivio.preview();
        } else {
            pivio.execute();
        }

    }

}
