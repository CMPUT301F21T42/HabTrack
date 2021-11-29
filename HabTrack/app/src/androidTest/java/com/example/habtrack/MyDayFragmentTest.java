package com.example.habtrack;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
/**These tests are written for the following user being logged in:
 * email: qwerty@gmail.ca
 * password: zxcvbnm12
 *(otherwise they will fail)
 */

public class MyDayFragmentTest {
    Solo solo;
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
    public void mydaytest(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View myday = solo.getView(R.id.myday_checkBox);
        solo.clickOnCheckBox(0);
        //EditText t1 = solo.getText(0);
        assertEquals(true,solo.isCheckBoxChecked(0));


    }

}
