package org.teamfarce.mirch;

/**
 * Stores information about a single suspect character. Extends the MapEntity class.
 * @author jacobwunwin
 *
 */
public class Suspect extends MapEntity {
	DialogueTree dialogueTree;
	boolean beenAccused;
	boolean isMurderer;
	
	/**
	 * Initialiser function.
	 */
	Suspect(){
		this.beenAccused = false;
		this.isMurderer = false;
	}
	
	/**
	 * Returns true if the boolean 
	 * @param hasEvidence
	 * @return boolean
	 */
	boolean accuse(boolean hasEvidence){
		return false;
	}
}
