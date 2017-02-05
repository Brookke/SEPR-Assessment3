package org.teamfarce.mirch.Miscellaneous;

/**
 * Holds the players score and allows it to be changed, used in the status bar.
 */
public class Score {
    /**
     * The current score.
     */
    public int currentScore;

    /**
     *  The constructor for the class, initialises the score to be 500.
     */
    public Score()
    {
        this.currentScore = 500;
    }

    /**
     * Takes an integer and adds it on to the current score.
     * @param scoreToAdd - the integer to add to the score.
     */
    public void addScore(int scoreToAdd)
    {
        currentScore = currentScore + scoreToAdd;
    }

    /**
     * Getter for current score
     * @return Returns current score.
     */
    public int getScore()
    {
        return currentScore;
    }
}
