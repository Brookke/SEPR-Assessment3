package org.teamfarce.mirch;

import java.util.List;

/**
 * Stores a snapshot of the game state.
 *
 * @author jacobwunwin
 */
public class GameSnapshot {
    private List<Suspect> suspects;
    private GameState state;
    List<Prop> props;
    List<Room> rooms;
    int meansProven;
    int motiveProven;
    int time;
    boolean gameWon;
    Journal journal;

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
     * Increments the pseudotime counter
     */
    public void incrementTime(){
    	this.time++;
    }
    
    /**
     * Returns the current value of the pseudotime variable
     * @return
     */
    public int getTime(){
    	return this.time;
    }
    
    /**
     * Returns a list of all rooms
     * @return
     */
    List<Room> getRooms(){
    	return this.rooms;
    }
    
    /**
     * Returns a list of all props
     * @return
     */
    List<Prop> getProps(){
    	return this.props;
    }

    /**
     * Increment the "means proof" value by the given value.
     * <p>
     * This effectively indicates that the means of the murder was proven by the given arbitrary
     * value.
     * </p>
     *
     * @param amount The increase in the "means proof"
     */
    void proveMeans(List<Clue> clues) {
        for (Clue clue : clues){
            this.meansProven += clue.provesMean;
        }
    }

    /**
     * Increment the "motive proof" value by the given value.
     * <p>
     * This effectively indicates that the motive of the murder was proven by the given arbitrary
     * value.
     * </p>
     *
     * @param amount The increase in the "motive proof"
     */
    void proveMotive(List<Clue> clues) {
        for (Clue clue : clues){
            this.motiveProven += clue.provesMotive;
        }
    }

    /**
     * Returns true if the means of the murder has been proven.
     *
     * @return Whether we have "proven" the means
     */
    boolean isMeansProven() {
        return (this.meansProven >= 100);  //Arbitrary value for now
    }

    /**
     * Returns true if the motive of the murder has been proven.
     *
     * @return Whether we have "proven" the motive
     */
    boolean isMotiveProven() {
        return (this.motiveProven >= 100);  //Arbitrary value for now
    }
    
    /**
     * Allows the setting of the game state
     * @param state
     */
    void setState(GameState state){
    	this.state = state;
    	this.incrementTime(); //increment pseudo time evry time we change the game state
    }
    
    /**
     * Returns the current game state
     * @return
     */
    GameState getState(){
    	return this.state;
    }

    /**
     * Returns an array list of all suspects
     * @return
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
     * @param prop The prop to add
     */
    void journalAddProp(Prop prop) {
        this.journal.addProp(prop);
        if (prop.takeClues()  != null){
        	proveMeans(prop.takeClues());
        	proveMotive(prop.takeClues());
        }
    }
}
