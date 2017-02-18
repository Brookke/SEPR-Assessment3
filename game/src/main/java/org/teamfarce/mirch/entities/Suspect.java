package org.teamfarce.mirch.entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.dialogue.Dialogue;

import java.util.List;
import java.util.Random;

/**
 * Stores information about a single suspect character.
 */
public class Suspect extends AbstractPerson
{
    private Random random = new Random();

    public List<Clue> relatedClues;
    public boolean isMurderer;
    /**
     * The position of the suspect on the map.
     */
    /**
     * The size of this suspect's step.
     */
    public Vector2 moveStep;
    private boolean beenAccused;

    private boolean killer = false;
    private boolean victim = false;
    /**
     * Initialiser function.
     *
     * @param name             A string for the name
     * @param description      A string description
     * @param filename         The filename of the image for this suspect.
     * @param startingPosition The position to start at.
     * @param dialogue    The json file containing the suspects dialogue.
     */
    public Suspect(
            MIRCH game,
            String name,
            String description,
            String filename,
            Vector2Int startingPosition,
            Dialogue dialogue
    )
    {
        super(game, name, description, filename, dialogue);

        this.beenAccused = false;
        this.isMurderer = false;
        this.setTileCoordinates(startingPosition.x, startingPosition.y);
        this.moveStep = new Vector2(0, 0);
    }


    /**
     * Accuse the suspect.
     * <p>
     * This should take into account whether the player has sufficient evidence and whether the
     * suspect is actually the murderer.
     * </p>
     *
     * @param hasEvidence Whether the player has sufficient evidence the accuse
     * @return Whether the player has successfully accused the suspect
     */
    public boolean accuse(boolean hasEvidence)
    {
        this.beenAccused = true;
        //clear the dialogue tree here
        if (this.isMurderer == false || hasEvidence == false) {
            game.gameSnapshot.modifyScore(-50);
        } else {
            game.gameSnapshot.modifyScore(100);
        }
        return (this.isMurderer) && (hasEvidence);
    }

    /**
     * Return whether the suspect has been accused.
     *
     * @return Whether the suspect has been accused.
     */
    public boolean hasBeenAccused()
    {
        return beenAccused;
    }

    @Override
    public void move(Direction dir)
    {
        if (this.state != PersonState.STANDING) {
            return;
        }

        if (!canMove) return;

        if (!getRoom().isWalkableTile(this.tileCoordinates.x + dir.getDx(), this.tileCoordinates.y + dir.getDy())) {
            setDirection(dir);
            return;
        }

        initialiseMove(dir);
    }

    /**
     * This method is called once a game tick to randomise movement.
     */
    @Override
    public void update(float delta)
    {
        super.update(delta);
        this.randomMove();
    }

    /**
     * This method attempts to move the NPC in a random direction
     */
    private void randomMove()
    {
        if (getState() == PersonState.WALKING) return;

        if (random.nextDouble() > 0.01) {
            return;
        }

        Direction dir;

        Double dirRand = random.nextDouble();
        if (dirRand < 0.5) {
            dir = this.direction;
        } else if (dirRand < 0.62) {
            dir = Direction.NORTH;
        } else if (dirRand < 0.74) {
            dir = Direction.SOUTH;
        } else if (dirRand < 0.86) {
            dir = Direction.EAST;
        } else {
            dir = Direction.WEST;
        }

        move(dir);
    }

    public boolean isKiller() {
        return killer;
    }

    public void setKiller() {
        this.killer = true;
        this.victim = false;
    }

    public boolean isVictim() {
        return victim;
    }

    public void setVictim() {
        this.victim = true;
        this.killer = false;
    }
}
