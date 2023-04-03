package com.example.snailscurlup;

import static org.junit.Assert.assertTrue;

import com.example.snailscurlup.ui.leaderboard.Leader;

import org.junit.Test;

public class LeaderUnitTest {
    private Leader mockLeader() {
        String username = "progamer420";
        String score = "38450";
        Leader leadPlayer = new Leader(username, score);
        return leadPlayer;
    }

    @Test
    public void testGetters() {
        Leader lead = mockLeader();
        assertTrue(lead.getUsername().equals("progamer420"));
        assertTrue(lead.getScore().equals("38450"));
    }
}
