/*
 * AccountActivityTest
 *
 * AccountActivityTest class contains Robotium Intent tests for the User Profile Acitivity.
 * Specifically it tests that a user can create a profile, log into the application and open the
 * AccountActivity. It also tests that the logout button functions correctly and that the
 * update account button correctly switches the activity too. The created profile is always deleted
 * after every test.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 4, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.app.Activity;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for AccountActivity. All the UI tests are written here. Robotium test framework is
 * used
 *
 * @author Mattheas Jamieson
 * @see LoginActivity
 * @version 1.0
 * @since 1.0
 */
public class AccountActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() throws Exception{
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
     * Check if the userProfileActivity can be opened successfully after login.
     */
    @Test
    public void checkCorrectActivity () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheasq@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        // Log into the app with newly created user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasq@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(2000);

        // Assert current activity is AccountActivity
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);

        // delete user from database
        solo.clickOnButton("Update Account");
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }

    /**
     * Check if the Logout button works in userProfileActivity.
     */
    @Test
    public void checkLogoutButton () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheasqa@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        // Log into the app with newly created user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasqa@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(2000);

        // Assert current activity is AccountActivity
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);

        // sign user out of application
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // sign back in then delete created user
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasqa@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.clickOnButton("Update Account");
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }

    /**
     * Check if the Update account button works in userProfileActivity.
     */
    @Test
    public void checkUpdateAccountButton () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheasp@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        // Log into the app with newly created user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasp@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(2000);

        // Tap Update Account button and check activity switches
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", AccountActivity.class);
        solo.clickOnButton("Update Account");
        solo.assertCurrentActivity("Wrong Activity", UpdateAccountActivity.class);

        // delete user from database
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }
}
