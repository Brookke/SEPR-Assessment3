package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * 
 * @author jacobwunwin
 * Stores a snapshot of the game state.
 */
public class GameSnapshot {
	
	ArrayList<Suspect> suspects;
	MapEntity detective;
	ArrayList<Prop> props;
	ArrayList<Room> rooms;
	int meansProven;
	int motiveProven;
	GameState state;
	
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
		
	}
	
	/**
	 * Allows the changing of the motiveProven variable, which stores how much
	 * the motive of the murder has been proven
	 * @param amount
	 */
	void proveMotive(int amount){
		
	}
	
	/**
	 * Returns true if the means of the murder has been proven
	 * @return boolean
	 */
	boolean isMeansProven(){
		return false;
	}
	
	/**
	 * Returns true if the motive of the murder has been proven
	 * @return
	 */
	boolean isMotiveProven(){
		return false;
	}

}
