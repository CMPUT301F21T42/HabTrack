package com.example.habtrack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class HabitTest {

    @Test
    void createExceptionTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("", "",
                        Calendar.getInstance().getTime(), plan);});
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("abcdefghi jklmnopqr s", "",
                        Calendar.getInstance().getTime(), plan);});
        assertThrows(IllegalArgumentException.class,
                () -> {new Habit("Title", "abcdefghi jklmnopqr stuvwxyz ABC",
                        Calendar.getInstance().getTime(), plan);});
    }

    @Test
    void equalsTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit1 = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        Habit habit2 = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        assertEquals(habit1, habit2);
    }

    @Test
    void compareToTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit1 = new Habit("1","", Calendar.getInstance().getTime(), plan);
        Habit habit2 = new Habit("2","", Calendar.getInstance().getTime(), plan);

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
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        assertEquals("Title", habit.getTitle());
    }

    @Test
    void setTitleTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        assertEquals("Title", habit.getTitle());

        habit.setTitle("HabitTitle");
        assertEquals("HabitTitle", habit.getTitle());
    }

    @Test
    void setTitleExceptionTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        assertThrows(IllegalArgumentException.class,
                () -> {habit.setTitle("abcdefghi jklmnopqr s");});
    }

    @Test
    void getReasonTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","Reason", Calendar.getInstance().getTime(), plan);
        assertEquals("Reason", habit.getReason());
    }

    @Test
    void setReasonTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","Reason", Calendar.getInstance().getTime(), plan);
        assertEquals("Reason", habit.getReason());

        habit.setReason("UpdatedReason");
        assertEquals("UpdatedReason", habit.getReason());
    }

    @Test
    void setReasonExceptionTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Habit habit = new Habit("Title","", Calendar.getInstance().getTime(), plan);
        assertThrows(IllegalArgumentException.class,
                () -> {habit.setReason("abcdefghi jklmnopqr stuvwxyz ABC");});
    }

    @Test
    void getStartDateTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Date startDate = Calendar.getInstance().getTime();
        Habit habit = new Habit("Title","", startDate, plan);
        assertEquals(startDate, habit.getStartDate());
    }

    @Test
    void setStartDateTest() {
        ArrayList<Boolean> plan = new ArrayList<>(Arrays.asList(new Boolean[7]));
        Collections.fill(plan, true);
        Date startDate = Calendar.getInstance().getTime();
        Habit habit = new Habit("Title","", startDate, plan);
        assertEquals(startDate, habit.getStartDate());

        Date updateDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        habit.setStartDate(updateDate);
        assertNotEquals(startDate, habit.getStartDate());
        assertEquals(updateDate, habit.getStartDate());
    }
}
