package com.example.habtrack;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**These tests are written for the user having
 * email: raunak@gmail.ca
 * password: Qwerty12345
 *(otherwise they will fail)
 * This test edit an already existing
 * habit with the name study
 *
 */
public class EditFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void additiontest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View v = solo.getView(R.id.nav_manage);// goes to manage tab
        solo.clickOnView(v, true);
        //clicking on the study habit to make changes to it
        solo.clickOnText("study",1);
        solo.clearEditText((EditText) solo.getView(R.id.title_editText));
        solo.enterText((EditText) solo.getView(R.id.title_editText), "studying");
        solo.waitForText("studying", 1, 2000);

        solo.clickOnCheckBox(0);
        assertEquals(true,solo.isCheckBoxChecked(0));
        solo.clickOnText("OK",1);
    }
    @After
    public void resetmethod(){
        //resets the data back to original configuration
        View v = solo.getView(R.id.nav_manage);
        solo.clickOnView(v, true);
        solo.clickOnText("studying",1);
        solo.clearEditText((EditText) solo.getView(R.id.title_editText));
        solo.enterText((EditText) solo.getView(R.id.title_editText), "study");
        solo.clickOnCheckBox(0);
        solo.clickOnText("OK",1);

    };
}



