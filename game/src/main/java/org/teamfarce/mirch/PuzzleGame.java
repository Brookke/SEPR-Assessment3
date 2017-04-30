package org.teamfarce.mirch;

import org.teamfarce.mirch.Screens.PuzzleScreen;

/**
 * Created by jacks on 17/03/2017.
 */
public class PuzzleGame {
    /*
    *
    * Public boolean for "game completed"
    * method for the game logic? linked to 8 buttons on screen
    * method updates bool to true or false
    * depending on if the game was won
    *
    */
    private boolean puzzleWon;
    public String codeEntered = "CODE";
    public String correctCode = "CODE42";

    public void PuzzleGame(){
        puzzleWon = false;
    }

    public boolean getPuzzleWon(){
        return this.puzzleWon;
    }

    public void setPuzzleWon(){
        this.puzzleWon = true;
    }

    public void addToCode(String digit){
        codeEntered += digit;
        System.out.println(codeEntered);
        if(codeEntered.contentEquals(correctCode)){
            setPuzzleWon();

        }
    }
}
