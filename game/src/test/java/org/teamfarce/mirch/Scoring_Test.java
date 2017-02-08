package org.teamfarce.mirch;

import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for scoring
 */
public class Scoring_Test extends GameTest {
    MIRCH game;
    @Test
    public void test_modifyScore(){
        int currentScore;
        int inc = 20;
        int newScore;
        currentScore = game.gameSnapshot.getScore();
        game.gameSnapshot.modifyScore(inc);
        newScore = game.gameSnapshot.getScore();
        assertSame(newScore, currentScore + inc);
    }
}
