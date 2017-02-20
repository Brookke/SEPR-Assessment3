package org.teamfarce.mirch.screens.elements;

import org.teamfarce.mirch.entities.Clue;

/**
 * Button to use with the interview response box
 */
public class InterviewResponseButton {

    /**
     * The text to display on the button
     */
    public String text;

    /**
     * The int to return as a result of the button being pressed
     */
    public int result;

    /**
     * The clue associated with the response, can be null
     */
    public Clue clue = null;

    /**
     * The event handler that listens for the click event
     */
    public EventHandler eventHandler;

    /**
     * Constructor for InterviewResponseButton
     *
     * @param buttonText      String to display on button
     * @param eventHandlerVal On click event handler - use a Lambda function (Java8 only)
     */
    public InterviewResponseButton(String buttonText, int buttonResult, Clue clue, EventHandler eventHandlerVal) {
        this.text = buttonText;
        this.result = buttonResult;
        this.eventHandler = eventHandlerVal;
        this.clue = clue;
    }

    /**
     * Event handler interface
     * Used for defining the click event handler on a InterviewResponseButton
     *
     * Initialising an event handler:
     * InterviewResponseButton.EventHandler eventHandler = (String name) -> {
     * System.out.println(name + " was pressed");
     * };
     *
     * Usage:
     * Used in InterviewResponseBox class on button click
     * InterviewResponseButton.eventHandler.trigger();
     */
    public interface EventHandler {
        void trigger(int result, Clue clue);

    }

}