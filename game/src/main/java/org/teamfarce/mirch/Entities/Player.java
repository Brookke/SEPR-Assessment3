package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Screens.AbstractScreen;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class Player extends AbstractPerson
{


    /**
     * Initialise the entity.
     *
     * @param name        The name of the entity.
     * @param description The description of the entity.
     * @param filename    The filename of the image to display for the entity.
     */
    public Player(String name, String description, String filename)
    {
        super(name, description, filename);

    }

    public void move(Vector2 move) {

        move.scl(speed);
        this.translate(move.x, move.y);
    }
}
