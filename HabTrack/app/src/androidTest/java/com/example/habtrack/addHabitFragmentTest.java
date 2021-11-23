package com.example.habtrack;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * These tests create a new habit called "Dance",
 * adds a reason to do it, adds start date and the
 * days to do it.
 */
public class addHabitFragmentTest {
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
    public void addhabitfrag() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
        //checks if the monday button is checked off
        assertEquals(true,solo.isCheckBoxChecked(1));
        // This will not function properly because robotium and firebase give
        //error as it cannot save the data. And the purpose of "Ok" is to save it to
        // the database/
        //solo.clickOnText("Ok");

    }
}