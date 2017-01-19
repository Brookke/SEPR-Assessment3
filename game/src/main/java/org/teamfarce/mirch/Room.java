package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a room in the map.
 *
 * @author Jacob Wunwin
 */
public class Room {
    Vector2 position;
    Vector2 size;
    int id;
    String filename;

    /**
     * Initialiser Function for Room Class.
     */
    Room(String file, Vector2 position) {
    	this.filename = file;
    	this.position = position;
    }
}
