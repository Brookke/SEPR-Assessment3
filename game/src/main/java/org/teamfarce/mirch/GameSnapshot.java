package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * Stores a snapshot of the game state.
 *
 * @author jacobwunwin
 */
public class GameSnapshot {
    private ArrayList<Suspect> suspects;
    private GameState state;
    ArrayList<Prop> props;
    ArrayList<Room> rooms;
    private ArrayList<Door> doors;
    int meansProven;
    int motiveProven;
    int time;
    boolean gameWon;
    Journal journal;
    private DialogueBox currentDialogueBox;

    /**
     * Initialises function.
     */
    GameSnapshot(
        ArrayList<Suspect> suspects,
        ArrayList<Prop> props,
        ArrayList<Room> rooms,
        ArrayList<Door> doors
    ) {
        this.suspects = suspects;
        this.state = GameState.map;
        this.props = props;
        this.rooms = rooms;
        this.doors = doors;
        this.meansProven = 0;
        this.motiveProven = 0;
        this.journal = new Journal();
        this.time = 0;
        this.gameWon = false;
    }
    
    public void incrementTime(){
    	this.time++;
    }
    
    public int getTime(){
    	return this.time;
    }
    
    ArrayList<Room> getRooms(){
    	return this.rooms;
    }
    
    ArrayList<Prop> getProps(){
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
    void proveMeans(ArrayList<Clue> clues) {
    	try{
	    	for (Clue clue : clues){
	            this.meansProven += clue.provesMean;
	    	}
    	} finally {}
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
    void proveMotive(ArrayList<Clue> clues) {
    	try {
	    	for (Clue clue : clues){
	    		this.motiveProven += clue.provesMotive;
	    	}
    	} finally {}
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
    
    void setState(GameState state){
    	this.state = state;
    	this.incrementTime(); //increment pseudo time evry time we change the game state
    }
    
    GameState getState(){
    	return this.state;
    }


	public ArrayList<Suspect> getSuspects() {
		return this.suspects;
	}

	public ArrayList<Door> getDoors() {
		// TODO Auto-generated method stub
		return this.doors;
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


    // /**
    //  * Adds the dialogue to the journal, used for keeping a log of dialogue
    //  * @param dialogue
    //  */
    // void journalAddDialogue(QuestionandResponse dialogue) {
    //  this.journal.addDialogue(dialogue);
    //  //for (Clue clue : dialogue.clues) {
    //  //  proveMeans(clue.provesMeans);
    //  //  provesMotives(clue.provesMotives);
    //  //}
    // }
}
