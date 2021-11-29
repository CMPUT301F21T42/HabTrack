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
    public void viewEditHabitEventTest() {
        //solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        View v = solo.getView(R.id.fab);
        solo.clickOnView(v);// clicking on the plus button to add a new habit.

        //adds all the data for the habit
        solo.enterText((EditText) solo.getView(R.id.title_editText), "Study");
        solo.waitForText("Study", 1, 1000);

        solo.enterText((EditText) solo.getView(R.id.reason_editText), "To gain knowledge");
        solo.waitForText("To gain knowledge", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.year_editText));
        solo.enterText((EditText) solo.getView(R.id.year_editText), "2021");
        solo.waitForText("2021", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.month_editText));
        solo.enterText((EditText) solo.getView(R.id.month_editText), "11");
        solo.waitForText("12", 1, 1000);

        solo.clearEditText((EditText) solo.getView(R.id.day_editText));
        solo.enterText((EditText) solo.getView(R.id.day_editText), "10");
        solo.waitForText("10", 1, 1000);

        View switch_button = solo.getView(R.id.ispublic_switch);
        solo.clickOnView(switch_button);

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
        solo.waitForActivity("Waiting", 10);

        solo.clearEditText((EditText) solo.getView(R.id.habit_event_comment));
        solo.enterText((EditText) solo.getView(R.id.habit_event_comment), "Done for today");

        solo.waitForText("Done", 1, 100);
        solo.clickOnText("Add");

        solo.clickOnText("Events");
        solo.waitForActivity("Waiting", 100);

        solo.clickOnText("Study" + " Completed");

        solo.clearEditText((EditText) solo.getView(R.id.habit_event_comment));
        solo.enterText((EditText) solo.getView(R.id.habit_event_comment), "Testing in progress");
        solo.waitForText("Testing in progress", 1, 100);

        solo.clickOnText("SAVE");

    }



}