package org.teamfarce.mirch;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;

/**
 * Creates an individual prop entity, extends from the MapEntity class.
 *
 * @author Jacob Wunwin
 */
public class Prop extends MapEntity {
    private ArrayList<Clue> clue;
    private Vector2 roomPosition;
    private Room currentRoom;
    
    /**
     * Initialises the object
     * @param id integer for identification
     * @param resourceIndex index for resources like sprites
     * @param dialogue
     * @param roomPosition stores position relative to currentroom
     * @param currentRoom a ref to the current room the suspect is in
     * @param name a string for the name
     * @param description a string description 
     */
    public Prop(
        int id,
        int resourceIndex, 
        DialogueBox dialogue,
        String name,
        String description,
        String filename,
        Vector2 roomPosition,
        Room currentRoom,
        ArrayList<Clue> clue
    ) {
    	
        super(id, resourceIndex, dialogue, name, description, filename);
        this.clue = clue;
    	this.roomPosition = roomPosition;
    	this.currentRoom = currentRoom;
    	this.filename = filename;
    }

    /**
     * Returns a Clue ArrayList of all clues attached to this prop.
     *
     * @return The list of clues
     */
    public ArrayList<Clue> takeClue() {
        return this.clue;
    }
}

