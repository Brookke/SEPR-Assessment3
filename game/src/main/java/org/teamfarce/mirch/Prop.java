package org.teamfarce.mirch;

import java.util.List;
import com.badlogic.gdx.math.Vector2;

/**
 * Creates an individual prop entity, extends from the MapEntity class.
 *
 * @author Jacob Wunwin
 */
public class Prop extends MapEntity {
    List<Clue> clue;
    public Vector2 roomPosition;
    public Room currentRoom;

    /**
     * Initialises the object
     * @param dialogue
     * @param roomPosition stores position relative to currentroom
     * @param currentRoom a ref to the current room the suspect is in
     * @param name a string for the name
     * @param description a string description 
     */
    public Prop(
        String name,
        String description,
        String filename,
        Vector2 roomPosition,
        Room currentRoom,
        List<Clue> clue
    ) {

        super(name, description, filename);
        this.clue = clue;
        this.roomPosition = roomPosition;
        this.currentRoom = currentRoom;
        this.filename = filename;
    }

    /**
     * Alternative prop constructor mostly used for testing purposes
     * @param filename
     * @param room
     * @param roomPos
     */
    public Prop(String filename, Room room, Vector2 roomPos){
        super("", "", filename);
        this.currentRoom = room;
        this.roomPosition = roomPos;
    }


    /**
     * Returns a Clue List of all clues attached to this prop.
     *
     * @return The list of clues
     */
    public List<Clue> takeClues() {
        return this.clue;
    }
}

