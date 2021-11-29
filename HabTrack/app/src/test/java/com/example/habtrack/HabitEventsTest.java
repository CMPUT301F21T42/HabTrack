package com.example.habtrack;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HabitEventsTest {

    @Test
    void testHabitEventConstrutor() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(2);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = habitID + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(habitID.equals(testObject.getHabitId()));
        assertTrue(Comment.equals(testObject.getComment()));
        assertTrue(location.get(0) == testObject.getLocation().get(0));
        assertTrue(location.get(1) == testObject.getLocation().get(1));
        assertTrue(photo.equals(testObject.getPhoto()));
        assertTrue(timeStamp.equals(testObject.getTimeStamp()));
        assertTrue(HabitEventID.equals(testObject.getHabitEventID()));
    }

    @Test
    void testHabitEventHabitID() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = habitID + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(habitID.equals(testObject.getHabitId()));
    }

    @Test
    void testHabitEventComment() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(Comment.equals(testObject.getComment()));

        Comment = "To learn new things";
        testObject.setComment(Comment);

        assertTrue(Comment.equals(testObject.getComment()));
    }

    @Test
    void testHabitEventtimeStamp() {

        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);


        assertTrue(timeStamp.equals(testObject.getTimeStamp()));
    }

    @Test
    void testHabitEventHabitEventID() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = habitID + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(HabitEventID.equals(testObject.getHabitEventID()));
    }

    @Test
    void testHabitEventPhoto() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(photo.equals(testObject.getPhoto()));
        String newPhoto = "New Photo";
        testObject.setPhoto(newPhoto);
    }

    @Test
    void testHabitEventLocation() {
        String habitID = "HabitID";
        String Title = "Study";
        String Comment = "To gain knowledge";
        ArrayList<Double> location = new ArrayList<>(5);
        location.add(52.123);
        location.add(-103.1234234324234);

        // Since we are storing photo as a string testing for a string functionality
        String photo = "Default Photo";

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventID = Title + timeStamp;

        HabitEvents testObject = new HabitEvents(habitID, Comment, photo, location, timeStamp);

        assertTrue(testObject.getLocation().get(0) == location.get(0));
        assertTrue(testObject.getLocation().get(1) == location.get(1));

    }

}
