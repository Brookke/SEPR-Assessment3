package org.teamfarce.mirch;

import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.Map.Map;
import org.teamfarce.mirch.Map.Room;

import java.util.List;
import java.util.Random;

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
    int score = 0;
    int time;
    int currentPersonality;
    private List<Suspect> suspects;
    private GameState state;
    private float counter = 0f;

    public boolean hasFoundMurderRoom = false;

    private Suspect interviewSuspect = null;

    public Suspect victim;
    public Suspect murderer;

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
        this.journal = new Journal(game);
        this.time = 0;
        this.gameWon = false;
        this.sumProvesMean = sumProvesMeans;
        this.sumProvesMotive = sumProvesMotive;
        this.score = 250;
        this.currentPersonality = 0;
    }


    /**
     * Takes an integer and adds it on to the current score.
     *
     * @param amount - the integer to add to the score.
     */
    public void modifyScore(int amount)
    {
        score += amount;

        if (score <= 0)
        {
            //Lost game

            String murderer = "";
            String victim = "";
            String room = "";
            String weapon = "";

            //Get the murderer and victims name
            for (Suspect s : game.gameSnapshot.getSuspects())
            {
                if (s.isKiller())
                {
                    murderer = s.getName();
                }

                if (s.isVictim())
                {
                    victim = s.getName();
                }
            }

            //Get the murder room name and the murder weapon
            for (Room r : game.gameSnapshot.map.getRooms())
            {
                if (r.isMurderRoom())
                {
                    room = r.getName();
                }

                for (Clue c : r.getClues())
                {
                    if (c.isMeansClue())
                    {
                        weapon = c.getName();
                    }
                }
            }

            //List of other detectives who could've possibly solved the crime
            String[] detectives = new String[]{"Richie Paper", "Princess Fiona", "Lilly Blort", "Michael Dodders"};

            //Send the speech to the narrrator screen and display it
            game.guiController.narratorScreen.setSpeech("Oh No!\n \nDetective " + detectives[new Random().nextInt(detectives.length)] + " has solved the crime before you! They discovered that all along it was " + murderer + " who killed " + victim + " in the " + room + " with " + weapon + "\n \n" +
                    "It's a real shame, I really thought you'd have gotten there first!\n \nOh well! Better luck next time!")
                    .setButton("End Game", new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    });

            game.gameSnapshot.setState(GameState.narrator);
        }
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
            modifyScore(-1);
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
    public List<Clue> getClues()
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
        return journal.hasFoundMurderWeapon();
    }

    /**
     * Returns true if the motive of the murder has been proven.
     *
     * @return Whether we have "proven" the motive.
     */
    public boolean isMotiveProven()
    {
        return journal.hasFoundMotiveClue();
    }

    /**
     * Returns true if they have discovered the murder room or not
     *
     * @return Whether the murder room has been discovered or not
     */
    public boolean hasFoundMurderRoom()
    {
        return hasFoundMurderRoom;
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

    public void setSuspectForInterview(Suspect s)
    {
        interviewSuspect = s;
    }

    public Suspect getSuspectForInterview()
    {
        return interviewSuspect;
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


    /**
     * Getter for current personality
     *
     * @return Returns current personality score.
     */

    public int getPersonality()
    {
        return this.currentPersonality;
    }
    /**
     * Updates current personality of player in game
     * @param amount Amount to modify personality score by
     */
    public void modifyPersonality(int amount) {
        if (this.currentPersonality > -10 && this.currentPersonality < 10) {
            this.currentPersonality += amount;
        }
    }
}
