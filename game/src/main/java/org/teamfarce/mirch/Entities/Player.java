package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.MapScreen;
import org.teamfarce.mirch.Settings;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.map.Room;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class Player extends AbstractPerson
{

    /**
     * This variable is detected by the mapScreen and initialises the room change
     */
    public boolean roomChange = false;

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

        if (this.isOnTriggerTile() && dir.toString().equals(getRoom().getMatRotation(this.tileCoordinates.x, this.tileCoordinates.y))) {
            roomChange = true;
            return;
        }

        if (!getRoom().isWalkableTile(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy())) {
            //setDirection(dir);
            return;
        }

        initialiseMove(dir);
    }

    /**
     * This method returns whether or not the player is standing on a tile that initiates a Transition to another room
     *
     * @return (boolean) Whether the player is on a trigger tile or not
     */
    public boolean isOnTriggerTile()
    {
        return this.getRoom().isTriggerTile(this.tileCoordinates.x, this.tileCoordinates.y);
    }


    /**
     * This takes the player at its current position, and automatically gets the transition data for the next room and applies it to the player and game
     */
    public void moveRoom()
    {
        if (isOnTriggerTile()) {
            Room.Transition newRoomData = this.getRoom().getTransitionData(this.getTileCoordinates().x, this.getTileCoordinates().y);

            this.setRoom(newRoomData.getNewRoom());


            if (newRoomData.newDirection != null) {
                //this.setDirection(newRoomData.newDirection);
                //this.updateTextureRegion();
            }

            this.setTileCoordinates(newRoomData.newTileCoordinates.x, newRoomData.newTileCoordinates.y);
        }
    }
}
