/*
 * UserInfoTest
 *
 * This source file performs unit tests all methods of the UserInfo class.
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
 * Contains unit tests for all UserInfo methods.
 *
 * @author Mattheas Jamieson
 * @see UserInfo
 * @version 1.0
 * @since 1.0
 */
public class UserInfoTest {

    /**
     * This method creates and returns an instance of the UserInfo class with specific
     * values that are used for the unit tests.
     *
     * @return   Returns a instance of the UserInfo classes with specific test values for
     *           attributes.
     */
    private UserInfo mockUserInfo () {
        return new UserInfo("userOne", "email@gmail.com", "password1");
    }

    /**
     * Checks if the getUserName method in the UserInfo class returns the correct userName
     * for the given instance of the class.
     */
    @Test
    public void testGetUserName () {
        UserInfo user = mockUserInfo();
        assertEquals("userOne", user.getUserName());
    }

    /**
     * Checks if the setUserName method in the UserInfo class updates the userName correctly
     * when it is called.
     */
    @Test
    public void testSetUserName () {
        UserInfo user = mockUserInfo();
        assertEquals("userOne", user.getUserName());
        user.setUserName("newUserName");
        assertEquals("newUserName", user.getUserName());
    }

    /**
     * Checks if the getEmail method in the UserInfo class returns the correct email
     * for the given instance of the class.
     */
    @Test
    public void testGetEmail () {
        UserInfo user = mockUserInfo();
        assertEquals("email@gmail.com", user.getEmail());
    }

    /**
     * Checks if the setEmail method in the UserInfo class updates the email correctly
     * when it is called.
     */
    @Test
    public void testSetEmail () {
        UserInfo user = mockUserInfo();
        assertEquals("email@gmail.com", user.getEmail());
        user.setEmail("newEmail@gmail.com");
        assertEquals("newEmail@gmail.com", user.getEmail());
    }

    /**
     * Checks if the getPassword method in the UserInfo class returns the correct password
     * for the given instance of the class.
     */
    @Test
    public void testGetPassword () {
        UserInfo user = mockUserInfo();
        assertEquals("password1", user.getPassword());
    }

    /**
     * Checks if the setPassword method in the UserInfo class updates the password correctly
     * when it is called.
     */
    @Test
    public void testSetPassword () {
        UserInfo user = mockUserInfo();
        assertEquals("password1", user.getPassword());
        user.setPassword("password2");
        assertEquals("password2", user.getPassword());
    }
}
