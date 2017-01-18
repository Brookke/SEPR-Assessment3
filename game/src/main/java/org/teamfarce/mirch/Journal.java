package org.teamfarce.mirch;

import java.util.ArrayList;

public class Journal {
	
	ArrayList<Prop> foundProps;
	ArrayList<JournalCharacter> metCharacters;
	
	Journal(){
		
	}
	
	void addProp(Prop prop){
		this.foundProps.add(prop);
	}
	
	void addConversation(String text, Suspect character){
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
