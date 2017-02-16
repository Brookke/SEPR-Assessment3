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

    public Dialogue(String jsonFile, List<Clue> clues) {
        importDialogue(jsonFile);

    }

    /**
     * Reads in the JSON file and stores dialogue in the dialogue HashMap
     *
     * @param fileName - The filename to read from
     */
    public void importDialogue(String fileName)
    {
        jsonData = new JsonReader().parse(Gdx.files.internal(fileName));
    }

    public boolean validateJsonAgainstClues(List<Clue> clues) throws InvalidDialogueException {
        for (Clue c: clues) {
            if (this.jsonData.get("responses").has(c.getName())) {
                if (!(this.jsonData.get("responses").get(c.getName()).toString().length() > 0)) {
                    throw new InvalidDialogueException("No response value for clue: " + c.getName());
                }
            } else {
                throw new InvalidDialogueException("No response key for clue: " + c.getName());
            }
        }

        return true;

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
