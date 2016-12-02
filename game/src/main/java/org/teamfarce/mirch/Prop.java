package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * Creates an individual prop entity, extends from the MapEntity class
 * @author jacobwunwin
 *
 */
public class Prop extends MapEntity {
	ArrayList<Clue> clue;
	
	/**
	 * Initialises the class
	 */
	Prop(){
		
	}
	
	/**
	 * Returns a Clue ArrayList of all clues attached to this prop.
	 * @return
	 */
	ArrayList<Clue> takeClue(){
		return this.clue;
	}
}

