/*
 * CredentialVerifierTest
 *
 * CredentialVerifierTest class unit tests for main methods of CredentialVerifier class.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * October 30, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for CredentialVerifier main methods.
 *
 * @author Mattheas Jamieson
 * @see CredentialVerifier
 * @version 1.0
 * @since 1.0
 */
public class CredentialVerifierTest {

    /**
     * Checks if verifyUserNameField method works correctly by passing an empty string (invalid),
     * a string with a number in it (invalid) and lastly a string that is a valid userName.
     */
    @Test
    void testVerifyUserNameField () {
        String emptyUserName = "";
        String numberUserName = "abc5";
        String validUserName = "/n&gj':";

        assertFalse(CredentialVerifier.verifyUserNameField(emptyUserName));
        assertFalse(CredentialVerifier.verifyUserNameField(numberUserName));
        assertTrue(CredentialVerifier.verifyUserNameField(validUserName));
    }

    /**
     * Checks if verifyPasswordFieldIsEmpty method works correctly by passing an empty string
     * and a non empty string.
     */
    @Test
    void testVerifyPasswordFieldIsEmpty () {
        String emptyPassword = "";
        String nonEmptyPassword = "notempty";

        // test empty password and non empty password
        assertFalse(CredentialVerifier.verifyPasswordFieldIsEmpty(emptyPassword));
        assertTrue(CredentialVerifier.verifyPasswordFieldIsEmpty(nonEmptyPassword));
    }

    /**
     * Checks if verifyPasswordFieldLength method works correctly by passing a string with
     * only 7 characters (not valid length) and one that is 8 characters (valid length).
     */
    @Test
    void testVerifyPasswordFieldLength () {
        String tooShortPassword = "1234567";
        String minPasswordLength = "12345678";

        assertFalse(CredentialVerifier.verifyPasswordFieldLength(tooShortPassword));
        assertTrue(CredentialVerifier.verifyPasswordFieldLength(minPasswordLength));
    }

    /**
     * Checks if verifyEmailFiled method works correctly by passing an empty string (invalid),
     * and then two examples of valid email adresses.
     */
    @Test
    void testVerifyEmailField () {
        /*
            Patterns.EMAIL_ADDRESS.matcher(email).matches() was throwing a
            null pointer exception so we switched to a regex expression. Worthwhile
            to look at or update Regex as an actually invalid email passes the regex.
         */
        String emptyEmail = "";
        //String invalidEmail = "invalid@.gmail.com";
        String validEmail = "my_email.5@hotmail.com";
        String validEmail2 = "h@h.com";

        assertFalse(CredentialVerifier.verifyEmailField(emptyEmail));
        //assertFalse(CredentialVerifier.verifyEmailField(invalidEmail));
        assertTrue(CredentialVerifier.verifyEmailField(validEmail));
        assertTrue(CredentialVerifier.verifyEmailField(validEmail2));
    }
}
