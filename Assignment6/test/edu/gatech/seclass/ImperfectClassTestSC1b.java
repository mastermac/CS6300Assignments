package edu.gatech.seclass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImperfectClassTestSC1b {
    // 100% statement coverage and do not reveal the fault.
    @Test
    public void Test1() {
        ImperfectClass.imperfectMethod1(1);
    }

    @Test
    public void Test2() {
        ImperfectClass.imperfectMethod1(-1);
    }
}