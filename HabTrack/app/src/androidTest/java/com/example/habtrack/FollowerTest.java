/*
 * FollowerTest
 *
 * FollowerTest class contains Robotium mixed with Espresso Intent tests for testing story point US 05.01.01,
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
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import androidx.test.filters.LargeTest;

/**
 * Test class for requirements US 05.01.01 through 05.03.01. All the UI tests are written here.
 * Robotium test framework is used alongside Espresso.
 *
 * @author Mattheas Jamieson
 * @see LoginActivity
 * @see MainActivity
 * @see SearchUsersActivity
 * @version 1.0
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class FollowerTest {
    private Solo solo;
    private final String testUserOneUserName = "robotestuseroneeee";
    private final String testUserTwoUserName = "robotestusertwoooo";
    private final String testUserOneEmail = "robotestuseroneeee@email.com";
    private final String testUserTwoEmail = "robotestusertwoooo@email.com";
    private final String testUserPassword = "password";

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
    public void A_checkCreateTestUsers() {
        // create two test users
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), testUserOneUserName);
        solo.enterText((EditText) solo.getView(R.id.new_email), testUserOneEmail);
        solo.enterText((EditText) solo.getView(R.id.new_password), testUserPassword);
        solo.enterText((EditText) solo.getView(R.id.confirm_password), testUserPassword);
        solo.clickOnButton("Sign up");

        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), testUserTwoUserName);
        solo.enterText((EditText) solo.getView(R.id.new_email), testUserTwoEmail);
        solo.enterText((EditText) solo.getView(R.id.new_password), testUserPassword);
        solo.enterText((EditText) solo.getView(R.id.confirm_password), testUserPassword);
        solo.clickOnButton("Sign up");

        // sign in with first test user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), testUserOneEmail);
        solo.enterText((EditText) solo.getView(R.id.password), testUserPassword);
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // using espresso add habit to first test user
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.title_editText)).perform(typeText("Study"));
        onView(withId(R.id.reason_editText)).perform(typeText("earn money"), closeSoftKeyboard());
        onView(withId(R.id.ispublic_switch)).perform(click());
        onView(withId(R.id.monday_checkBox)).perform(click());
        onView(withId(R.id.wednesday_checkBox)).perform(click());
        onView(withText("OK")).perform(click());

        // sign out first test user
        solo.clickOnImageButton(0);
        solo.clickOnText("Account");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Signs into second user, search up first user and send a follow request using robotium.
     */
    @Test
    public void B_checkSendFollowRequest() {
        // sign in with second test user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), testUserTwoEmail);
        solo.enterText((EditText) solo.getView(R.id.password), testUserPassword);
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // search for first user and send follow request
        solo.clickOnImageButton(0);
        solo.clickOnText("Explore");
        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchFriendInput), testUserOneUserName);
        solo.clickOnButton("Search");
        solo.clearEditText((EditText) solo.getView(R.id.searchFriendInput));
        solo.clickOnText(testUserOneUserName);
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnButton("Follow");

        // sign out of current user
        solo.goBack();
        solo.goBack();
        solo.clickOnText("Account");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Signs into first user, deny user two's follow request. Then search users own profile and ensure
     * that it has in fact no user followers.
     */
    @Test
    public void C_checkDenyFollowRequest() {
        // sign in with first test user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), testUserOneEmail);
        solo.enterText((EditText) solo.getView(R.id.password), testUserPassword);
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check for follow request and deny
        solo.clickOnImageButton(0);
        solo.clickOnText("Notifications");
        assertTrue(solo.searchText(testUserTwoUserName));
        solo.clickOnText("Deny");
        solo.goBack();

        // sign out of current user
        solo.clickOnText("Account");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Signs into first user, grant user two's follow request. Then search users own profile and ensure
     * that it has in fact no user followers. Then switch accounts and ensure public habit is visible.
     */
    @Test
    public void D_checkGrantFollowRequest() {
        // resend follow request from user two to user one
        B_checkSendFollowRequest();

        // sign in with first test user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), testUserOneEmail);
        solo.enterText((EditText) solo.getView(R.id.password), testUserPassword);
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // check for follow request and accept it
        solo.clickOnImageButton(0);
        solo.clickOnText("Notifications");
        assertTrue(solo.searchText(testUserTwoUserName));
        solo.clickOnText("Accept");
        solo.goBack();

        // sign out of first user
        solo.clickOnText("Account");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // sign in to second user and check first users public habit is visible
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), testUserTwoEmail);
        solo.enterText((EditText) solo.getView(R.id.password), testUserPassword);
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Check public habit is viewable
        onView(withId(R.id.nav_friends)).perform(click());
        solo.clickOnText(testUserOneUserName);
        assertTrue(solo.searchText("Study"));

        // sign out user two
        solo.clickOnImageButton(0);
        solo.clickOnText("Account");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
}
