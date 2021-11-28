package com.example.habtrack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for {@link HabitHandler} methods
 * @author Wendy Zhang
 */
public class HabitHandlerTest {

    private HabitHandler mockHabitHandler() {
        return new HabitHandler();
    }

    @Test
    void isLegalTitleTest() {
        HabitHandler habitHandler = mockHabitHandler();
        assertTrue(habitHandler.isLegalTitle("Title"));
        assertFalse(habitHandler.isLegalTitle(" "));
        assertFalse(habitHandler.isLegalTitle("This is a long habit title"));
    }

    @Test
    void isLegalReasonTest() {
        HabitHandler habitHandler = mockHabitHandler();
        assertTrue(habitHandler.isLegalReason("Reason"));
        assertFalse(habitHandler.isLegalReason("This is a really super long habit reason"));
    }

    @Test
    void isLegalStartDateTest() {
        HabitHandler habitHandler = mockHabitHandler();
        assertTrue(habitHandler.isLegalDate("2021-11-01"));
        assertFalse(habitHandler.isLegalDate("20111101"));
        assertFalse(habitHandler.isLegalDate("2021-02-30"));
    }
}
