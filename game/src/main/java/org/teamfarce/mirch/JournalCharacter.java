/**
 * 
 */
package org.teamfarce.mirch;

/**
 * Stores a reference to a character for use in the player Journal. THis allows us to keep track of characters
 * The player has met.
 * @author jacobwunwin
 *
 */
public class JournalCharacter {
	public Suspect suspect;
	public String communicatons;
	public float xMove;
	public float yMove;
	
	JournalCharacter(Suspect suspect, String initialComms){
		this.suspect = suspect;
		this.communicatons = initialComms;
		this.xMove = 0f;
		this.yMove = 0f;
	}
}
