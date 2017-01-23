package org.teamfarce.mirch;

import java.util.List;
import java.util.Collection;

/**
 * Stores a snapshot of the game state.
 */
public class GameSnapshot {
    private List<Suspect> suspects;
    private GameState state;
    private List<Prop> props;
    private List<Room> rooms;
    private int meansProven;
    private int motiveProven;
    private int time;

    /**
     * Indicates whether the game has been won.
     */
    public boolean gameWon;

    /**
     * Holds the journal associated with this state.
     */
    public Journal journal;

    /**
     * Initialises function.
     */
    GameSnapshot(
        List<Suspect> suspects,
        List<Prop> props,
        List<Room> rooms
    ) {
        this.suspects = suspects;
        this.state = GameState.map;
        this.props = props;
        this.rooms = rooms;
        this.meansProven = 0;
        this.motiveProven = 0;
        this.journal = new Journal();
        this.time = 0;
        this.gameWon = false;
    }

    /**
     * Increments the pseudo-time counter.
     */
    public void incrementTime() {
        ++this.time;
    }

    /**
     * Returns the current value of the pseudo-time variable.
     *
     * @return The current time.
     */
    public int getTime() {
        return this.time;
    }

    /**
     * Returns a list of all rooms.
     *
     * @return The rooms.
     */
    List<Room> getRooms() {
        return this.rooms;
    }

    /**
     * Returns a list of all props.
     *
     * @return The props.
     */
    List<Prop> getProps() {
        return this.props;
    }

    /**
     * Increment the "means proof" value by the given value in the clues.
     * <p>
     * This effectively indicates that the means of the murder was proven by the given arbitrary
     * value in the clues.
     * </p>
     *
     * @param clues The clues which provide the means proven value.
     */
    void proveMeans(Collection<Clue> clues) {
        for (Clue clue: clues){
            this.meansProven += clue.provesMean;
        }
    }

    /**
     * Increment the "motive proof" value by the given value.
     * <p>
     * This effectively indicates that the motive of the murder was proven by the given arbitrary
     * value in the clues.
     * </p>
     *
     * @param clues The clues which provide the motive proven value.
     */
    void proveMotive(Collection<Clue> clues) {
        for (Clue clue: clues){
            this.motiveProven += clue.provesMotive;
        }
    }

    /**
     * Returns true if the means of the murder has been proven.
     *
     * @return Whether we have "proven" the means.
     */
    boolean isMeansProven() {
        return (this.meansProven >= 100);
    }

    /**
     * Returns true if the motive of the murder has been proven.
     *
     * @return Whether we have "proven" the motive.
     */
    boolean isMotiveProven() {
        return (this.motiveProven >= 100);
    }

    /**
     * Allows the setting of the game state.
     *
     * @param state The state to set.
     */
    void setState(GameState state) {
        this.state = state;
        this.incrementTime();
    }

    /**
     * Returns the current game state.
     *
     * @return The game state.
     */
    GameState getState() {
        return this.state;
    }

    /**
     * Returns a list of all suspects.
     *
     * @return The suspects.
     */
    public List<Suspect> getSuspects() {
        return this.suspects;
    }

    /**
     * Adds the prop to the journal.
     * <p>
     * This tells the journal to keep a log of this prop.
     * </p>
     *
     * @param prop The prop to add.
     */
    void journalAddProp(Prop prop) {
        this.journal.addProp(prop);
        if (prop.takeClues()  != null){
            proveMeans(prop.takeClues());
            proveMotive(prop.takeClues());
        }
    }
}
