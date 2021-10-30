package com.example.habtrack;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserInfoTest {

    private UserInfo mockUserInfo () {
        return new UserInfo("userOne", "email@gmail.com", "password1");
    }

    @Test
    public void testGetUserName () {
        UserInfo user = mockUserInfo();
        assertEquals("userOne", user.getUserName());
    }

    @Test
    public void testSetUserName () {
        UserInfo user = mockUserInfo();
        assertEquals("userOne", user.getUserName());
        user.setUserName("newUserName");
        assertEquals("newUserName", user.getUserName());
    }

    @Test
    public void testGetEmail () {
        UserInfo user = mockUserInfo();
        assertEquals("email@gmail.com", user.getEmail());
    }

    @Test
    public void testSetEmail () {
        UserInfo user = mockUserInfo();
        assertEquals("email@gmail.com", user.getEmail());
        user.setEmail("newEmail@gmail.com");
        assertEquals("newEmail@gmail.com", user.getEmail());
    }

    @Test
    public void testGetPassword () {
        UserInfo user = mockUserInfo();
        assertEquals("password1", user.getPassword());
    }

    @Test
    public void testSetPassword () {
        UserInfo user = mockUserInfo();
        assertEquals("password1", user.getPassword());
        user.setPassword("password2");
        assertEquals("password2", user.getPassword());
    }
}
