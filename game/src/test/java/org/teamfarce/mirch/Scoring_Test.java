package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for scoring
 */
public class Scoring_Test extends GameTest
{

    GameSnapshot gameSnapshot;

    @Before
    public void init_tests()
    {

        gameSnapshot = new GameSnapshot(null, null, null, 0, 0);

    }


    @Test
    public void modifyScore_Addition()
    {
        int currentScore;
        int inc = 20;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.modifyScore(inc);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore + inc);
    }

    @Test
    public void modifyScore_Subtraction()
    {
        int currentScore;
        int inc = -20;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.modifyScore(inc);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore + inc);
    }

    @Test
    public void updateScore_no_decrease()
    {
        int currentScore;
        float delta = 0.25f;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.updateScore(delta);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore);
    }

    @Test
    public void updateScore_has_decrease()
    {
        int currentScore;
        float delta = 5.00f;
        int newScore;
        currentScore = gameSnapshot.getScore();
        gameSnapshot.updateScore(delta);
        newScore = gameSnapshot.getScore();
        assertEquals(newScore, currentScore - 1);
    }
}
