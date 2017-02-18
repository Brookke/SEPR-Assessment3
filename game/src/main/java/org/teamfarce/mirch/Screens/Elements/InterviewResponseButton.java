package org.teamfarce.mirch.Screens.Elements;

import org.teamfarce.mirch.Entities.Clue;

/**
 * Button to use with the interview response box
 */
public class InterviewResponseButton
{
    private Clue clue = null;
    /**
     * The text to display on the button
     */
    public String text;

    /**
     * The int to return as a result of the button being pressed
     */
    public int result;

    /**
     * The event handler that listens for the click event
     */
    public EventHandler eventHandler;

    /**
     * The event handler that listens for the click event
     */
    public ClueEventHandler clueEventHandler;

    /**
     * Constructor for InterviewResponseButton
     *
     * @param buttonText      String to display on button
     * @param eventHandlerVal On click event handler - use a Lambda function (Java8 only)
     */
    public InterviewResponseButton(String buttonText, int buttonresult, EventHandler eventHandlerVal)
    {
        text = buttonText;
        result = buttonresult;
        eventHandler = eventHandlerVal;
    }

    public InterviewResponseButton(String buttonText, int buttonresult, Clue clue, ClueEventHandler eventHandlerVal)
    {
        text = buttonText;
        result = buttonresult;
        this.clue = clue;
        clueEventHandler = eventHandlerVal;
    }

    /**
     * Event handler interface
     * Used for defining the click event handler on a InterviewResponseButton
     * <p>
     * Initialising an event handler:
     * InterviewResponseButton.EventHandler eventHandler = (String name) -> {
     * System.out.println(name + " was pressed");
     * };
     * <p>
     * Usage:
     * Used in InterviewResponseBox class on button click
     * InterviewResponseButton.eventHandler.trigger();
     */
    public interface EventHandler
    {
        void trigger(int result);

    }

    /**
     * Event handler interface
     * Used for defining the click event handler on a InterviewResponseButton
     * <p>
     * Initialising an event handler:
     * InterviewResponseButton.EventHandler eventHandler = (String name) -> {
     * System.out.println(name + " was pressed");
     * };
     * <p>
     * Usage:
     * Used in InterviewResponseBox class on button click
     * InterviewResponseButton.eventHandler.trigger();
     */
    public interface ClueEventHandler
    {
        void trigger(int result, Clue clue);

    }

}