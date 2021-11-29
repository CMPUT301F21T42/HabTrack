package com.example.habtrack;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * These tests create a new habit called "Dance",
 * adds a reason to do it, adds start date and the
 * days to do it.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class addHabitFragmentTest {

    @Rule
    public ActivityTestRule<UserLoginStatusActivity> mActivityTestRule = new ActivityTestRule<>(UserLoginStatusActivity.class);


    @Test
    public void addHabit1Test() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.title_editText)).perform(typeText("Dance"));
        onView(withId(R.id.reason_editText)).perform(typeText("Fun"), closeSoftKeyboard());
        onView(withId(R.id.monday_checkBox)).perform(click());
        onView(withId(R.id.wednesday_checkBox)).perform(click());
        onView(withText("OK")).perform(click());
        }




    }
