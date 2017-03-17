package org.teamfarce.mirch;

/**
 * Created by jacks on 17/03/2017.
 */
public class PuzzleGame {
    /*
    *
    * Public boolen for "game completed"
    * method for teh game logic? linked to 8 buttons on screen
    * method updates bool to true or false
    * depending on if the game was won
    *
    */
    private boolean puzzleWon;

    public void PuzzleGame(){
        puzzleWon = false;
    }

    public boolean getPuzzleWon(){
        return this.puzzleWon;
    }

    public void setPuzzleWon(){
        this.puzzleWon = true;
    }
}
