package edu.gatech.seclass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Junit test class created for use in Georgia Tech CS6300.
 * <p>
 * You should implement your tests in this class.
 */

public class MyStringTest {

    private MyStringInterface mystring;

    @Before
    public void setUp() {
        mystring = new MyString();
    }

    @After
    public void tearDown() {
        mystring = null;
    }

    @Test
    // Description: Instructor-provided test 1
    public void testCountNumbersS1() {
        mystring.setString("My numbers are 11, 96, and thirteen");
        assertEquals(2, mystring.countNumbers());
    }

    @Test(expected = NullPointerException.class)
    // Description: <Count numbers when string is null>
    public void testCountNumbersS2() {
        mystring.setString(null);
        mystring.countNumbers();
    }

    @Test
    // Description: <Count numbers in an empty string>
    public void testCountNumbersS3() {
        mystring.setString("");
        assertEquals(0, mystring.countNumbers());
    }

    @Test
    // Description: <Counter numbers when there is . among digit>
    public void testCountNumbersS4() {
        mystring.setString("I don't handle real number such as 10.4");
        assertEquals(2, mystring.countNumbers());
    }

    @Test
    // Description: Instructor-provided test 2
    public void testAddNumberS1() {
        mystring.setString("hello 90, bye 2");
        assertEquals("hello 92, bye 4", mystring.addNumber(2, false));
    }

    @Test(expected = IllegalArgumentException.class)
    // Description: <n is negative, and the current string is not null>
    public void testAddNumberS2() {
        mystring.setString("hello 90, bye 2");
        mystring.addNumber(-1, false);
    }

    @Test(expected = NullPointerException.class)
    // Description: <If the current string is null>
    public void testAddNumberS3() {
        mystring.setString(null);
        mystring.addNumber(2, false);
    }

    @Test
    // Description: <when string contain only numbers>
    public void testAddNumberS4() {
        mystring.setString("12345");
        assertEquals("12346", mystring.addNumber(1, false));
    }

    @Test
    // Description: <when invert is true>
    public void testAddNumberS5() {
        mystring.setString("hello 90, bye 2");
        assertEquals("hello 89, bye 01", mystring.addNumber(8, true));
    }

    @Test
    // Description: <n is a large number>
    public void testAddNumberS6() {
        mystring.setString("hello 90, bye 2");
        assertEquals("hello 091, bye 201", mystring.addNumber(100, true));
    }

    @Test
    // Description: Instructor-provided test 3
    public void testConvertDigitsToNamesInSubstringS1() {
        mystring.setString("I'd b3tt3r put s0me d161ts in this 5tr1n6, right?");
        mystring.convertDigitsToNamesInSubstring(17, 23);
        assertEquals("I'd b3tt3r put szerome donesix1ts in this 5tr1n6, right?", mystring.getString());
    }

    @Test(expected = NullPointerException.class)
    // Description: <when the current string is null>
    public void testConvertDigitsToNamesInSubstringS2() {
        mystring.setString(null);
        mystring.convertDigitsToNamesInSubstring(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    // Description: <If "initialPosition" < 1 or "initialPosition" > "finalPosition">
    public void testConvertDigitsToNamesInSubstringS3() {
        mystring.setString("hello 90, bye 2");
        mystring.convertDigitsToNamesInSubstring(-2, 2);
    }

    @Test(expected = MyIndexOutOfBoundsException.class)
    // Description: <"finalPosition" is out of bounds>
    public void testConvertDigitsToNamesInSubstringS4() {
        mystring.setString("hello 90, bye 2");
        mystring.convertDigitsToNamesInSubstring(2, 100);
    }

    @Test
    // Description: <string with no space>
    public void testConvertDigitsToNamesInSubstringS5() {
        mystring.setString("abc416d");
        mystring.convertDigitsToNamesInSubstring(2, 7);
        assertEquals("abcfouronesixd", mystring.getString());
    }

    @Test
    // Description: <finalpostion is same as initialposition>
    public void testConvertDigitsToNamesInSubstringS6() {
        mystring.setString("abc416d");
        mystring.convertDigitsToNamesInSubstring(4, 4);
        assertEquals("abcfour16d", mystring.getString());
    }
}

