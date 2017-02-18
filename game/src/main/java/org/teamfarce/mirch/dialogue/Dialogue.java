package org.teamfarce.mirch.dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.teamfarce.mirch.Entities.Clue;

import java.util.Iterator;
import java.util.List;

/**
 * Created by brookehatton on 16/02/2017.
 */
public class Dialogue
{
    private JsonValue jsonData;
    private static String templatePath;

    public Dialogue(String jsonFile) throws InvalidDialogueException {
        importDialogue(jsonFile);


        //TODO: validate against template
        validateJsonAgainstTemplate();
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
    private boolean validateJsonAgainstTemplate() throws InvalidDialogueException {
        JsonValue jsonTemp =  new JsonReader().parse(Gdx.files.internal("template.JSON"));

            Iterator itr = jsonTemp.get("responses").iterator();
            while (itr.hasNext()) {
                String key = itr.next().toString().split(":")[0];
                if (this.jsonData.get("responses").has(key)) {
                    if (!(this.jsonData.get("responses").getString(key).length() > 0)) {
                        throw new InvalidDialogueException("No response value for key: " + key);
                    }
                } else {
                    throw new InvalidDialogueException("No response key for key: " + key);
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


    public class InvalidDialogueException extends Exception
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
