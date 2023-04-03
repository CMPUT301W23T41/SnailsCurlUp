package com.example.snailscurlup;

import android.app.Activity;

import androidx.exifinterface.media.ExifInterface;
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

public class SearchingTest {
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

    //TODO: wait for the actual implementation
    /**
     * Checking the search-by-username function from search page
     * Synthesizes random user credentials for testing purposes.
     * @throws AssertionError - For when test fails
     */
    @Test
    public void checkSearchByUsername(){

        // Assertion for current activity being the main one
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