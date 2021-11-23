package com.example.habtrack.tests;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.habtrack.R;
import com.example.habtrack.UserLoginStatusActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class addHabit1test {

    @Rule
    public ActivityTestRule<UserLoginStatusActivity> mActivityTestRule = new ActivityTestRule<>(UserLoginStatusActivity.class);

    @Test
    public void addHabit1Test() {

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.title_editText)).perform(typeText("Study"));
        onView(withId(R.id.reason_editText)).perform(typeText("earn money"), closeSoftKeyboard());
        onView(withId(R.id.monday_checkBox)).perform(click());
        onView(withId(R.id.wednesday_checkBox)).perform(click());
        onView(withText("OK")).perform(click());
    }
}