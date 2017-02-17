package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.dialogue.DialogueTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information about a single suspect character.
 */
public class Suspect extends AbstractPerson
{
    public List<Clue> relatedClues;
    public boolean isMurderer;
    /**
     * The dialogue tree of this suspect.
     */
    public String dialogueFile;
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
     * @param dialogueFile     The json file containing the suspects dialogue.
     */
    public Suspect(
            String name,
            String description,
            String filename,
            Vector2Int startingPosition,
            String dialogueFile
    )
    {
        super(name, description, filename);

        this.beenAccused = false;
        this.isMurderer = false;
        this.setTileCoordinates(startingPosition.x, startingPosition.y);
        this.moveStep = new Vector2(0, 0);
        this.dialogueFile = dialogueFile;
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
            MIRCH.me.gameSnapshot.modifyScore(-50);
        } else {
            MIRCH.me.gameSnapshot.modifyScore(100);
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
