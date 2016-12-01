package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * PLEASE FILL IN THIS EXPLANATION
 * @author jacobwunwin
 *
 */
public class DialogueTree {
	ArrayList<QuestioningIntention> questionIntentions;
	ArrayList<QuestioningIntention> askedQuestions;
	
	/**
	 * Initialiser function
	 */
	DialogueTree(){
		
	}
	
	/**
	 * Returns a new Dialogue from the given intention and style
	 * @param intention
	 * @param style
	 * @return Dialogue
	 */
	Dialogue askQuestion(int intention, int style){
		return new Dialogue();
	}
}
