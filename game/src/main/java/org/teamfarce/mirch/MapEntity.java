package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class to represent different map entities.
 *
 * @author Jacob Wunwin
 */
public class MapEntity {
    int id;
    Vector2 roomPosition;
    Room currentRoom;
    String name;
    String description;

    /**
     * Initialise class.
     */
    MapEntity(){
    }

    /**
     * Returns position of room as a Vector2.
     *
     * @return The vector requested
     */
    Vector2 getPosition() {
        return this.roomPosition;
    }

    /**
     * Allows the setting of the rooms position.
     *
     * @param pos The provided position
     */
    void setPosition(Vector2 pos) {
        this.roomPosition = pos;
    }
}
