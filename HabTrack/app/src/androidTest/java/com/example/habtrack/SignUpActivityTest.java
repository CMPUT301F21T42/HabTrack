/*
 * SignUpActivityTest
 *
 * SignUpActivityTest class contains Robotium Intent tests for the Sign Up Activity.
 * Specifically it tests that a user can create a profile, log into the application
 * and then delete said profile.
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
 * Test class for SignUpActivity. All the UI tests are written here. Robotium test framework is
 * used.
 *
 * @author Mattheas Jamieson
 * @see SignUpActivity
 * @version 1.0
 * @since 1.0
 */
public class SignUpActivityTest {
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
     * Check ifs a user can be created through the sign up page, then if the user
     * can log in to the app, and lastly deletes the user.
     */
    @Test
    public void checkSignUpUser () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheas@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");

        // Log into the app with newly created user
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.email), "mattheas@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(8000);

        // Delete user from database
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.clickOnButton("Update Account");
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }
}
