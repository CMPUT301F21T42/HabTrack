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

public class EditFragmentTest {
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
    public void additiontest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View v = solo.getView(R.id.nav_manage);
        solo.clickOnView(v, true);
        solo.waitForActivity("Waiting", 10);
        //solo.assertCurrentActivity("Wrong Activity", ManageFragment.class);
        solo.clickOnText("study",1);
        solo.clearEditText((EditText) solo.getView(R.id.title_editText));
        solo.enterText((EditText) solo.getView(R.id.title_editText), "Studying");
        solo.waitForText("Studying", 1, 2000);
        View sunday_button = solo.getView(R.id.sunday_checkBox);
        solo.clickOnView(sunday_button);
        //solo.clickOnCheckBox(View solo.getView(R.id.monday_checkBox));
        solo.clickOnCheckBox(0);
        assertEquals(solo.isCheckBoxChecked(0),true);
        solo.clickOnCheckBox(4);
        solo.clickOnText("OK",1);}
}
