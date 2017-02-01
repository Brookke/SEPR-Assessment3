package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by brookehatton on 01/02/2017.
 */
public abstract class AbstractPerson extends MapEntity
{

    float speed = 3;

    /**
     * Initialise the entity.
     *
     * @param name        The name of the entity.
     * @param description The description of the entity.
     * @param filename    The filename of the image to display for the entity.
     */
    public AbstractPerson(String name, String description, String filename)
    {
        super(name, description, filename);
    }

    /**
     * This controls the movement of a person
     * @param move
     */
    public abstract void move(Vector2 move);


}
