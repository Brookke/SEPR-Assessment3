package org.teamfarce.mirch.entities;

import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.screens.PuzzleScreen;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.map.Room;
import org.teamfarce.mirch.screens.MapScreen;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class Player extends AbstractPerson {
    /**
     * This variable is detected by the mapScreen and initialises the room change
     */
    public boolean roomChange = false;
    /**
     * This variable stores the NPC that the person will talk to when they finish walking. It is null if nothing should happen
     */
    Suspect talkToOnEnd = null;
    /**
     * This variable stores a clue that has been clicked on. It is to be collected when the currentPlayer arrives at the clue
     */
    Clue findOnEnd = null;
    /**
     * This variable stores a goal location to be tracked to the next time the currentPlayer completes a move
     */
    private Vector2Int trackToNext = null;

    /**
     * This boolean stores whether the currentPlayer is to transition to the next room or not when they finish walking
     */
    private boolean transitionOnEnd = false;

    /**
     * Initialise the entity.
     *
     * @param name            The name of the entity.
     * @param description     The description of the entity.
     * @param spriteSheetFile The spriteSheetFile of the image to display for the entity.
     */
    public Player(MIRCH game, String name, String description, String spriteSheetFile, Dialogue dialogue) {
        super(game, name, description, spriteSheetFile, dialogue);

        this.state = PersonState.STANDING;
    }

    /**
     * This Moves the currentPlayer to a new tile.
     *
     * @param dir the direction that the currentPlayer should move in.
     */
    public void move(Direction dir) {
        if (this.state != PersonState.STANDING) {
            return;
        }

        if (this.isOnTriggerTile() && dir.toString().equals(getRoom().getMatRotation(this.tileCoordinates.x, this.tileCoordinates.y))) {
            roomChange = true;
            return;
        }

        if (!getRoom().isWalkableTile(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy())) {
            direction = dir;

            /**
             * If they are currently tracking to somewhere and they get interrupted, rerun the path finding algorithm
             */
            if (!toMoveTo.isEmpty()) {
                toMoveTo = aStarPath(toMoveTo.get(toMoveTo.size() - 1));
            }

            return;
        }

        initialiseMove(dir);
    }

    /**
     * This method is called when the currentPlayer clicks on the screen.
     *
     * This is handled in the PlayerController and passed to here
     *
     * @param tileLocation - The tile location they clicked at.
     */
    public void interact(Vector2Int tileLocation) {
        safeInteract(tileLocation, 0);
    }

    /**
     * This loopCounter is here because sometimes the interact and finishMove methods become stuck in an infinite loop with each other.
     * @param tileLocation
     * @param loopCounter
     */
    private void safeInteract(Vector2Int tileLocation, int loopCounter) {
        loopCounter++;
        if (loopCounter > 5) {
            System.out.println("WARN: Terminating safeInteract because loop counter exceeded maximum");
            return;
        }

        if (talkToOnEnd != null) {
            talkToOnEnd.canMove = true;
        }

        talkToOnEnd = null;
        findOnEnd = null;
        transitionOnEnd = false;

        if (getState() == PersonState.WALKING) {
            trackToNext = tileLocation;
            return;
        }

        if (getRoom().isTriggerTile(tileLocation.getX(), tileLocation.getY())) {
            transitionOnEnd = true;
            toMoveTo = aStarPath(tileLocation);

            if (toMoveTo.isEmpty()) safeFinishMove(loopCounter);

            return;
        }

        for (Suspect s : ((MapScreen) game.getGUIController().mapScreen).getNPCs()) {
            if (s.getTileCoordinates().equals(tileLocation) && s.getState() != PersonState.WALKING) {
                toMoveTo = aStarPath(getClosestNeighbour(s.getTileCoordinates()));

                if (!toMoveTo.isEmpty()) {
                    s.canMove = false;
                    talkToOnEnd = s;
                    return;
                }
            }
        }

        for (Clue c : getRoom().getClues()) {
            if (c.getTileCoordinates().equals(tileLocation)) {
                toMoveTo = aStarPath(getClosestNeighbour(c.getTileCoordinates()));

                findOnEnd = c;
            }
        }

        if (!getRoom().isWalkableTile(tileLocation.getX(), tileLocation.getY())) {
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
    public Vector2Int getClosestNeighbour(Vector2Int goal) {

        Vector2Int north = new Vector2Int(goal.getX(), goal.getY() + 1);
        Vector2Int east = new Vector2Int(goal.getX() + 1, goal.getY());
        Vector2Int south = new Vector2Int(goal.getX(), goal.getY() - 1);
        Vector2Int west = new Vector2Int(goal.getX() - 1, goal.getY());

        if (!getRoom().isWalkableTile(north)) {
            north = null;
        }

        if (!getRoom().isWalkableTile(east)) {
            east = null;
        }

        if (!getRoom().isWalkableTile(south)) {
            south = null;
        }

        if (!getRoom().isWalkableTile(west)) {
            west = null;
        }

        //Work out whether the currentPlayer is further, east, north, south or west of the goal
        int xDist = getTileX() - goal.getX();
        //- means west of goal

        int yDist = getTileY() - goal.getY();
        //- means south of goal

        int absX = Math.abs(xDist);
        int absY = Math.abs(yDist);

        Vector2Int[] priority = new Vector2Int[]{null, null, null, null};

        if (absX >= absY) {
            priority = lessThanPriorityDecision(xDist, priority, 0, 3, west, east);
            priority = lessThanPriorityDecision(yDist, priority, 1, 2, south, north);
        } else if (absY > absX) {
            priority = lessThanPriorityDecision(yDist, priority, 0, 3, south, north);
            priority = lessThanPriorityDecision(xDist, priority, 1, 2, west, east);
        }

        for (Vector2Int v : priority) {
            if (v != null) {
                return v;
            }
        }

        return null;
    }

    /**
     * This method checks the coordinate and places the directions as a priority in a list. This is used by the `getClosestNeighbour` method
     *
     * @param check              - The number to check is less than 0
     * @param priority           - The priority list
     * @param priorityListFirst  - The first position to place a direction in
     * @param priorityListSecond - The second position to place a direction in
     * @param first              - The first priority direction
     * @param second             - The second priority direction
     * @return Vector2Int the priority list back after being modified
     */
    private Vector2Int[] lessThanPriorityDecision(int check, Vector2Int[] priority, int priorityListFirst, int priorityListSecond, Vector2Int first, Vector2Int second) {
        if (check <= 0) {
            priority[priorityListFirst] = first;
            priority[priorityListSecond] = second;
        } else {
            priority[priorityListFirst] = second;
            priority[priorityListSecond] = first;
        }

        return priority;
    }

    /**
     * This method returns whether or not the currentPlayer is standing on a tile that initiates a Transition to another room
     *
     * @return (boolean) Whether the currentPlayer is on a trigger tile or not
     */
    public boolean isOnTriggerTile() {
        return this.getRoom().isTriggerTile(this.tileCoordinates.x, this.tileCoordinates.y);
    }

    @Override
    public void finishMove() {
        super.finishMove();
        safeFinishMove(0);
    }

    private void safeFinishMove(int loopCounter) {
        loopCounter++;

        if (toMoveTo.isEmpty() && talkToOnEnd != null) {
            System.out.println("talkToOnEnd != null");
            talkToOnEnd.setDirection(getTileCoordinates().dirBetween(talkToOnEnd.getTileCoordinates()));
            setDirection(talkToOnEnd.direction.getOpposite());

            game.getGameSnapshot().setState(GameState.interviewStart);
            game.getGameSnapshot().setSuspectForInterview(talkToOnEnd);
        }

        if (toMoveTo.isEmpty() && findOnEnd != null) {
            System.out.println("findOnEnd != null");
            setDirection(findOnEnd.getTileCoordinates().dirBetween(getTileCoordinates()));
            MapScreen.grabScreenshot = true;
            game.getGameSnapshot().setState(GameState.findClue);
        }

        if (toMoveTo.isEmpty() && transitionOnEnd) {
            System.out.println("transitionOnEnd");
            if (getRoom().isTriggerTile(getTileX(), getTileY())) {
                System.out.println("trigger tile");
                // Protect against bug where mat directions are sometimes not found
                Direction matDirection = Direction.valueOf(getRoom().getMatRotation(getTileX(), getTileY()));
                if (matDirection != null) {
                    setDirection(matDirection);
                    roomChange = true;
                }
            }
        }

        if (trackToNext != null) {
            System.out.println("trackToNext != null");
            safeInteract(trackToNext, loopCounter);
            trackToNext = null;
        }
    }

    /**
     * This takes the currentPlayer at its current position, and automatically gets the transition data for the next room and applies it to the currentPlayer and game
     */
    public void moveRoom() {
        if (isOnTriggerTile()) {
            Room.Transition newRoomData = this.getRoom().getTransitionData(this.getTileCoordinates().x, this.getTileCoordinates().y);

            this.setRoom(newRoomData.getNewRoom());

            if (newRoomData.newDirection != null) {
                direction = newRoomData.newDirection;
                this.updateTextureRegion();
            }

            this.setTileCoordinates(newRoomData.newTileCoordinates.x, newRoomData.newTileCoordinates.y);
            if(newRoomData.getNewRoom().getName().equals("Secret Lab")){
                // ^need added expression for puzzle being completed, global or getter
                if (game.getGameSnapshot().puzzleGame.getPuzzleWon() == false) {
                    game.getGameSnapshot().setState(GameState.puzzleStart);
                }
            }
        }
    }

    /**
     * This method gets the clue that has just been found by the currentPlayer
     *
     * @return Clue - The found clue
     */
    public Clue getClueFound() {
        return findOnEnd;
    }

    /**
     * This method clears the NPC that is to be spoken to when movement ends
     */
    public void clearTalkTo() {
        talkToOnEnd = null;
    }

    /**
     * This method clears the clue that has just been found
     */
    public void clearFound() {
        findOnEnd = null;
    }
}
