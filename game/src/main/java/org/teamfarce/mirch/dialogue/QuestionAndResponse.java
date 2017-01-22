package org.teamfarce.mirch.dialogue;

import java.lang.Runnable;
import java.util.Collection;
import java.util.ArrayList;
import org.teamfarce.mirch.Clue;

/**
 * Holds a question and the response.
 * <p>
 * This class holds a question and its response. Along with this are the clues the response
 * represents, the style associated with this particular question and a function which add new
 * question intentions to the dialogue trees.
 * </p>
 * <p>
 * This class could be split into the obvious component parts, the question and the response.
 * However, due to the one to one relationship between these two concepts, they have been merged
 * into this single class.
 * </p>
 */
public class QuestionAndResponse {
    private String question;
    private String style;
    private String response;
    private Collection<Clue> clues;
    private IDialogueTreeAdder dialogueTreeAdder;

    /**
     * Construct a QuestionAndResponse object.
     *
     * @param question The text of the question.
     * @param style A description of the style of the question.
     * @param response The text of the response.
     * @param clues The clue provided by this response.
     * @param dialogueTreeAdder A dialgoue tree adder for the next question intention.
     */
    public QuestionAndResponse(
        String question,
        String style,
        String response,
        Collection<Clue> clues,
        IDialogueTreeAdder dialogueTreeAdder
    ) {
        this.question = question;
        this.style = style;
        this.response = response;
        this.clues = clues;
        this.dialogueTreeAdder = dialogueTreeAdder;
    }

    /**
     * Construct a QuestionAndResponse object without any dialogue adder.
     *
     * @param question The text of the question.
     * @param style A description of the style of the question.
     * @param response The text of the response.
     * @param clues The clue provided by this response.
     */
    public QuestionAndResponse(
        String question,
        String style,
        String response,
        Collection<Clue> clues
    ) {
        this(question, style, response, clues, new NullDialogueTreeAdder());
    }

    /**
     * Get the description of this question.
     * <p>
     * This description will include information about the style of the question as well as the
     * question itself.
     * </p>
     *
     * @return A description of this question.
     */
    public String getDescription() {
        return String.format("%1$s: %2$s", style, question);
    }

    /**
     * Ask the question.
     * <p>
     * This will add to the other dialogue trees and return the result of asking this question.
     * </p>
     *
     * @return The result of asking the question.
     */
    public QuestionResult ask() {
        dialogueTreeAdder.addToTrees();
        return new QuestionResult(response, clues);
    }

    /**
     * Assign a new DialogueTreeAdder.
     *
     * @param dialogueTreeAdder The IDialogueTreeAdder to set.
     */
    public void assignDialogueTreeAdder(IDialogueTreeAdder dialogueTreeAdder) {
        this.dialogueTreeAdder = dialogueTreeAdder;
    }

    /**
     * Add a new DialogueTreeAdder.
     *
     * @param dialogueTreeAdder The IDialogueTreeAdder to add.
     */
    public void addDialogueTreeAdder(IDialogueTreeAdder dialogueTreeAdder) {
        this.dialogueTreeAdder = this.dialogueTreeAdder.addDialogueTreeAdder(dialogueTreeAdder);
    }
}
