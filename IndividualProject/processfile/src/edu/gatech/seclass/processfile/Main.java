package edu.gatech.seclass.processfile;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class Main {

    // Empty Main class for compiling Individual Project.
    // DO NOT ALTER THIS CLASS or implement it.
    private static final Charset charset = StandardCharsets.UTF_8;
    private static CommandLine cmd = null;
    private static ArrayList<String> lines;
    private static String filecontent;

    public static void main(String[] args) throws FileNotFoundException {
        // Empty Skeleton Method

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option r = Option.builder("r").numberOfArgs(2).hasArg(true).desc("string").build();
        r.setArgs(2);
        options.addOption("f", false, "overwrite");
        options.addOption("n", false, "add line numbers");
        options.addOption("s", true, "string");
        options.addOption("g", false, "all occurrences");
        options.addOption("i", false, "case insensitive");
        options.addOption("p", true, "string");
        options.addOption(r);

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            usage();
            return;
        }

        ArrayList<String> l = new ArrayList<String>(cmd.getArgList());

        if ((cmd.getOptions().length) == 0 && (l.size() > 1)) {
            usage();
            return;
        }

        if (l.size() == 0) {
            usage();
            return;
        }

        String file = l.get(l.size() - 1);

        try {
            filecontent = new String(Files.readAllBytes(Paths.get(file)), charset);
        } catch (InvalidPathException e) {
            usage();
            return;
        } catch (FileNotFoundException e) {
            usage();
            return;
        } catch (IOException e) {
            usage();
            return;
        }

        if (filecontent.length() == 0) {
            if (!cmd.hasOption("f")) {
                System.out.print("");
            }
            return;
        }

        lines = new ArrayList<String>(Arrays.asList(filecontent.split(System.getProperty("line.separator"))));
        int os = lines.size();

        if (cmd.hasOption("i") && (!cmd.hasOption("r")) && (!cmd.hasOption("s"))) {
            usage();
            return;
        }

        if (cmd.hasOption("g") && (!cmd.hasOption("r"))) {
            usage();
            return;
        }

        if (cmd.hasOption("s")) {
            if (cmd.getOptionValues("s").length == 0) {
                usage();
                return;
            }
            String arg;
            arg = cmd.getOptionValues("s")[cmd.getOptionValues("s").length - 1];
            if (arg != "") {
                lines = optionS(arg);
            }
        }

        if (cmd.hasOption("r")) {
            if (cmd.getOptionValues("r").length == 0) {
                usage();
                return;
            }
            String arg1;
            String arg2;
            arg1 = cmd.getOptionValues("r")[cmd.getOptionValues("r").length - 2];
            arg2 = cmd.getOptionValues("r")[cmd.getOptionValues("r").length - 1];
            if (arg1 == "") {
                usage();
                return;
            } else {
                lines = optionR(arg1, arg2);
            }
        }

        if (cmd.hasOption("p")) {
            String arg;
            arg = cmd.getOptionValues("p")[cmd.getOptionValues("p").length - 1];
            lines = optionP(arg);
        }

        if (cmd.hasOption("n")) {
            lines = optionN();
        }

        String listString = String.join(System.lineSeparator(), lines);
        if (filecontent.lastIndexOf(System.lineSeparator()) == filecontent.length() - 1) {
            if (os == 0 && cmd.hasOption("n")) {
                int c = StringUtils.countMatches(filecontent, System.lineSeparator());
                for (int i = 0; i < c; i++) {
                    listString += String.valueOf(i + 1) + " " + System.lineSeparator();
                }
            } else {
                if (filecontent.length() > 1) {
                    if (cmd.hasOption("n") && filecontent.substring(filecontent.length() - 2, filecontent.length() - 1).equals(System.lineSeparator())) {
                        listString += System.lineSeparator() + String.valueOf(lines.size() + 1) + " ";
                    }
                }
                listString += System.lineSeparator();
            }
        }

        if (cmd.hasOption("f")) {
            PrintWriter writer = new PrintWriter(file);
            writer.write(listString);
            writer.close();
            writer.flush();
        } else {
            System.out.print(listString);
        }
    }

    private static ArrayList<String> optionN() {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            temp.add(String.valueOf(i + 1) + " " + lines.get(i));
        }
        return temp;
    }

    private static ArrayList<String> optionP(String arg) {
        ArrayList<String> temp = new ArrayList<String>();
        for (String line : lines) {
            temp.add(arg + line);
        }
        return temp;
    }

    private static ArrayList<String> optionR(String arg1, String arg2) {
        ArrayList<String> temp = new ArrayList<String>();
        if (cmd.hasOption("i")) {
            if (cmd.hasOption("g")) {
                for (String line : lines) {
                    temp.add(line.replaceAll("(?i)" + Pattern.quote(arg1), Matcher.quoteReplacement(arg2)));
                }
            } else {
                for (String line : lines) {
                    temp.add(line.replaceFirst("(?i)" + Pattern.quote(arg1), Matcher.quoteReplacement(arg2)));
                }
            }
        } else {
            if (cmd.hasOption("g")) {
                for (String line : lines) {
                    temp.add(line.replace(arg1, arg2));
                }
            } else {
                for (String line : lines) {
                    temp.add(line.replaceFirst(Pattern.quote(arg1), Matcher.quoteReplacement(arg2)));
                }
            }
        }
        return temp;
    }

    private static ArrayList<String> optionS(String arg) {
        ArrayList<String> temp = new ArrayList<String>();
        if (cmd.hasOption("i")) {
            for (String line : lines) {
                if (line.toLowerCase(Locale.ROOT).contains(arg.toLowerCase(Locale.ROOT))) {
                    temp.add(line);
                }
            }
        } else {
            for (String line : lines) {
                if (line.contains(arg)) {
                    temp.add(line);
                }
            }
        }
        return temp;
    }

    private static void usage() {
        System.err.println("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE");
    }
}
