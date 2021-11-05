/*
 * UpdateAccountActivityTest
 *
 * UpdateAccountActivityTest class contains Robotium Intent tests for the Update Account Acitivty.
 * Specifically it tests that a user can create a profile, log into the application and update
 * the username of their profile. It also tests if they can update the email of their account in
 * a similar way. For each test a new profile is created then deleted after.
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
import static org.junit.Assert.assertTrue;

/**
 * Test class for UpdateAccountActivity. All the UI tests are written here. Robotium test framework
 * is used.
 *
 * @author Mattheas Jamieson
 * @see UpdateAccountActivity
 * @version 1.0
 * @since 1.0
 */
public class UpdateAccountActivityTest {
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
     * can log in to the app, then if the user can update their username and lastly
     * deletes the user.
     */
    @Test
    public void checkUpdateUserName () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheasjqf@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Log into the app with newly created user
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasjqf@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(2000);

        // Update userName
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnButton("Update Account");
        solo.clearEditText((EditText) solo.getView(R.id.update_user_name));
        solo.enterText((EditText) solo.getView(R.id.update_user_name), "matt_updated");
        solo.clickOnButton("Save changes");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);

        // assert that userName is updated
        assertTrue(solo.searchText("matt_updated"));

        // delete user from database
        solo.clickOnButton("Update Account");
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }

    /**
     * Check ifs a user can be created through the sign up page, then if the user
     * can log in to the app, then if the user can update their email and lastly
     * deletes the user.
     */
    @Test
    public void checkUpdateUserEmail () {
        FirebaseAuth.getInstance().signOut();

        // Create a new user through the signup page
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnText("Sign up");
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.enterText((EditText) solo.getView(R.id.new_username), "matt");
        solo.enterText((EditText) solo.getView(R.id.new_email), "mattheasjwf@test.com");
        solo.enterText((EditText) solo.getView(R.id.new_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.confirm_password), "testpassword");
        solo.clickOnButton("Sign up");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Log into the app with newly created user
        solo.enterText((EditText) solo.getView(R.id.email), "mattheasjwf@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testpassword");
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // delay
        solo.sleep(2000);

        // Update email
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnButton("Update Account");
        solo.clearEditText((EditText) solo.getView(R.id.update_email));
        solo.enterText((EditText) solo.getView(R.id.update_email), "mattj@updated.com");
        solo.clickOnButton("Save changes");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);

        // assert that email is updated
        assertTrue(solo.searchText("matt@updated.com"));

        // delete user from database
        solo.clickOnButton("Update Account");
        solo.clickOnButton("Delete Account");
        solo.clickOnText("OK");
    }
}
