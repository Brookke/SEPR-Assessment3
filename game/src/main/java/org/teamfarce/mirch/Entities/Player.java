package org.teamfarce.mirch.Entities;

import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.MapScreen;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.map.Room;

import java.util.Arrays;
import java.util.List;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class Player extends AbstractPerson
{
    /**
     * This variable stores the NPC that the person will talk to when they finish walking. It is null if nothing should happen
     */
    Suspect talkToOnEnd = null;

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
    public Player(MIRCH game, String name, String description, String filename)
    {
        super(game, name, description, filename);

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
            direction = dir;

            if (!toMoveTo.isEmpty())
            {
                toMoveTo = aStarPath(toMoveTo.get(toMoveTo.size() - 1));
            }

            return;
        }

        initialiseMove(dir);
    }


    public void interact(Vector2Int tileLocation)
    {
        //Check to see if a person is standing at that location
        //Then we'll want to walk towards them (or next to them)

        for (Suspect s : ((MapScreen) game.guiController.mapScreen).getNPCs())
        {
            if (s.getTileCoordinates().equals(tileLocation) && s.getState() != PersonState.WALKING)
            {
                toMoveTo = aStarPath(getClosestNeighbour(s.getTileCoordinates()));
                s.canMove = false;
                talkToOnEnd = s;
                return;
            }
        }

        //if (clue is at location)
        //{
        //   move next to it
        //}

        if (!getRoom().isWalkableTile(tileLocation.getX(), tileLocation.getY()))
        {
            return;
        }

        toMoveTo = aStarPath(tileLocation);
    }

    /**
     * This method checks what the best fit neighbour tile is for a goal.
     *
     * It bases its decision on the players location and what tiles are not locked
     *
     * @param goal - The goal destination
     * @return - The best fitting tile
     */
    public Vector2Int getClosestNeighbour(Vector2Int goal)
    {
        Vector2Int result = null;

        Vector2Int north = new Vector2Int(goal.getX() + 1, goal.getY() + 1);
        Vector2Int east = new Vector2Int(goal.getX() + 1, goal.getY());
        Vector2Int south = new Vector2Int(goal.getX(), goal.getY() - 1);
        Vector2Int west = new Vector2Int(goal.getX() - 1, goal.getY());

        if (!getRoom().isWalkableTile(north.getX(), north.getY()))
        {
            north = null;
        }

        if (!getRoom().isWalkableTile(east.getX(), east.getY()))
        {
            east = null;
        }

        if (!getRoom().isWalkableTile(south.getX(), south.getY()))
        {
            south = null;
        }

        if (!getRoom().isWalkableTile(west.getX(), west.getY()))
        {
            west = null;
        }

        //Work out whether the player is further, east, north, south or west of the goal
        int xDist = getTileX() - goal.getX();
        //- means west of goal

        int yDist = getTileY() - goal.getY();
        //- means south of goal

        int absX = Math.abs(xDist);
        int absY = Math.abs(yDist);

        Vector2Int[] priority = new Vector2Int[]{null, null, null, null};

        if (absX >= absY)
        {
            if (xDist < 0)
            {
                priority[0] = west;
                priority[3] = east;

                if (yDist <= 0)
                {
                    priority[1] = south;
                    priority[2] = north;
                }
                else
                {
                    priority[1] = north;
                    priority[2] = south;
                }
            }
            else
            {
                priority[0] = east;
                priority[3] = west;

                if (yDist <= 0)
                {
                    priority[1] = south;
                    priority[2] = north;
                }
                else
                {
                    priority[1] = north;
                    priority[2] = south;
                }
            }
        }
        else if (absY > absX)
        {
            if (yDist < 0)
            {
                priority[0] = south;
                priority[3] = north;

                if (xDist <= 0)
                {
                    priority[1] = west;
                    priority[2] = east;
                }
                else
                {
                    priority[1] = west;
                    priority[2] = east;
                }
            }
            else
            {
                priority[0] = north;
                priority[3] = south;

                if (xDist <= 0)
                {
                    priority[1] = west;
                    priority[2] = east;
                }
                else
                {
                    priority[1] = west;
                    priority[2] = east;
                }
            }
        }

        for (Vector2Int v : priority)
        {
            if (v != null)
            {
                return v;
            }
        }

        return null;
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

    @Override
    public void finishMove() {
        super.finishMove();

        if (toMoveTo.isEmpty() && talkToOnEnd != null)
        {
            talkToOnEnd.setDirection(getTileCoordinates().dirBetween(talkToOnEnd.getTileCoordinates()));
            setDirection(talkToOnEnd.direction.getOpposite());

            //THIS IS WHERE YOU WILL BEGIN A CHAT WITH THE NPC
        }
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
                direction = newRoomData.newDirection;
                this.updateTextureRegion();
            }

            this.setTileCoordinates(newRoomData.newTileCoordinates.x, newRoomData.newTileCoordinates.y);
        }
    }

}
