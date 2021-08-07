package edu.gatech.seclass.processfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

public class MainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private final Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
     *  TEST UTILITIES
     */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file =  createTmpFile();

        OutputStreamWriter fileWriter =
                     new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }

    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
     *   TEST CASES
     */

    // Instructions Example 1
    @Test
    public void mainTest1() throws Exception {

        String input = "0123456789" + System.lineSeparator() + "abcdefghi";
        String expected = "1 0123456789" + System.lineSeparator() + "2 abcdefghi";

        File inputFile = createInputFile(input);
        String args[] = {"-n", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructions Example 2
    @Test
    public void mainTest2() throws Exception {
        String input = "0123456789" + System.lineSeparator() + "abcdefghi" + System.lineSeparator();
        String expected = "1 0123456789" + System.lineSeparator() + "2 abcdefghi" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Instructions Example 3
    @Test
    public void mainTest3() throws Exception {

        String input = "Hello" + System.lineSeparator() + 
                       "Beatrice" + System.lineSeparator() +
                       "albert" + System.lineSeparator() + 
                       "@#$%" + System.lineSeparator() +
                       "#%Albert" + System.lineSeparator() +
                       "--’’--911" + System.lineSeparator() +
                       "hello";
        String expected = "hello";

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructions Example 4
    @Test
    public void mainTest4() throws Exception {

        String input = "Hello" + System.lineSeparator() + 
                       "Beatrice" + System.lineSeparator() +
                       "albert" + System.lineSeparator() + 
                       "@#$%" + System.lineSeparator() +
                       "#%Albert" + System.lineSeparator() +
                       "--’’--911" + System.lineSeparator() +
                       "hello" + System.lineSeparator();
        String expected = "Hello" + System.lineSeparator() + "hello" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructions Example 5
    @Test
    public void mainTest5() throws Exception {

        String input = "I have a cat" + System.lineSeparator() + 
                       "I have two birds" + System.lineSeparator() + 
                       "My cat is brown and his cat is yellow" + System.lineSeparator() + 
                       "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() + 
                          "I have two birds" + System.lineSeparator() + 
                          "My dog is brown and his cat is yellow" + System.lineSeparator() + 
                          "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructions Example 8
    @Test
    public void mainTest6() throws Exception {

        String input = "Hello" + System.lineSeparator() + 
                       "Beatrice" + System.lineSeparator() +
                       "albert" + System.lineSeparator() + 
                       "@#$%" + System.lineSeparator() +
                       "#%Albert" + System.lineSeparator() +
                       "--’’--911" + System.lineSeparator() +
                       "hello";
        String expected = "1 ##Hello" + System.lineSeparator() + 
                          "2 ##Beatrice" + System.lineSeparator() +
                          "3 ##albert" + System.lineSeparator() + 
                          "4 ##@#$%" + System.lineSeparator() +
                          "5 ###%Albert" + System.lineSeparator() +
                          "6 ##--’’--911" + System.lineSeparator() +
                          "7 ##hello";

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-p", "##", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // command line argument errors should display a usage message on stderr
    @Test
    public void mainTest7() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String args[]  = new String[0];
        Main.main(args);
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }
}
