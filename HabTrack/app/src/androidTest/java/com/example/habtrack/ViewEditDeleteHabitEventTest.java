package com.example.habtrack;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habtrack.ui.myday.MydayFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewEditDeleteHabitEventTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start(){
        Activity activity = rule.getActivity();
    }

    @Test
    public void addfragTest() {
        //solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        View v = solo.getView(R.id.fab);
        solo.clickOnView(v);// clicking on the plus button to add a new habit.

        //adds all the data for the habit
        solo.enterText((EditText) solo.getView(R.id.title_editText), "Dance");
        solo.waitForText("Dance", 1, 1000);

        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Like doing it");
        solo.waitForText("Like doing it", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.year_editText));
        solo.enterText((EditText) solo.getView(R.id.year_editText), "2021");
        solo.waitForText("2021", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.month_editText));
        solo.enterText((EditText) solo.getView(R.id.month_editText), "12");
        solo.waitForText("12", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.day_editText));
        solo.enterText((EditText) solo.getView(R.id.day_editText), "10");
        solo.waitForText("10", 1, 1000);

        View monday_button = solo.getView(R.id.monday_checkBox);
        solo.clickOnView(monday_button);

        View tuesday_button = solo.getView(R.id.tuesday_checkBox);
        solo.clickOnView(tuesday_button);

        View wednesday_button = solo.getView(R.id.wednesday_checkBox);
        solo.clickOnView(wednesday_button);

        View thur_button = solo.getView(R.id.thursday_checkBox);
        solo.clickOnView(thur_button);


        View fri_button = solo.getView(R.id.friday_checkBox);
        solo.clickOnView(fri_button);


        View monday_button1 = solo.getView(R.id.saturday_checkBox);
        solo.clickOnView(monday_button1);

        View monday_button2 = solo.getView(R.id.sunday_checkBox);
        solo.clickOnView(monday_button2);

        solo.clickOnText("OK");

        solo.clickOnCheckBox(0);
        //solo.waitForActivity("Waiting", 10);

        solo.clearEditText((EditText) solo.getView(R.id.habit_event_comment));
        solo.enterText((EditText) solo.getView(R.id.habit_event_comment), "Done");

        solo.waitForText("Done", 1, 1000);
        solo.clickOnText("Add");

    }



}
//package com.example.habtrack;
//
//import android.app.Activity;
//import android.widget.EditText;
//
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.rule.ActivityTestRule;
//
//import com.example.habtrack.ui.events.ViewEditDeleteHabitEvent;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.robotium.solo.Solo;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//public class ViewEditDeleteHabitEventTest {
//    private Solo solo;
//
//
//    @Rule
//    public ActivityTestRule<MainActivity> rule =
//            new ActivityTestRule<MainActivity>(MainActivity.class, true, true);
//
//    @Before
//    public void setup() {
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//    }
//
//    @Test
//    public void start() {
//        Activity activity = rule.getActivity();
//    }
//
//    @Test
//    public void addHabitTest() {
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        solo.clickOnButton(R.id.fab);
//        solo.enterText((EditText) solo.getView(R.id.habit_title), "Study");
//        solo.enterText((EditText) solo.getView(R.id.reason_editText), "To gain knowledge");
//        solo.clickOnCheckBox(R.id.monday_checkBox);
//        solo.clickOnCheckBox(R.id.tuesday_checkBox);
//        solo.clickOnCheckBox(R.id.wednesday_checkBox);
//        solo.clickOnCheckBox(R.id.thursday_checkBox);
//        solo.clickOnCheckBox(R.id.friday_checkBox);
//        solo.clickOnCheckBox(R.id.saturday_checkBox);
//        solo.clickOnCheckBox(R.id.sunday_checkBox);
//        solo.clickOnButton("OK");
//
////        onView(withId(R.id.fab)).perform(click());
////        onView(withId(R.id.title_editText)).perform(typeText("Dance"));
////        onView(withId(R.id.reason_editText)).perform(typeText("Fun"), closeSoftKeyboard());
////        onView(withId(R.id.monday_checkBox)).perform(click());
////        onView(withId(R.id.wednesday_checkBox)).perform(click());
////        onView(withText("OK")).perform(click());
//    }
//
//
//}
