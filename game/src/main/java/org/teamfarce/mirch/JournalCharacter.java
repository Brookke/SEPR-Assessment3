/**
 * 
 */
package org.teamfarce.mirch;

/**
 * @author jacobwunwin
 *
 */
public class JournalCharacter {
	public Suspect suspect;
	public String communicatons;
	
	JournalCharacter(Suspect suspect, String initialComms){
		this.suspect = suspect;
		this.communicatons = initialComms;
	}
}
