import com.raegon.pivio.Pivio;
import org.apache.commons.cli.*;

import java.nio.file.Paths;

public class PivioApplication {

    public static void main(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption("s", "source", true, "source path");
        options.addOption("t", "target", true, "target path");
        options.addOption("po", "postfix", true, "rename postfix");
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
        String postfix = cmd.getOptionValue("po");

        Pivio pivio = new Pivio();
        pivio.setSource(Paths.get(source));
        pivio.setTarget(Paths.get(target));
        pivio.setPostfix(postfix);
        
        if (cmd.hasOption("preview")) {
            pivio.preview();
        } else {
            pivio.execute();
        }

    }

}
