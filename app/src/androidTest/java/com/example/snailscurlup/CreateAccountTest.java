package com.example.snailscurlup;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Random;

public class CreateAccountTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Sets up the Solo object for the intent tests at beginning of execution.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Fetches the current activity.
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if the application can create an account from the initial screen.
     * Synthesizes random user credentials for testing purposes.
     * @throws AssertionError - For when test fails
     */
    @Test
    public void checkCreateAccount(){
        // Assertion for current activity being the main one
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Go to Create Account page
        solo.clickOnView(solo.getView(R.id.create_account_button));

        // Fill in account details
        // Generate a random number to attach to account so it can be tested
        // many times without wiping app data
        final int randomLowerBound = 10000000;
        final int randomUpperBound = 99999999;
        Random random = new Random();
        int randomUsernameID = random.nextInt(randomUpperBound - randomLowerBound) + randomLowerBound;

        // Assemble some fake info to provide to account registration
        String dummyUsername = "testuser" + Integer.toString(randomUsernameID);
        String dummyEmail = dummyUsername + "@email.com";
        String dummyPhone = "1234567890";

        // Enter in fake information for account registration
        solo.enterText((EditText) solo.getView(R.id.username_field), dummyUsername);
        solo.enterText((EditText) solo.getView(R.id.Email_field), dummyEmail);
        //solo.enterText((EditText) solo.getView(R.id.phonenumber_field), dummyPhone);

        // Proceed with account creation
        solo.clickOnView(solo.getView(R.id.create_account_button));

        // Test to see if username is recognized
        String welcomeMsg = dummyUsername;
        assertTrue(solo.waitForText(welcomeMsg, 1, 3000));
    }

    /**
     * Cleans up all intent testing objects after execution completes.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
