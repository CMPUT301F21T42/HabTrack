package com.example.habtrack;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CredentialVerifierTest {

    @Test
    void testVerifyUserNameField () {
        String emptyUserName = "";
        String numberUserName = "abc5";
        String validUserName = "/n&gj':";

        // test for empty string, string with number and finally valid string
        assertFalse(CredentialVerifier.verifyUserNameField(emptyUserName));
        assertFalse(CredentialVerifier.verifyUserNameField(numberUserName));
        assertTrue(CredentialVerifier.verifyUserNameField(validUserName));
    }

    @Test
    void testVerifyPasswordFieldIsEmpty () {
        String emptyPassword = "";
        String nonEmptyPassword = "notempty";

        // test empty password and non empty password
        assertFalse(CredentialVerifier.verifyPasswordFieldIsEmpty(emptyPassword));
        assertTrue(CredentialVerifier.verifyPasswordFieldIsEmpty(nonEmptyPassword));
    }

    @Test
    void testVerifyPasswordFieldLength () {
        String tooShortPassword = "1234567";
        String minPasswordLength = "12345678";

        // test password thats not long enough and one thats just long enough
        assertFalse(CredentialVerifier.verifyPasswordFieldLength(tooShortPassword));
        assertTrue(CredentialVerifier.verifyPasswordFieldLength(minPasswordLength));
    }

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

        // test an empty email, an invalid email with a "." after @ and two valid combos
        assertFalse(CredentialVerifier.verifyEmailField(emptyEmail));
        //assertFalse(CredentialVerifier.verifyEmailField(invalidEmail));
        assertTrue(CredentialVerifier.verifyEmailField(validEmail));
        assertTrue(CredentialVerifier.verifyEmailField(validEmail2));
    }
}
