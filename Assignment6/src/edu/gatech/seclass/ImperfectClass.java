package edu.gatech.seclass;

/**
 * This is a Georgia Tech provided code example for use in assigned
 * private GT repositories. Students and other users of this template
 * code are advised not to share it with other students or to make it
 * available on publicly viewable websites including repositories such
 * as Github and Gitlab.  Such sharing may be investigated as a GT
 * honor code violation. Created for CS6300 Summer 2021.
 * <p>
 * Template provided for the White-Box Testing Assignment. Follow the
 * assignment directions to either implement or provide comments for
 * the appropriate methods.
 */

public class ImperfectClass {

    public static void exampleMethod1(int a) {
        // ...
        int x = a / 0; // Example of instruction that makes the method
        // fail with an ArithmeticException
        // ...
    }

    public static int exampleMethod2(int a, int b) {
        // ...
        return (a + b) / 0; // Example of instruction that makes the
        // method fail with an ArithmeticException
    }

    public static void exampleMethod3() {
        // NOT POSSIBLE: This method cannot be implemented because
        // <REPLACE WITH REASON> (this is the example format for a
        // method that is not possible) ***/
    }

    public static void imperfectMethod1(int a) { // Change the signature as needed
        // Either add a comment in the format provided above or
        // implement the method.
        int x;
        if (a >= 0) {
            x = 3 / a;
        } else {
            x = 3 + a;
        }
    }

    public static void imperfectMethod2() { // Change the signature as needed
        //NOT POSSIBLE: This method cannot be implemented because
        //Branch Coverage subsumes Statement Coverage. so all the test widths
        //that satisfy that criteria will also satisfy the other one
        //There is no way to cover all the branches without covering all the statements
        //So, if every possible 100% BC does not reveal the fault, then that means
        // none of 100% SC can reveal the fault.
    }

    public static void imperfectMethod3(int a) { // Change the signature as needed
        int x;
        if (a != 0) {
            x = 3 + a;
        }
        x = 3 / a;
    }

    public static void imperfectMethod4(int a) { // Change the signature as needed
        //NOT POSSIBLE: This method cannot be implemented because
        //a 100% PC suite is also a 100% BC suite, we already know that every
        //possible 100% BC test suite does not reveal the fault.
        //that means every possible 100% PC suite does not reveal the fault
        //because every possible 100% PC suite is a 100% BC test suite.
    }

    public static String[] imperfectMethod5() {
        String a[] = new String[7];
        /*
				public static boolean providedImperfectMethod(boolean a, boolean b) {
  					int x = 24;
                    int y = 24;
                    if (a)
                        x = y;
                    else
                        x = -2*x;
                    if (b)
                        y = 0-x;
                    return ((100/(x-y))>= 0);
				}
        */
        //
        // Replace the "?" in column "output" with "T", "F", or "E":
        //
        //         | a | b |output|
        //         ================
        a[0] =  /* | T | T | <T, F, or E> (e.g., "T") */ "T";
        a[1] =  /* | T | F | <T, F, or E> (e.g., "T") */ "E";
        a[2] =  /* | F | T | <T, F, or E> (e.g., "T") */ "F";
        a[3] =  /* | F | F | <T, F, or E> (e.g., "T") */ "F";
        // ================
        //
        // Replace the "?" in the following sentences with "NEVER",
        // "SOMETIMES" or "ALWAYS":
        //
        a[4] = /* Test suites with 100% statement coverage */ "SOMETIMES";
        /*reveal the fault in this method.*/
        a[5] = /* Test suites with 100% branch coverage */ "SOMETIMES";
        /*reveal the fault in this method.*/
        a[6] =  /* Test suites with 100% path coverage */ "ALWAYS";
        /*reveal the fault in this method.*/
        // ================
        return a;
    }
}

