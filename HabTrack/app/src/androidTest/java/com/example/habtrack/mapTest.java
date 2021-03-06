package com.example.habtrack;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**These tests are written for the following user being logged in:
 * email: qwerty@gmail.ca
 * password: zxcvbnm12
 *(otherwise they will fail)
 * This test navigates to the manage tab.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class mapTest {
    Solo solo;
    @Rule
    public ActivityTestRule<UserLoginStatusActivity> rule = new ActivityTestRule<>(UserLoginStatusActivity.class);

    @Before
    public void setup(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void mapTest(){
        solo.clickOnCheckBox(0);
        //onView(withId(R.id.myday_checkBox)).perform(click());
        onView(withId(R.id.habit_event_comment)).perform(typeText("It was fun"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.imageButton)).perform(click());
        onView(withId(R.id.mapText)).perform(typeText("University of Alberta"));
        onView(withId(R.id.search_button_maps)).perform(click(),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirm_button_maps)).perform(click());
        onView(withText("Add")).perform(click());
    }
}
