package edu.gatech.seclass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImperfectClassTestSC1a {
    //less than 100% statement coverage and reveal the fault.
    @Test
    public void Test1() {
        ImperfectClass.imperfectMethod1(0);
    }
}
