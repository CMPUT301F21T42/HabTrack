package com.example.habtrack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HabitEventsTest {

    @Test
    void testHabitEventConstrutor() {
        String Title = "Study";
        String Comment = "To gain knowledge";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(Title, Comment, false, false, timeStamp);

        assertTrue(Title.equals(testObject.getTitle()));
        assertTrue(Comment.equals(testObject.getComment()));
        assertTrue(false == testObject.getPhoto());
        assertTrue(false == testObject.getLocation());
        assertTrue(timeStamp.equals(testObject.getTimeStamp()));
        assertTrue(HabitEventID.equals(testObject.getHabitEventID()));
    }

    @Test
    void testHabitEventTitle() {
        String Title = "Study";
        String Comment = "To gain knowledge";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(Title, Comment, false, false, timeStamp);

        assertTrue(Title.equals(testObject.getTitle()));

        Title = "Study Software Engineering";
        testObject.setTitle(Title);

        assertTrue(Title.equals(testObject.getTitle()));
    }

    @Test
    void testHabitEventComment() {
        String Title = "Study";
        String Comment = "To gain knowledge";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(Title, Comment, false, false, timeStamp);

        assertTrue(Comment.equals(testObject.getComment()));

        Comment = "To learn new things";
        testObject.setComment(Comment);

        assertTrue(Comment.equals(testObject.getComment()));
    }

    @Test
    void testHabitEventtimeStamp() {
        String Title = "Study";
        String Comment = "To gain knowledge";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(Title, Comment, false, false, timeStamp);

        assertTrue(timeStamp.equals(testObject.getTimeStamp()));
    }

    @Test
    void testHabitEventHabitEventID() {
        String Title = "Study";
        String Comment = "To gain knowledge";
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(Title, Comment, false, false, timeStamp);

        assertTrue(HabitEventID.equals(testObject.getHabitEventID()));
    }

}
