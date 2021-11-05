/*
 * LoginActivityTest
 *
 * LoginActivityTest class contains Robotium Intent tests for the Login Activity.
 * Specifically it tests the Login and Signup Button.
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
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for LoginActivity. All the UI tests are written here. Robotium test framework is
 * used
 *
 * @author Mattheas Jamieson
 * @see LoginActivity
 * @version 1.0
 * @since 1.0
 */
public class LoginActivityTest {

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
     * Check if login button along with a email/ password that are stored in the DB
     * will switch current activity from LoginActivity to MainActivity.
     */
    @Test
    public void checkLoginButton () {
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        //Get view for EditText and enter a email
        solo.enterText((EditText) solo.getView(R.id.email), "mattheas@gmail.com");

        //Get view for EditText and enter a password
        solo.enterText((EditText) solo.getView(R.id.password), "mattheas");

        // select Log in button
        solo.clickOnButton("Log in");

        // login successful if current activity is MainActivity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Check if the SignUp button (textview) will switch current activity from LoginActivity
     * to SignUpActivity.
     */
    @Test
    public void checkSignUpButton () {
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // select Log in button
        solo.clickOnText("Sign up");

        // login successful if current activity is MainActivity
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }
}


