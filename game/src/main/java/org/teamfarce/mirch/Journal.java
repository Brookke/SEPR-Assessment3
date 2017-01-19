package org.teamfarce.mirch;

import java.util.ArrayList;

public class Journal {
	
	ArrayList<Prop> foundProps;
	String conversations;
	ArrayList<Clue> foundClues;
	ArrayList<JournalCharacter> metCharacters;
	
	public Journal(){
		this.foundProps = new ArrayList<Prop>();
		this.conversations = "";
	}
	
	public void addProp(Prop prop){
		this.foundProps.add(prop);
		System.out.println("Prop added");
	}
	
	
	public void addClue(Clue clue){
		this.foundClues.add(clue);
	}
	
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
	
	ArrayList<Prop> getProps(){
		return this.foundProps;
	}


	
}
