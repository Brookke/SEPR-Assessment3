package org.teamfarce.mirch.dialogue;

import org.teamfarce.mirch.MIRCH;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a questions intention.
 * <p>
 * This question intent will lead to multiple questions and response objects, differentiated by the
 * style of questioning.
 * </p>
 */
public class QuestionIntent
{
    private int id;
    private List<QuestionAndResponse> questions;
    private String description;

    /**
     * Initialise the question intent.
     *
     * @param id          The unqiue identifier of this question intent.
     * @param questions   The questions which can be asked with this intention.
     * @param description The description of this question intention.
     */
    public QuestionIntent(int id, List<QuestionAndResponse> questions, String description)
    {
        this.id = id;
        this.questions = questions;
        this.description = description;
    }

    /**
     * Initialise the question intent with no questions.
     *
     * @param id          The unqiue identifier of this question intent.
     * @param description The description of this question intention.
     */
    public QuestionIntent(int id, String description)
    {
        this(id, new ArrayList<>(), description);
    }

    /**
     * Get the description of the question intention.
     *
     * @return The description of the question intent.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve a list of descriptions of the styles and associated question text.
     *
     * @return A list of styles and the associated question text.
     */
    public List<String> getAvailableStyleChoices()
    {
        return questions.stream().map(q -> q.getDescription()).collect(Collectors.toList());
    }

    /**
     * Selects a question and returns the result of it.
     *
     * @param style The index of the style to select.
     * @return The result of asking the question.
     */
    public QuestionResult selectQuestion(int style)
    {
        MIRCH.score.addScore(-10);
        return questions.get(style).ask();
    }

    /**
     * Adds a question to the current list of questions.
     *
     * @param question The question to add.
     */
    public void addQuestion(QuestionAndResponse question)
    {
        this.questions.add(question);
    }

    /**
     * Returns the id of this question itent.
     *
     * @return The id
     */
    public int getId()
    {
        return this.id;
    }
}
