package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * The Journal stores relevant information to the players progress through the game. This includes a list
 * of all props discovered, and a log of all conversations.
 * @author jacobwunwin
 *
 */
public class Journal {
	
	ArrayList<Prop> foundProps;
	String conversations;
	ArrayList<Clue> foundClues;
	ArrayList<JournalCharacter> metCharacters;
	
	/**
	 * Initialise the Journal
	 */
	public Journal(){
		this.foundProps = new ArrayList<Prop>();
		this.conversations = "";
	}
	
	/**
	 * Add a prop to the journal
	 * @param prop
	 */
	public void addProp(Prop prop){
		this.foundProps.add(prop);
		System.out.println("Prop added");
	}
	
	
	/**
	 * Add a clue to the journal
	 * @param clue
	 */
	public void addClue(Clue clue){
		this.foundClues.add(clue);
	}
	
	/**
	 * Add a conversation with a given character to the journal
	 * @param text
	 * @param character
	 */
	public void addConversation(String text, Suspect character){
		boolean characterFound = false;
		for (JournalCharacter c : this.metCharacters){
			if (c.suspect.equals(character)){
				c.communicatons += text + "\n";
				characterFound = true;
			}
		}
		if (!characterFound){
			this.metCharacters.add(new JournalCharacter(character, text));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	ArrayList<Prop> getProps(){
		return this.foundProps;
	}


	
}
