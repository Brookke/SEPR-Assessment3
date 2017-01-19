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
    private ArrayList<Prop> props;
    private ArrayList<Room> rooms;
    private int meansProven;
    private int motiveProven;
    Journal journal;
    private DialogueBox currentDialogueBox;

    /**
     * Initialises function.
     */
    GameSnapshot(
        ArrayList<Suspect> suspects,
        ArrayList<Prop> props,
        ArrayList<Room> rooms
    ) {
        this.suspects = suspects;
        this.state = GameState.map;
        this.props = props;
        this.rooms = rooms;
        this.meansProven = 0;
        this.motiveProven = 0;
        this.journal = new Journal();
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
    void proveMeans(int amount) {
        this.meansProven += amount;
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
    void proveMotive(int amount) {
        this.motiveProven += amount;
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
    }
    
    GameState getState(){
    	return this.state;
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
        //this.journal.addProp(prop);
        //proveMeans(prop.takeClue().provesMeans);
        //proveMotive(prop.takeClue().provesMotive);
    }

	public ArrayList<Suspect> getSuspects() {
		return this.suspects;
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
