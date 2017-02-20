package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for scoring
 */
public class Scoring_Test extends GameTest {

    GameSnapshot gameSnapshot;

    @Before
    public void init_tests() {

        gameSnapshot = new GameSnapshot(null, null, null, null, null);

    }


    @Test
    public void modifyScoreAddition() {
        int currentScore;
        int inc = 20;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.modifyScore(inc);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore + inc);
    }

    @Test
    public void modifyScoreSubtraction() {
        int currentScore;
        int inc = -20;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.modifyScore(inc);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore + inc);
    }

    @Test
    public void updateScoreNoDecrease() {
        int currentScore;
        float delta = 0.25f;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.updateScore(delta);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore);
    }

    @Test
    public void updateScoreHasDecrease() {
        int currentScore;
        float delta = 5.00f;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.updateScore(delta);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore - 1);
    }
}
