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


    }


    @Override
    public void move(Direction dir) {



        if (!getRoom().isWalkableTile(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy())) {
            this.direction = dir;
            return;
        }
        this.setTileCoordinates(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy());
        this.setPosition(this.tileCoordinates.x * Settings.TILE_SIZE, this.tileCoordinates.y * Settings.TILE_SIZE);
    }
}
