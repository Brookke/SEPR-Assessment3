package org.teamfarce.mirch;

import java.util.ArrayList;

public class ResponseIntent {
	ArrayList<String> responses;
	String correctResponse;
	Clue clue;
	QuestionIntent newQuestion;
	
	public ResponseIntent(ArrayList<String> responses, String correctResponse, Clue clue, QuestionIntent newQ){
		this.responses = responses;
		this.correctResponse = correctResponse;
		this.clue = clue;
		this.newQuestion = newQ;
	}
	
	QuestionIntent getQuestionIntent(){
		return this.newQuestion;
	}
	
	Clue getClue(){
		return this.clue;
	}
	

}
