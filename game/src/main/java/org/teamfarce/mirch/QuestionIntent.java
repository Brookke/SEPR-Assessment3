package org.teamfarce.mirch;

import java.util.ArrayList;

public class QuestionIntent {
	private ArrayList<Question> questions;
	private ResponseIntent response;
	//string used when displying the question intents to choose from
	private String description;
	
	QuestionIntent(ArrayList<Question> questions, ResponseIntent resp ) {
        this.questions = questions; 
        this.response = resp;
	}
	
	
	String getDescription(){
		return this.description;
	}
	
    ArrayList<Question> getStyleChoices(){
    	return this.questions;
    }
    
    ResponseIntent getResponseIntent(){
    	return this.response;
    }
    


}
