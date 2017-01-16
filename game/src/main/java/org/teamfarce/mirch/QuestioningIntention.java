package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * PLEASE FILL IN THIS EXPLANATION.
 *
 * @author Jacob Wunwin
 */
public class QuestioningIntention {
    ArrayList<Question> questions;
    String description;

    /**
     * Initialiser function.
     */
    QuestioningIntention(){
    }

    /**
     * Allows the asking of a question in a given style, returning a QuestionResult.
     *
     * @param style The id of the questioning style to use
     * @return The concrete question to be asked
     */
    QuestionResult askQuestion(int style) {
        return new QuestionResult();
    }
}
