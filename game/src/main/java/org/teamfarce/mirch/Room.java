package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;


/**
 * Represents a room in the map.
 */
public class Room extends Sprite
{
    /**
     * This is the amount of pixels for a single room unit.
     */
    public static final float pixelsPerUnit = 160.0f;

    /**
     * The position of origin of this room
     */

    /**
     * The filename of the image for the background of this room.
     */
    public String filename;

    /**
     * Initialiser Function for Room Class.
     *
     * @param filename The filename of the background image.
     * @param position  The position to set.
     */
    public Room(String filename, Vector2 position)
    {
        super(new Texture(Gdx.files.internal("rooms/" + filename)));
        this.filename = filename;
        this.setPosition(position.x, position.y);
    }

    /**
     * This constructs a new room with a position in Room Units.
     *
     * @param filename The filename of the background image.
     * @param position The position to scale and then set.
     * @return The new room.
     */
    static Room constructWithUnitSizes(String filename, Vector2 position)
    {
        return new Room(filename, position.scl(pixelsPerUnit));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        } else if (obj instanceof Room) {
            Room r = (Room) obj;
            return r.getX() == this.getX() && r.getY() == this.getY();
        }

        return false;
    }
}
