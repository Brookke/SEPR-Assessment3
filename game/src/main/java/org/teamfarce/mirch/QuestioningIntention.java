package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * PLEASE FILL IN THIS EXPLANATION
 * @author jacobwunwin
 *
 */
public class QuestioningIntention {
	ArrayList<Question> questions;
	String description;
	
	/**
	 * Initialiser function
	 */
	QuestioningIntention(){
		
	}
	
	/**
	 * Allows the asking of a question in a given style, returning a QuestionResult
	 * @param style
	 * @return questionResult
	 */
	QuestionResult askQuestion(int style){
		return new QuestionResult();
	}
}
