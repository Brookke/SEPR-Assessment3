package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * 
 * @author jacobwunwin
 * Stores a snapshot of the game state.
 */
public class GameSnapshot {
	
	private ArrayList<Suspect> suspects;
	private MapEntity detective;
	private GameState state;
	private ArrayList<Prop> props;
	private ArrayList<Room> rooms;
	private int meansProven;
	private int motiveProven;
	private Journal journal;
	private ArrayList<DialogueBox> currentDialogueBox;
	
	/**
	 * Initialises function
	 */
	GameSnapshot(){
		this.meansProven = 0;
		this.motiveProven = 0;
	}
	
	/**
	 * Allows changing of the meansProven variable, which stores how much the
	 * means of murder has been proven.
	 * @param amount
	 */
	void proveMeans(int amount){
		this.meansProven += amount;
	}
	
	/**
	 * Allows the changing of the motiveProven variable, which stores how much
	 * the motive of the murder has been proven
	 * @param amount
	 */
	void proveMotive(int amount){
		this.motiveProven += amount;
	}
	
	/**
	 * Returns true if the means of the murder has been proven
	 * @return boolean
	 */
	boolean isMeansProven(){
		return (this.meansProven >= 100)  //Arbitrary value for now 
	}
	
	/**
	 * Returns true if the motive of the murder has been proven
	 * @return
	 */
	boolean isMotiveProven(){
		return (this.motiveProven >= 100)  //Arbitrary value for now 
	}
	
	/**
	 * Adds the prop to the journal, used for keeping a log of props
	 * @param prop
	 */
	void journalAddProp(Prop prop){
		this.journal.addProp(prop);
		//proveMeans(prop.takeClue().provesMeans);
		//proveMotive(prop.takeClue().provesMotive);
	}
	
	/**
	 * Adds the dialogue to the journal, used for keeping a log of dialogue
	 * @param dialogue
	 */
	void journalAddDialogue(QuestionandResponse dialogue){
		this.journal.addDialogue(dialogue);
		//for (Clue clue : dialogue.clues) {
		//	proveMeans(clue.provesMeans);
		//	provesMotives(clue.provesMotives);
		//}
	}
}
