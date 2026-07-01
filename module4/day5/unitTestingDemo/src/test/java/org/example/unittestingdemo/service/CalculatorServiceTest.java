package org.example.unittestingdemo.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorServiceTest {

    private static CalculatorService calculatorService;

    @BeforeAll
    static void init() {
        System.out.println("BeforeAll: init() method called");
        calculatorService = new CalculatorService();
    }

    @BeforeEach
    void setUp() {
        System.err.println("setting up the test");
    }

    @Test
    void testAdd() {
        int result = calculatorService.add(2, 3);
        int expected = 5;
        assert result == expected;
    }

    @Test
    void testSub() {
        int result = calculatorService.sub(12, 3);
        int expected = 9;
        assert result == expected;
    }

    @Test
    void testMul() {
        int result = calculatorService.mul(2, 3);
        int expected = 6;
        assert result == expected;
    }

    @Test
    void testDiv() {
        int result = calculatorService.div(10, 2);
        int expected = 5;
        assert result == expected;
    }

    @AfterAll
    static void tearDown() {
        System.out.println("tearDown: method called");
    }
}
