package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a room in the map.
 */
public class Room {
    /**
     * This is the amount of pixels for a single room unit.
     */
    public static final float pixelsPerUnit = 160.0f;

    /**
     * The position of origin of this room
     */
    public Vector2 position;

    /**
     * The filename of the image for the background of this room.
     */
    public String filename;

    /**
     * Initialiser Function for Room Class.
     *
     * @param filename The filename of the background image.
     * @param postion The position to set.
     */
    Room(String filename, Vector2 position) {
        this.filename = filename;
        this.position = position;
    }

    /**
     * This constructs a new room with a position in Room Units.
     *
     * @param filename The filename of the background image.
     * @param position The position to scale and then set.
     * @return The new room.
     */
    static Room constructWithUnitSizes(String filename, Vector2 position) {
        return new Room(filename, position.scl(pixelsPerUnit));
    }
}
