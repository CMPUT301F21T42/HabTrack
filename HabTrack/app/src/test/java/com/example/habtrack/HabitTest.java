package com.example.habtrack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains unit tests for {@link Habit} methods
 * @author Wendy Zhang
 */
public class HabitTest {

    private Habit mockHabit() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        return new Habit("Title","", Calendar.getInstance().getTime(), plan, true);
    }

    @Test
    void createExceptionTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("", "",
                        Calendar.getInstance().getTime(), plan, true);});
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("abcdefghi jklmnopqr s", "",
                        Calendar.getInstance().getTime(), plan, true);});
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("Title", "abcdefghi jklmnopqr stuvwxyz ABC",
                        Calendar.getInstance().getTime(), plan, true);});
    }

    @Test
    void equalsTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit1 = mockHabit();
        Habit habit2 = new Habit("Title","", Calendar.getInstance().getTime(), plan, true);
        assertEquals(habit1, habit2);
    }

    @Test
    void compareToTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit1 = new Habit("1","", Calendar.getInstance().getTime(), plan, true);
        Habit habit2 = new Habit("2","", Calendar.getInstance().getTime(), plan, true);

        ArrayList<Habit> habits1 = new ArrayList<>();
        habits1.add(habit2);
        habits1.add(habit1);

        ArrayList<Habit> habits2 = new ArrayList<>();
        habits2.add(habit1);
        habits2.add(habit2);

        assertNotEquals(habits1, habits2);

        Collections.sort(habits1);
        assertEquals(habits1, habits2);
    }

    @Test
    void getTitleTest() {
        Habit habit = mockHabit();
        assertEquals("Title", habit.getTitle());
    }

    @Test
    void setTitleTest() {
        Habit habit = mockHabit();
        assertEquals("Title", habit.getTitle());

        habit.setTitle("HabitTitle");
        assertEquals("HabitTitle", habit.getTitle());
    }

    @Test
    void setTitleExceptionTest() {
        Habit habit = mockHabit();
        assertThrows(IllegalArgumentException.class,
                () -> {habit.setTitle("This is a long habit title");});
    }

    @Test
    void getReasonTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","Reason", Calendar.getInstance().getTime(), plan, true);
        assertEquals("Reason", habit.getReason());
    }

    @Test
    void setReasonTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","Reason", Calendar.getInstance().getTime(), plan, true);
        assertEquals("Reason", habit.getReason());

        habit.setReason("UpdatedReason");
        assertEquals("UpdatedReason", habit.getReason());
    }

    @Test
    void setReasonExceptionTest() {
        Habit habit = mockHabit();
        assertThrows(IllegalArgumentException.class,
                () -> {habit.setReason("This is a really super long habit reason");});
    }

    @Test
    void getStartDateTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Date startDate = Calendar.getInstance().getTime();
        Habit habit = new Habit("Title","", startDate, plan, true);
        assertEquals(startDate, habit.getStartDate());
    }

    @Test
    void setStartDateTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Date startDate = Calendar.getInstance().getTime();
        Habit habit = new Habit("Title","", startDate, plan, true);
        assertEquals(startDate, habit.getStartDate());

        Date updateDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        habit.setStartDate(updateDate);
        assertNotEquals(startDate, habit.getStartDate());
        assertEquals(updateDate, habit.getStartDate());
    }

    @Test
    void isPublicTest() {
        Habit habit = mockHabit();
        assertTrue(habit.isPublic());
    }

    @Test
    void setPublicTest() {
        Habit habit = mockHabit();
        assertTrue(habit.isPublic());

        habit.setPublic(false);
        assertFalse(habit.isPublic());
    }

    @Test
    void progressTest() {
        Habit habit = mockHabit();

        // getProgressTest
        assertEquals(0, habit.getProgress());
        assertEquals(0, habit.getProgressNumerator());
        assertEquals(0, habit.getProgressDenominator());

        habit.setProgressNumerator(2);
        habit.setProgressDenominator(3);
        assertEquals(66, habit.getProgress());

        // Numerator = 3, Denominator = 3
        habit.incrementProgressNumerator();
        assertEquals(100, habit.getProgress());

        // Numerator = 1, Denominator = 3
        habit.decrementProgressNumerator();
        habit.decrementProgressNumerator();
        assertEquals(33, habit.getProgress());

        // Numerator = 1, Denominator = 4
        habit.incrementProgressDenominator();
        assertEquals(25, habit.getProgress());

        // Numerator = 1, Denominator = 3
        habit.decrementProgressDenominator();
        assertEquals(33, habit.getProgress());
    }
}
