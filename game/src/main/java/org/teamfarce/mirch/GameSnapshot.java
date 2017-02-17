package org.teamfarce.mirch;

import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.Screens.InterviewScreen;
import org.teamfarce.mirch.map.Map;
import org.teamfarce.mirch.map.Room;

import java.util.List;

/**
 * Stores a snapshot of the game state.
 */
public class GameSnapshot
{
    MIRCH game;

    /**
     * Indicates whether the game has been won.
     */
    public boolean gameWon;
    /**
     * Holds the journal associated with this state.
     */
    public Journal journal;
    List<Clue> clues;
    List<Room> rooms;
    public Map map;
    int meansProven;
    int motiveProven;
    int sumProvesMotive;
    int sumProvesMean;
    int score;
    int time;
    private List<Suspect> suspects;
    private GameState state;
    private float counter = 0f;


    /**
     * Initialises function.
     */
    GameSnapshot(
            MIRCH game,
            Map map,
            List<Room> rooms,
            List<Suspect> suspects,
            List<Clue> clues,
            int sumProvesMotive,
            int sumProvesMeans
    )
    {
        this.game = game;
        this.suspects = suspects;
        this.state = GameState.narrator;
        this.clues = clues;
        this.map = map;
        this.rooms = rooms;
        this.meansProven = 0;
        this.motiveProven = 0;
        this.journal = new Journal();
        this.time = 0;
        this.gameWon = false;
        this.sumProvesMean = sumProvesMeans;
        this.sumProvesMotive = sumProvesMotive;
        this.score = 500;

    }


    /**
     * Takes an integer and adds it on to the current score.
     *
     * @param amount - the integer to add to the score.
     */
    public void modifyScore(int amount)
    {
        score += amount;
    }

    /**
     * Getter for current score
     *
     * @return Returns current score.
     */

    public int getScore()
    {
        return this.score;
    }

    public void updateScore(float delta)
    {
        counter += delta;
        if (counter >= 5) {
            counter = 0;
            score--;
        }

    }

    /**
     * Returns the current value of the pseudo-time variable.
     *
     * @return The current time.
     */
    public int getTime()
    {
        return this.time;
    }

    /**
     * Returns a list of all rooms.
     *
     * @return The rooms.
     */
    List<Room> getRooms()
    {
        return this.rooms;
    }

    /**
     * Returns a list of all props.
     *
     * @return The props.
     */
    List<Clue> getClues()
    {
        return this.clues;
    }

    // The following two functions should be merged at some point.

 /*   *//**
 * Increment the "means proof" value by the given value in the clues.
 * <p>
 * This effectively indicates that the means of the murder was proven by the given arbitrary
 * value in the clues.
 * </p>
 *
 * @param clues The clues which provide the means proven value.
 *//*
    void proveMeans(Collection<Clue> clues)
    {
        for (Clue clue : clues) {
            this.meansProven += clue.provesMean;
        }
    }

    *//**
 * Increment the "motive proof" value by the given value.
 * <p>
 * This effectively indicates that the motive of the murder was proven by the given arbitrary
 * value in the clues.
 * </p>
 *
 * @param clues The clues which provide the motive proven value.
 *//*
    void proveMotive(Collection<Clue> clues)
    {
        for (Clue clue : clues) {
            this.motiveProven += clue.provesMotive;
        }
    }*/

    /**
     * Returns true if the means of the murder has been proven.
     *
     * @return Whether we have "proven" the means.
     */
    public boolean isMeansProven()
    {
        return (this.meansProven >= this.sumProvesMean * 0.5);
    }

    /**
     * Returns true if the motive of the murder has been proven.
     *
     * @return Whether we have "proven" the motive.
     */
    public boolean isMotiveProven()
    {
        return (this.motiveProven >= this.sumProvesMotive * 0.5);
    }

    /**
     * Returns the current game state.
     *
     * @return The game state.
     */
    public GameState getState()
    {
        return this.state;
    }

    /**
     * Allows the setting of the game state.
     *
     * @param state The state to set.
     */
    public void setState(GameState state)
    {
        this.state = state;
    }

    /**
     * Returns a list of all suspects.
     *
     * @return The suspects.
     */
    public List<Suspect> getSuspects()
    {
        return this.suspects;
    }

    public void setSuspect(Suspect s)
    {
        ((InterviewScreen) game.guiController.interviewScreen).setSuspect(s);
    }

    /**
     * Adds the prop to the journal.
     * <p>
     * This tells the journal to keep a log of this prop.
     * </p>
     *
     * @param //Clue The clue to add.
     */
    public void journalAddClue(Clue clue)
    {
        this.journal.foundClues.add(clue);

    }
}
