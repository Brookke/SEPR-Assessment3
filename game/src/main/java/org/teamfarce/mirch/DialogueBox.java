package org.teamfarce.mirch;

import java.util.ArrayList;


public class DialogueBox {
    private ArrayList<String> textBuffer; //this seems redundant
    private ArrayList<DialogueOption> dialogueOption;
    
	public DialogueBox(ArrayList<DialogueOption> dialogueOption){
		this.dialogueOption = dialogueOption;
	}
	
	public DialogueOption selectOption(int selection){
		return this.dialogueOption.get(selection);
	}
}