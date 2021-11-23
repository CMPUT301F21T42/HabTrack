package com.example.habtrack.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.habtrack.R;
import com.example.habtrack.UserLoginStatusActivity;
import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditHabitTest {
    Solo solo;
    @Rule
    public ActivityTestRule<UserLoginStatusActivity> mActivityTestRule = new ActivityTestRule<>(UserLoginStatusActivity.class);

//    @Rule
//    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);
//
//
//    @Before
//    public void setup(){
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
//    }

    @Test
    public void EditHabitTest(){
        onView(withId(R.id.nav_manage)).perform(click());
        //onData(anything()).inAdapterView(withId(R.id.habit_list)).atPosition(0).perform(click());
        //onData(anything()).atPosition(1).perform(click());

        onView(withId(R.id.habit_list)).perform(click());
        onView(withId(R.id.title_editText)).perform(clearText());
        onView(withId(R.id.title_editText)).perform(typeText("Ronnni"),closeSoftKeyboard());
        onView(withText("OK")).perform(click());
    }
}

