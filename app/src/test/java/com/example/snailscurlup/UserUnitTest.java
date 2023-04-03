package com.example.snailscurlup;

import static org.junit.Assert.assertTrue;

import com.example.snailscurlup.model.User;

import org.junit.Test;

/**
 * A unit test for the "User" class.
 * Tests serialization of the user information.
 */
public class UserUnitTest {

    private User mockUser() {
        // Produces a fake User object to use for tests
        final String username = "GenericUser";
        final String email = "myverycoolemailinbox@jmail.com";
        final String phoneNumber = "5878675309";
        final String totalScore = "1010";
        User dummy = new User(username, email, phoneNumber, totalScore);
        return dummy;
    }

    @Test
    public void testUserSerialization() {
        /**
         * Tests if the user info is serialized correctly.
         */
        User dummy = mockUser();
        dummy.setProfilePhotoPath("");  // Null by default, need to do this to prevent error
        String serializedUser = dummy.toString();
        System.out.println(serializedUser);
        assertTrue(serializedUser.equals("Username: GenericUser, Email: myverycoolemailinbox@jmail.com, Phone Number: 5878675309, Profile Photo Path: "));
    }
}
