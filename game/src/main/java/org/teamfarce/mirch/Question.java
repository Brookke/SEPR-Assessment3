package org.teamfarce.mirch;

public class Question {
	public enum Style{
		AGGRESSIVE, PLACATING, CONVERSATIONAL, DIRECT, GRUNTSANDPOINTS
	}
	
	private Style style;
	private String questionText;
	
	Question(Style style, String text){
		this.style = style;
		this.questionText = text;
	}
	
	String getQuestionText(){
		return this.questionText;
	}
	
	Style getStyle(){
		return this.style;
	}

}
