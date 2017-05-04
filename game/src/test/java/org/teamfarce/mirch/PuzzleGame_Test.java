package org.teamfarce.mirch;
import org.junit.Test;
import org.teamfarce.mirch.entities.AbstractPerson;
import org.teamfarce.mirch.screens.MapScreen;

import static org.junit.Assert.*;
import java.util.List;

/**
 * Created by Andrew on 03/05/2017.
 */
public class PuzzleGame_Test extends GameTest {

    private boolean puzzleWon = false;
    public String codeEntered = "CODE";
    public String correctCode = "CODE42";
    private MIRCH game;


    @Test
    public void isWon(){
        PuzzleGame puzzleGame = new PuzzleGame(game);
        puzzleGame.puzzleWon = false;
        assertFalse(puzzleGame.puzzleWon);
        puzzleGame.puzzleWon = true;
        assertTrue(puzzleGame.puzzleWon);
    }

    @Test
    public void addToCode(){
        PuzzleGame pg  = new PuzzleGame(game);
        assertEquals(pg.codeEntered, "CODE");
        pg.codeEntered += 4;
        assertEquals(pg.codeEntered, "CODE4");
        pg.codeEntered += 2;
        assertEquals(pg.codeEntered, "CODE42");

    }

    @Test
    public void isCodeCorrect(){
        PuzzleGame pg  = new PuzzleGame(game);
        pg.codeEntered = "CODE42";
        assertEquals(pg.codeEntered, pg.correctCode);
    }
}
