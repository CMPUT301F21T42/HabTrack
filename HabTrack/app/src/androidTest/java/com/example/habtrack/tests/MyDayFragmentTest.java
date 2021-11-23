package com.example.habtrack.tests;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.habtrack.UserLoginStatusActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import com.example.habtrack.R;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyDayFragmentTest {
    Solo solo;
    @Rule
    public ActivityTestRule<UserLoginStatusActivity> rule = new ActivityTestRule<>(UserLoginStatusActivity.class);

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void mydaytest(){
        solo.clickOnCheckBox(0);
        onView(withId(R.id.myday_checkBox)).perform(click());
    }

}
