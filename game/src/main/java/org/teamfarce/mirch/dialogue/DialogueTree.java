package org.teamfarce.mirch.dialogue;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds question intents for a character and has methods to select from the tree.
 */
public class DialogueTree {
    private List<QuestionIntent> questionIntents;

    /**
     * Initialise the dialogue tree.
     *
     * @param questionIntents The initial state of the dialogue tree.
     */
    public DialogueTree(List<QuestionIntent> questionIntents) {
        this.questionIntents = questionIntents;
    }

    /**
     * Retrieve a list of strings which describe the possible questions intentions available to
     * ask.
     *
     * @return The descriptions.
     */
    public List<String> getAvailableIntentChoices() {
        return questionIntents.stream().map(q -> q.getDescription()).collect(Collectors.toList());
    }

    /**
     * Returns a list of question texts and associated style which may be asked with the provided
     * intention id.
     *
     * @param intent The intention to query.
     * @return The question texts and styles.
     */
    public List<String> getAvailableStyleChoices(int intent) {
        return questionIntents.get(intent).getAvailableStyleChoices();
    }

    /**
     * Selects a question and returns the result of it.
     * <p>
     * The result of selecting the question with the associated intent and style will be the
     * response text from the character and any clues it provides.
     * </p>
     *
     * @param intent The index of the intent to select.
     * @param style The index of the style to select.
     * @return The result of selecting the question.
     */
    public QuestionResult selectQuestion(int intent, int style) {
        return this.questionIntents.remove(intent).selectQuestion(style);
    }

    /**
     * Add the given question intent to the current collection of question intents.
     *
     * @param questionIntent The question intent to add.
     */
    public void addQuestionIntent(QuestionIntent questionIntent) {
        questionIntents.add(questionIntent);
    }
}
