package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * PLEASE FILL IN THIS EXPLANATION.
 *
 * @author Jacob Wunwin
 */
public class DialogueTree {
    ArrayList<QuestioningIntention> questionIntentions;
    ArrayList<QuestioningIntention> askedQuestions;

    /**
     * Initialiser function.
     */
    public DialogueTree(){
    }

    /**
     * Returns a new Dialogue from the given intention and style.
     *
     * @param intention The id of the intention of the question
     * @param style The id of the style of the question
     * @return The response dialogue
     */
    Dialogue askQuestion(int intention, int style) {
        return new Dialogue();
    }
}
