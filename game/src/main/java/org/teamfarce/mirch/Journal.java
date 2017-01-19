package org.teamfarce.mirch;

import java.util.ArrayList;

public class Journal {
	
	ArrayList<Prop> foundProps;
	String conversations;
	
	Journal(){
		this.foundProps = new ArrayList<Prop>();
		this.conversations = "";
	}
	
	void addProp(Prop prop){
		this.foundProps.add(prop);
		System.out.println("Prop added");
	}
	
	void addConversation(String text, String characterName){
		this.conversations = characterName + " : " + text + "\n" + this.conversations;
	}
	
	
	ArrayList<Prop> getProps(){
		return this.foundProps;
	}


	
}
