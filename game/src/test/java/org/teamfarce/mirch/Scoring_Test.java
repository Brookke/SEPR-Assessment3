package org.teamfarce.mirch;

import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for scoring
 */
public class Scoring_Test extends GameTest {
    MIRCH game;
    game = new MIRCH();
    game.gameSnapshot = new GameSnapshot(null, null, null, 0, 0);
    @Test
    public void test_modifyScore_Addition(){
        int currentScore;
        int inc = 20;
        int newScore;
        currentScore = game.gameSnapshot.getScore();
        game.gameSnapshot.modifyScore(inc);
        newScore = game.gameSnapshot.getScore();
        assertSame(newScore, currentScore + inc);
    }

    @Test
    public void test_modifyScore_Subtraction(){
        int currentScore;
        int inc = -20;
        int newScore;
        currentScore = game.gameSnapshot.getScore();
        game.gameSnapshot.modifyScore(inc);
        newScore = game.gameSnapshot.getScore();
        assertSame(newScore, currentScore - inc);
    }

    @Test
    public void test_updateScore_no_decrease(){
        int currentScore;
        float delta = 0.25f;
        int newScore;
        currentScore = game.gameSnapshot.getScore();
        game.gameSnapshot.updateScore(delta);
        newScore = game.gameSnapshot.getScore();
        assertSame(newScore, currentScore);
    }

    @Test
    public void test_updateScore_has_decrease(){
        int currentScore;
        float delta = 1.00f;
        int newScore;
        currentScore = game.gameSnapshot.getScore();
        game.gameSnapshot.updateScore(delta);
        newScore = game.gameSnapshot.getScore();
        assertSame(newScore, currentScore - 1);
    }
}
