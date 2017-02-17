package org.teamfarce.mirch.dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.teamfarce.mirch.Entities.Clue;

import java.util.List;

/**
 * Created by brookehatton on 16/02/2017.
 */
public class Dialogue
{
    private JsonValue jsonData;

    public Dialogue(String jsonFile, List<Clue> clues) throws InvalidDialogueException {
        importDialogue(jsonFile);
        validateJsonAgainstClues(clues);
    }

    /**
     * Reads in the JSON file and stores dialogue in the dialogue HashMap
     *
     * @param fileName - The filename to read from
     */
    private void importDialogue(String fileName)
    {
        jsonData = new JsonReader().parse(Gdx.files.internal(fileName));
    }

    /**
     * This takes a list of clues and validates that there exists a valid response to each of the clues.
     * @param clues Clues to validate against
     * @return Boolean true if it passes
     * @throws InvalidDialogueException if the JSON is invalid
     */
    private boolean validateJsonAgainstClues(List<Clue> clues) throws InvalidDialogueException {
        for (Clue c: clues) {
            if (this.jsonData.get("responses").has(c.getName())) {
                if (!(this.jsonData.get("responses").getString(c.getName()).length() > 0)) {
                    throw new InvalidDialogueException("No response value for clue: " + c.getName());
                }
            } else {
                throw new InvalidDialogueException("No response key for clue: " + c.getName());
            }
        }

        //TODO: add verification of noneResponses too

        return true;

    }

    public String get(Clue clue)
    {
        return get(clue.getName());
    }

    public String get(String speechKey)
    {
        if (this.jsonData.get("responses").has(speechKey)) {
            return this.jsonData.get("responses").getString(speechKey);
        } else {
            //TODO: randomise
            return this.jsonData.get("noneResponse").getString(0);
        }
    }


    class InvalidDialogueException extends Exception
    {
        //Parameterless Constructor
        public InvalidDialogueException() {}

        //Constructor that accepts a message
        public InvalidDialogueException(String message)
        {
            super(message);
        }
    }
}
