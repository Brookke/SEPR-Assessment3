package org.teamfarce.mirch.dialogue;

import java.util.Collection;
import org.teamfarce.mirch.Clue;

/**
 * Holds the information which is returned by asking a question in the dialogue system.
 * <p>
 * Due to the fact that this class exists to allow the passing of these values together, the value
 * have public access.
 * </p>
 */
public class QuestionResult {
    /**
     * The response of the question.
     */
    public String response;

    /**
     * The clues provided by the question.
     */
    public Collection<Clue> clues;

    /**
     * Constructs a question result.
     *
     * @param response The textual response of the question.
     * @param clues The clues provided by the question.
     */
    public QuestionResult(String response, Collection<Clue> clues) {
        this.response = response;
        this.clues = clues;
    }
}
