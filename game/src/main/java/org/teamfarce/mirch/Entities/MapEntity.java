package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Room;

/**
 * Base class to represent different map entities.
 */
public class MapEntity
{
    protected Vector2 roomPosition;
    protected Room currentRoom;
    protected String name;
    protected String description;
    protected String filename;

    /**
     * Initialise the entity.
     *
     * @param name        The name of the entity.
     * @param description The description of the entity.
     * @param filename    The filename of the image to display for the entity.
     */
    public MapEntity(
            String name,
            String description,
            String filename
    )
    {
        this.name = name;
        this.description = description;
        this.filename = filename;
    }

    /**
     * Returns position of room as a Vector2.
     *
     * @return The room's position.
     */
    public Vector2 getPosition()
    {
        return this.roomPosition;
    }

    /**
     * Allows the setting of the entity's position.
     *
     * @param position The provided position
     */
    public void setPosition(Vector2 position)
    {
        this.roomPosition = position;
    }

    /**
     * Gets the current room the entity is within.
     *
     * @return The entity's current room.
     */
    public Room getRoom()
    {
        return this.currentRoom;
    }

    /**
     * Set the room of the entity.
     *
     * @param room The room to set.
     */
    public void setRoom(Room room)
    {
        this.currentRoom = room;
    }

    /**
     * Get the name of the entity.
     *
     * @return The entity's name.
     */
    public String getName()
    {
        return this.name;
    }

    public String getFilename()  {
        return this.filename;
    }

    /**
     * Get the description.
     *
     * @return The entity's description.
     */
    public String getDescription()
    {
        return this.description;
    }
}
