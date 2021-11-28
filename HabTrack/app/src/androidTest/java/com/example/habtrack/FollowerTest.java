/*
 * FollowerTest
 *
 * FollowerTest class contains Robotium (or possibly espresso) Intent tests for testing story point US 05.01.01,
 * US 05.02.01 and US 05.03.01. Specifically that is testing sending a follow request, granting/ denying a follow
 * request and viewing the habits of followed users respectfully.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 27, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

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
import static org.junit.Assert.assertTrue;

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

/**
 * Test class for requirements US 05.01.01 through 05.03.01. All the UI tests are written here.
 * Robotium test framework is used alongside espresso
 *
 * @author Mattheas Jamieson
 * @see LoginActivity
 * @see MainActivity
 * @see SearchUsersActivity
 * @version 1.0
 * @since 1.0
 */
@LargeTest
public class FollowerTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Creates two users that will interact by sending follow requests, approving or denying said
     * follow requests and viewing the public habits of the other user.
     */
    @Test
    public void createTestUsers() {
        // create two test users
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "testuserfriend");
        solo.enterText((EditText) solo.getView(R.id.new_email), "friend@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "testuserfriendtwo");
        solo.enterText((EditText) solo.getView(R.id.new_email), "friendtwo@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        // sign in with first test user using robotium
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "friend@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // using espresso add habit to signed in user
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.title_editText)).perform(typeText("Study"));
        onView(withId(R.id.reason_editText)).perform(typeText("earn money"), closeSoftKeyboard());
        onView(withId(R.id.monday_checkBox)).perform(click());
        onView(withId(R.id.wednesday_checkBox)).perform(click());
        onView(withText("OK")).perform(click());

        // sign out first test user
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Signs into second user, search up first user and send a follow request using robotium
     * and espresso.
     */
    @Test
    public void checkSendFollowRequest() {
        // sign in with second test user using robotium
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "friendtwo@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // search for first user and send follow request
        onView(withId(R.id.nav_friends)).perform(click());
        solo.enterText((EditText) solo.getView(R.id.searchFriendInput), "testuserfriend");
        solo.clickOnButton("Search");
        solo.clickOnView(solo.getView(R.id.request_to_follow));

        // sign out user two from app
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Signs into first user, grant user two's follow request. Then search users own profile and ensure
     * that it has in fact no user followers. Then switch accounts and ensure public habit is visible.
     */
    @Test
    public void checkGrantFollowRequest() {
        // ASSUMPTION THAT OTHER USERS HABIT IS PUBLIC?????

        // sign in with first test user using robotium
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "friend@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check for follow request and accept it
        solo.clickOnImageButton(0);
        solo.clickOnText("Follower");
        assertTrue(solo.searchText("testuserfriendtwo"));

        // check your follower number is one

        // switch account and check that public habit is visible

    }

    /**
     * Signs into first user, deny user two's follow request. Then search users own profile and ensure
     * that it has in fact no user followers.
     */
    @Test
    public void checkDenyFollowRequest() {
        // sign in with first test user using robotium
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "friend@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check for follow request and deny
        solo.clickOnImageButton(0);
        solo.clickOnText("Follower");
        assertTrue(solo.searchText("testuserfriendtwo"));

        // deny follow request

        // check your follower number is zero


    }


}
