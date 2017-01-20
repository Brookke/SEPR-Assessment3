package org.teamfarce.mirch;

import java.util.ArrayList;
import java.util.HashMap;

import org.teamfarce.mirch.Question.Style;

/**
 * Holds questionintents for a character and has convenience methods to navigate the resulting tree.
 *
 * @author Jacob Wunwin
 */
public class DialogueTree {
	private HashMap<Style, Personality> mapStylePersonality = new HashMap<Style, Personality>();
	private Personality personality;
    ArrayList<QuestionIntent> questions;
    
    enum Personality {
    	AGGRESSIVE, ANXIOUS, UPBEAT, SAD, CAVEMAN
    }

    /**
     * Initialiser function.
     */
    public DialogueTree(ArrayList<QuestionIntent> questions, Personality personality){
    	this.questions = questions;
    	this.personality = personality;
    	mapStylePersonality.put(Style.AGGRESSIVE, Personality.ANXIOUS);
    	mapStylePersonality.put(Style.CONVERSATIONAL, Personality.AGGRESSIVE);
    	mapStylePersonality.put(Style.DIRECT, Personality.UPBEAT);
    	mapStylePersonality.put(Style.PLACATING, Personality.SAD);
    	mapStylePersonality.put(Style.GRUNTSANDPOINTS, Personality.CAVEMAN);
    }

    /**
     * Returns a new Dialogue from the given intention and style.
     *
     * @param intention The id of the intention of the question
     * @param style The id of the style of the question
     * @return The response dialogue
     */
    
    /**
     * gives a preview of the question intents you can use (player selects one, then selects a style)
     * @return
     */
    ArrayList<String> getAvailableIntentsAsString(){
    	ArrayList<String> intents = new ArrayList<String>();
    	for (QuestionIntent questionIntent : this.questions){
    		intents.add(questionIntent.getDescription());		
    	}
    	return intents;
    }
    
    ArrayList<QuestionIntent> getAvailableIntents(){
    	return this.questions;
    }
    
    /**
     * 
     * given a QuestionIntent, return the available styled questions as an arraylist of strings
     * @return
     */
    ArrayList<String> getAvailableStyles(int intentSelection){
    	ArrayList<String> styledQuestions = new ArrayList<String>();
    	for (Question styledQuestion : this.questions.get(intentSelection).getStyleChoices()){
    		styledQuestions.add(styledQuestion.getStyle().name() + " : " + styledQuestion.getQuestionText());
    	}
    	return styledQuestions;
    }
    
    /**
     * 
     * @param intentSelection - selects intent
     * @param styleSelection - selects questionstyle
     * if the correct choice is made, an optimal response is given, a new intent is added to the tree,
     * and a clue is yielded, otherwise a bad response and no progressing is given.
     * @return
     */
    String selectStyledQuestion(int intentSelection, int styleSelection, Journal journal, Suspect suspect){
    	ResponseIntent respInt = this.questions.get(intentSelection).getResponseIntent();
    	Style style = this.questions.get(intentSelection).getStyleChoices().get(styleSelection).getStyle();
    	String question = this.questions.get(intentSelection).getDescription();
		this.questions.remove(intentSelection);
    	
    	if (this.mapStylePersonality.get(style) == this.personality) {
    		//todo use add conversation to journal method.
    		if(!respInt.isDead()){
        		this.questions.add(respInt.getQuestionIntent());
    		}
    		journal.addClue(respInt.getClue());
    		journal.addConversation(respInt.getCorrectResponse(), suspect.name);
    		journal.addConversation(question, "You");
    		
    		return respInt.getCorrectResponse();
    	} 
    	else {
    		journal.addConversation(respInt.responses.get(this.personality.ordinal()), suspect.name);
    		journal.addConversation(question, "You");
    		return respInt.responses.get(this.personality.ordinal());
    	}
    }
    
    
    
}
