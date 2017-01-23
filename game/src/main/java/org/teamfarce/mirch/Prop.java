package org.teamfarce.mirch;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;

/**
 * Creates an individual prop entity, extends from the MapEntity class.
 */
public class Prop extends MapEntity {
    public List<Clue> clues;

    /**
     * The current position of the prop in the room.
     */
    public Vector2 roomPosition;

    /**
     * The room the prop current resides in.
     */
    public Room currentRoom;

    /**
     * Initialises the object.
     *
     * @param name The name for the prop.
     * @param description A description of the prop.
     * @param filename The filename of the image of the prop.
     * @param roomPosition The position in the room.
     * @param currentRoom The current room the prop is in.
     * @param clues The clues this prop provides.
     */
    public Prop(
        String name,
        String description,
        String filename,
        Vector2 roomPosition,
        Room currentRoom,
        List<Clue> clues
    ) {
        super(name, description, filename);
        this.clues = clues;
        this.roomPosition = roomPosition;
        this.currentRoom = currentRoom;
        this.filename = filename;
    }

    /**
     * Alternative prop constructor mostly used for testing purposes.
     *
     * @param filename The filename of the image of the prop.
     * @param room The room to start in.
     * @param roomPos The position in the room to start at.
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
        return this.clues;
    }
}

