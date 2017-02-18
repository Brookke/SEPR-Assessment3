package org.teamfarce.mirch.dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.teamfarce.mirch.entities.Clue;

import java.util.Iterator;

/**
 * Created by brookehatton on 16/02/2017.
 */
public class Dialogue
{
    private JsonValue jsonData;
    private static String templatePath;
    private boolean isForPlayer;

    public Dialogue(String jsonFile, boolean player) throws InvalidDialogueException {
        importDialogue(jsonFile);
        isForPlayer = player;

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
        jsonData = new JsonReader().parse(Gdx.files.internal("characters/" + fileName));
    }

    /**
     * This takes a template file and validates that there exists a valid response like the templates.
     * @return Boolean true if it passes
     * @throws InvalidDialogueException if the JSON is invalid
     */
    private boolean validateJsonAgainstTemplate() throws InvalidDialogueException {
        JsonValue jsonTemp =  new JsonReader().parse(Gdx.files.internal("characters/template.JSON"));
        Iterator itr = jsonTemp.get("responses").iterator();



        if (isForPlayer) {
            while (itr.hasNext()) {
                String key = itr.next().toString().split(":")[0];
                if (this.isForPlayer && this.jsonData.get("responses").get(key).size != 3) {
                    if (!key.equals("introduction")) {
                        throw new InvalidDialogueException("No question values for the key: " + key + ", in the player json");
                    }
                }
            }

        } else {
            while (itr.hasNext()) {
                String key = itr.next().toString().split(":")[0];
                if (!this.jsonData.get("responses").has(key)) {
                    throw new InvalidDialogueException("No response key for key: " + key);
                }
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
            return this.jsonData.get("noneResponses").getString(0);
        }
    }

    public String get(Clue clue, String personality)
    {
        if (isForPlayer) {
            return this.jsonData.get("responses").get(clue.getName()).getString(personality);
        }

        if (personality.toLowerCase().equals(jsonData.getString("personality").toLowerCase())) {
            return get(clue.getName());
        } else {
            return "";
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
