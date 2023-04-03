package com.example.snailscurlup;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Random;

public class PlayerTest {
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
     *  Using existing testing account: testusert41 and testuser
     *  checks if the player can check other player's profile from Search page
     *  @throws AssertionError - For when test fails
     */
    @Test
    public void checkGoToOtherProfile(){

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Login to the testAccount: testusert41 if not login already
        if(solo.searchText("Login")){
            solo.clickOnView(solo.getView(R.id.login_button));
            solo.enterText((EditText) solo.getView(R.id.username_field), "testusert41");
            solo.clickOnView(solo.getView(R.id.login_button));
        }

        // Go to Search page from navbar
        solo.clickOnView(solo.getView(R.id.search));

        // Enter in fake information for account registration
        solo.enterText((EditText) solo.getView(R.id.search_bar_input_text), "testuser");

        // Proceed with Search button
        solo.clickOnView(solo.getView(R.id.search_button));

        // Test to see if username is recognized
        String searchWelcomeMsg = "Name: " + "testuser";
        assertTrue(solo.waitForText(searchWelcomeMsg, 1, 3000));

        // Proceed with list content view
        solo.clickOnView(solo.getView(R.id.user_image));

        // Test to see if page goes to this user's profile
        assertTrue(solo.waitForView(R.id.user_info_section));
        assertTrue(solo.searchText("testuser"));


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