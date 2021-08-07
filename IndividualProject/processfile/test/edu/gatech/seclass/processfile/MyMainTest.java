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

public class MyMainTest {

    // Place all  of your tests in this class, optionally using MainTest.java as an example.
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
        File file = createTmpFile();

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

    // Frame #: 1
    @Test
    public void processfileTest1() throws Exception {
        String input = "0123456789" + System.lineSeparator() + "abcdefghi" + System.lineSeparator();
        String expected = "1 0123456789" + System.lineSeparator() + "2 abcdefghi" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-f", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 2
    @Test
    public void processfileTest2() throws Exception {
        String input = "0123456789" + System.lineSeparator() + "abcdefghi" + System.lineSeparator();
        String expected = "1 0123456789" + System.lineSeparator() + "2 abcdefghi" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-n", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 3
    @Test
    public void processfileTest3() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "hello";

        File inputFile = createInputFile(input);
        String args[] = {"-s", "albert", "-s", "hello", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 4
    @Test
    public void processfileTest4() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "pig", "-r", "cat", "dog", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 5
    @Test
    public void processfileTest5() throws Exception {
        String input = "I have a cat cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his dog is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-g","-g",inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 6
    @Test
    public void processfileTest6() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-g", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 7
    @Test
    public void processfileTest7() throws Exception {
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

    // Frame #: 8
    @Test
    public void processfileTest8() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello" + System.lineSeparator();
        String expected = "Hello" + System.lineSeparator() + "hello" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 9
    @Test
    public void processfileTest9() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-i", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 10
    @Test
    public void processfileTest10() throws Exception {
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

    // Frame #: 11
    @Test
    public void processfileTest11() throws Exception {
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
        String args[] = {"-n", "-p", "$$", "-p", "##", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 12
    @Test
    public void processfileTest12() throws Exception {
        String input = "";
        String expected = "";
        File inputFile = createInputFile(input);
        String args[] = {"-n", "-p", "##", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 13
    @Test
    public void processfileTest13() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "hello albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello" + System.lineSeparator();
        String expected = "Hello" + System.lineSeparator() + "hello albert" + System.lineSeparator() +
                "hello" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 14
    @Test
    public void processfileTest14() throws Exception {
        String input = "I have a cat cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT cat and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his dog is yellow" + System.lineSeparator() +
                "I have 1 dog dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-g", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 15
    @Test
    public void processfileTest15() throws Exception {
        String input = "Hello hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "hello hello albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "Hello" + System.lineSeparator();
        String expected = "Hello hello" + System.lineSeparator() + "hello hello albert" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 16
    @Test
    public void processfileTest16() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 17
    @Test
    public void processfileTest17() throws Exception {
        String input = "I have a cat cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT Cat and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog Cat and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 18
    @Test
    public void processfileTest18() throws Exception {
        String input = "I have a cat cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT Cat and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT Cat and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 19
    @Test
    public void processfileTest19() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"s", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 20
    @Test
    public void processfileTest20() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";

        File inputFile = createInputFile(input);
        String args[] = {"-s", "", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 21
    @Test
    public void processfileTest21() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "@#$%";


        File inputFile = createInputFile(input);
        String args[] = {"-s", "@", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 22
    @Test
    public void processfileTest22() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@3#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "@3#$%";


        File inputFile = createInputFile(input);
        String args[] = {"-s", "@3", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 23
    @Test
    public void processfileTest23() throws Exception {
        String input = "Hello Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@3#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "Hello Hello";


        File inputFile = createInputFile(input);
        String args[] = {"-s", " ", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 24
    @Test
    public void processfileTest24() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "dog", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 25
    @Test
    public void processfileTest25() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My dog is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "", "dog", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 26
    @Test
    public void processfileTest26() throws Exception {
        String input = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 @#$ and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "@#$", "dog", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 27
    @Test
    public void processfileTest27() throws Exception {
        String input = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 @#$CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "@#$cat", "dog", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 28
    @Test
    public void processfileTest28() throws Exception {
        String input = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1   and 2 birdS" + System.lineSeparator();
        String expected = "I have a dog" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1dogand 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "   ", "dog", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 29
    @Test
    public void processfileTest29() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 30
    @Test
    public void processfileTest30() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a @@" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My @@ is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "@@", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 31
    @Test
    public void processfileTest31() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a @@AA" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My @@AA is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "@@AA", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 32
    @Test
    public void processfileTest32() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a   " + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My    is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "  ", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 33
    @Test
    public void processfileTest33() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-p", inputFile.getPath()};
        Main.main(args);
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    // Frame #: 34
    @Test
    public void processfileTest34() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "AI have a cat" + System.lineSeparator() +
                "AI have two birds" + System.lineSeparator() +
                "AMy cat is brown and his cat is yellow" + System.lineSeparator() +
                "AI have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-p", "A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 35
    @Test
    public void processfileTest35() throws Exception {
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

    // Frame #: 36
    @Test
    public void processfileTest36() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "  I have a cat" + System.lineSeparator() +
                "  I have two birds" + System.lineSeparator() +
                "  My cat is brown and his cat is yellow" + System.lineSeparator() +
                "  I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-p", "  ", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 37
    @Test
    public void processfileTest37() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "A@I have a cat" + System.lineSeparator() +
                "A@I have two birds" + System.lineSeparator() +
                "A@My cat is brown and his cat is yellow" + System.lineSeparator() +
                "A@I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-p", "A@", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 38
    @Test
    public void processfileTest38() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My pig is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog and 2 birdS" + System.lineSeparator();
        String expected = "#AI have a dog" + System.lineSeparator() +
                "#AI have two birds" + System.lineSeparator() +
                "#AMy pig is brown and his dog is yellow" + System.lineSeparator() +
                "#AI have 1 dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 39
    @Test
    public void processfileTest39() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My pig is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 dog and 2 birdS" + System.lineSeparator();
        String expected = "#AI have a dog" + System.lineSeparator() +
                "#AI have two birds" + System.lineSeparator() +
                "#AMy pig is brown and his dog is yellow" + System.lineSeparator() +
                "#AI have 1 dog and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 40
    @Test
    public void processfileTest40() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "bye" + System.lineSeparator();
        String expected = "#AHello"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 41
    @Test
    public void processfileTest41() throws Exception {
        String input = "I like one And two" + System.lineSeparator() +
                "Purple flowers" + System.lineSeparator() +
                "Georgia Tech Cremson" + System.lineSeparator();
        String expected = "NO#I like one but two"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "and", "but", "-s", "and", "-p", "NO#", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 42
    @Test
    public void processfileTest42() throws Exception {
        String input = "I like one And two" + System.lineSeparator() +
                "Purple flowers" + System.lineSeparator() +
                "Georgia Tech Cremson" + System.lineSeparator();
        String expected = "1 NO#I like one but two"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "and", "but", "-s", "and", "-p", "NO#", "-i", "-n", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 43
    @Test
    public void processfileTest43() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "1 ##AHello" + System.lineSeparator() +
                "2 ##ABeatrice" + System.lineSeparator() +
                "3 ##Aalbert" + System.lineSeparator() +
                "4 ##A@#$%" + System.lineSeparator() +
                "5 ##A#%Albert" + System.lineSeparator() +
                "6 ##A--’’--911" + System.lineSeparator() +
                "7 ##Ahello";

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-p", "##A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 44
    @Test
    public void processfileTest44() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();

        String expected = "1 #AI have a dog" + System.lineSeparator() +
                "2 #AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-n", "-i", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 45
    @Test
    public void processfileTest45() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();

        String expected = "1 #AI have a dog" + System.lineSeparator() +
                "2 #AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-n", "-i", "-p", "#A", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 46
    @Test
    public void processfileTest46() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator();
        String expected = "1 #AHello"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", "-n", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 47
    @Test
    public void processfileTest47() throws Exception {
        String input = "I like one And two" + System.lineSeparator() +
                "Purple flowers" + System.lineSeparator() +
                "Georgia Tech Cremson" + System.lineSeparator();
        String expected = "1 NO#I like two And two"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "one", "two", "-s", "and", "-p", "NO#", "-i", "-n", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 48
    @Test
    public void processfileTest48() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();

        String expected = "1 #AI have a dog" + System.lineSeparator() +
                "2 #AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-n", "-i", "-p", "#A", "-g", "-s", "I", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Frame #: 49
    @Test
    public void processfileTest49() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "##AHello" + System.lineSeparator() +
                "##ABeatrice" + System.lineSeparator() +
                "##Aalbert" + System.lineSeparator() +
                "##A@#$%" + System.lineSeparator() +
                "##A#%Albert" + System.lineSeparator() +
                "##A--’’--911" + System.lineSeparator() +
                "##Ahello";

        File inputFile = createInputFile(input);
        String args[] = {"-p", "##A", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 50
    @Test
    public void processfileTest50() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();
        String expected = "#AI have a dog" + System.lineSeparator() +
                "#AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-p", "#A", "-f", "-i", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));

    }

    // Frame #: 51
    @Test
    public void processfileTest51() throws Exception {
        String input = "We have a Cat" + System.lineSeparator() +
                "We have two birds" + System.lineSeparator();

        String expected = ""+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-g", "-s", "Z", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 52
    @Test
    public void processfileTest52() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator();
        String expected = "#AHello"+ System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", "-p", "#A", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 53
    @Test
    public void processfileTest53() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();
        String expected = "#AI have a dog" + System.lineSeparator() +
                "#AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-s", "I", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 54
    @Test
    public void processfileTest54() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();
        String expected = "#AI have a dog" + System.lineSeparator() +
                "#AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-s", "I", "-f", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 55
    @Test
    public void processfileTest55() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator() +
                "hello";
        String expected = "1 ##AHello" + System.lineSeparator() +
                "2 ##ABeatrice" + System.lineSeparator() +
                "3 ##Aalbert" + System.lineSeparator() +
                "4 ##A@#$%" + System.lineSeparator() +
                "5 ##A#%Albert" + System.lineSeparator() +
                "6 ##A--’’--911" + System.lineSeparator() +
                "7 ##Ahello";

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-p", "##A", "-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 56
    @Test
    public void processfileTest56() throws Exception {
        String input = "0123456789 a" + System.lineSeparator() + "bcdefghi" + System.lineSeparator();
        String expected = "1 #A0123456789 B" + System.lineSeparator() + "2 #Abcdefghi" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-f", "-r", "A", "B", "-i", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 57
    @Test
    public void processfileTest57() throws Exception {
        String input = "0123456789 a" + System.lineSeparator() + "bcdefghi" + System.lineSeparator();
        String expected = "1 #A0123456789 B" + System.lineSeparator() + "2 #Abcdefghi" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-n", "-f", "-r", "A", "B", "-g", "-i", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 58
    @Test
    public void processfileTest58() throws Exception {
        String input = "Hello" + System.lineSeparator() +
                "Beatrice" + System.lineSeparator() +
                "albert" + System.lineSeparator() +
                "@#$%" + System.lineSeparator() +
                "#%Albert" + System.lineSeparator() +
                "--’’--911" + System.lineSeparator();
        String expected = "1 #AHello" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-s", "hello", "-i", "-f", "-n", "-p", "#A", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 59
    @Test
    public void processfileTest59() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();
        String expected = "1 #AI have a dog" + System.lineSeparator() +
                "2 #AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-s", "I", "-f", "-n", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Frame #: 60
    @Test
    public void processfileTest60() throws Exception {
        String input = "I have a Cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator();
        String expected = "1 #AI have a dog" + System.lineSeparator() +
                "2 #AI have two birds" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "dog", "-i", "-p", "#A", "-s", "I", "-f", "-n", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }
    //tests added for D2
    @Test
    public void processfileTest61() throws Exception {
        String input = "I have a cat. Can I have a dog?" + System.lineSeparator()
        +"I have two birds" + System.lineSeparator()
        +"My cat is brown and his cat is yellow?" + System.lineSeparator()
        +"I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I sold a cat. Can I sold a dog?" + System.lineSeparator()+
                "My cat is brown and his cat is yellow?" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r","I","We","-g","-s","?","-r","have","sold","-f",inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // command line argument errors should display a usage message on stderr
    @Test
    public void processfileTest62() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String args[]  = new String[0];
        Main.main(args);
        assertEquals("Usage: processfile [ -f | -n | -s string | -r string1 string2 | -g | -i | -p  ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }

    @Test
    public void processfileTest63() throws Exception {
        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-p", "", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    @Test
    public void processfileTest64() throws Exception {

        String input = "I have a cat" + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My cat is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();
        String expected = "I have a " + System.lineSeparator() +
                "I have two birds" + System.lineSeparator() +
                "My  is brown and his cat is yellow" + System.lineSeparator() +
                "I have 1 CaT and 2 birdS" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String args[] = {"-r", "cat", "", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

}


