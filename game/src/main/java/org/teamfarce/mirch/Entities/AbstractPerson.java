package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Settings;
import org.teamfarce.mirch.Vector2Int;

import java.util.Comparator;

/**
 * Created by brookehatton on 01/02/2017.
 */
public abstract class AbstractPerson extends MapEntity
{


    Direction direction;
    PersonState state;
    private Vector2Int startTile = new Vector2Int(0,0);
    private Vector2Int endTile = new Vector2Int(0,0);
    private float animTimer;
    private float animTime = 0.35f;

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
     */
    public abstract void move(Direction dir);

    public PersonState getState()
    {
        return state;
    }
    

    /**
     * This class is to sort a list of AbstractPerson in highest Y coordinate to lowest Y coordinate
     * <p>
     * It is used to render NPCs and the Player in the correct order to avoid it appearing as though someone
     * is standing on top of someone else
     */
    public static class PersonPositionComparator implements Comparator<AbstractPerson>
    {
        /**
         * This method compares the 2 objects.
         *
         * @param o1 - The first object to compare
         * @param o2 - The second object to compare
         * @return (int) if <0 o1 is considered to be first in the list
         */
        @Override
        public int compare(AbstractPerson o1, AbstractPerson o2)
        {
            return o2.getTileCoordinates().y - o1.getTileCoordinates().y;
        }
    }


    /**
     * This is called to update the players position.
     * Called from the game loop, it interpolates the movement so that the person moves smoothly from tile to tile.
     */
    public void update(float delta)
    {
        if (this.state == PersonState.WALKING) {

            this.setPosition(Interpolation.linear.apply(startTile.x * Settings.TILE_SIZE, endTile.x * Settings.TILE_SIZE, animTimer / animTime), Interpolation.linear.apply(startTile.y * Settings.TILE_SIZE, endTile.y * Settings.TILE_SIZE, animTimer / animTime));

            this.animTimer += delta;

            if (animTimer > animTime) {
                this.setTileCoordinates(endTile.x, endTile.y);
                this.finishMove();
            }
        }

    }

    /**
     * Sets up the move, initialising the start position and destination as well as the state of the person.
     * This allows the movement to be smooth and fluid.
     *
     * @param dir the direction that the person is moving in.
     */
    public void initialiseMove(Direction dir)
    {
        getRoom().lockCoordinate(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy());

        this.direction = dir;

        this.startTile.x = this.tileCoordinates.x;
        this.startTile.y = this.tileCoordinates.y;

        this.endTile.x = this.startTile.x + dir.getDx();
        this.endTile.y = this.startTile.y + dir.getDy();
        this.animTimer = 0f;

        this.state = PersonState.WALKING;
    }



    /**
     * Finalises the move by resetting the animation timer and setting the state back to standing.
     * Called when the player is no longer moving.
     */
    public void finishMove()
    {
        animTimer = 0f;

        this.state = PersonState.STANDING;

        getRoom().unlockCoordinate(tileCoordinates.x, tileCoordinates.y);

        //updateTextureRegion();
    }

    /**
     * The state of the person explains what they are currently doing.
     * <li>{@link #WALKING}</li>
     * <li>{@link #STANDING}</li>
     */
    public enum PersonState
    {
        /**
         * Person is walking.
         */
        WALKING,

        /**
         * Person is standing still.
         */
        STANDING;
    }



}
