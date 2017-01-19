package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * 
 * @author LJ
 *class works with dialoguebox to handle any textual information such as interacting with props.
 */
public class DialogueOption {
	private int id;
	private String text;
	
	public DialogueOption(int id, String text){
        this.id = id;
        this.text = text;
	}
	
	/**
	 * given a particular gamestate return an option for dialogue???
	 * @param gamestate
	 * @return dialoguebox
	 */
	public DialogueBox action(GameState gamestate) {
		if(gamestate == GameState.dialogue){
			ArrayList<DialogueOption> dOps = new ArrayList<DialogueOption>();
			dOps.add(this);
			return new DialogueBox(dOps);
		}
	}

}
