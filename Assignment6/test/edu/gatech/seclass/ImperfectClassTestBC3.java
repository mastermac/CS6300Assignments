package edu.gatech.seclass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImperfectClassTestBC3 {
    //less than 100% statement coverage and reveal the fault.
    @Test
    public void Test1() {
        ImperfectClass.imperfectMethod3(3);
    }

    @Test
    public void Test2() {
        ImperfectClass.imperfectMethod3(0);
    }

}
