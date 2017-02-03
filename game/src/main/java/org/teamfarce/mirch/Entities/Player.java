package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Settings;
import org.teamfarce.mirch.Vector2Int;

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

        this.state = PersonState.STANDING;
        this.tileCoordinates = new Vector2Int(10,10);
        this.updatePosition();


    }

    /**
     * This Moves the player to a new tile.
     *
     * @param dir the direction that the player should move in.
     */
    public void move(Direction dir)
    {
        if (this.state != PersonState.STANDING) {
            return;
        }

        if (!getRoom().isWalkableTile(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy())) {
            //setDirection(dir);
            return;
        }

        initialiseMove(dir);
    }

}
