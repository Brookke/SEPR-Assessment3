package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.dialogue.DialogueTree;

/**
 * Stores information about a single suspect character.
 */
public class Suspect extends MapEntity {
    private boolean beenAccused;
    boolean isMurderer;

    /**
     * The dialogue tree of this suspect.
     */
    public DialogueTree dialogueTree;

    /**
     * The position of the suspect on the map.
     */
    public Vector2 mapPosition;

    /**
     * The size of this suspect's step.
     */
    public Vector2 moveStep;

    /**
     * Initialiser function.
     *
     * @param name A string for the name
     * @param description A string description 
     * @param filename The filename of the image for this suspect.
     * @param startingPosition The position to start at.
     * @param dialogueTree The dialogue tree for this suspect.
     */
    public Suspect(
        String name,
        String description,
        String filename,
        Vector2 startingPosition,
        DialogueTree dialogueTree
    ) {
        super(name, description, filename);

        this.beenAccused = false;
        this.isMurderer = false;
        this.mapPosition = startingPosition;
        this.moveStep = new Vector2(0, 0);
        this.dialogueTree = dialogueTree;
    }

    /**
     * Initialiser function.
     *
     * @param filename The filename of the image for this suspect.
     * @param pos The position to start at.
     */
    public Suspect(String filename, Vector2 pos){
        super(null, null, filename);
        this.mapPosition = pos;
    };

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
    boolean accuse(boolean hasEvidence) {
        this.beenAccused = true;
        //clear the dialogue tree here
        return (this.isMurderer)&&(hasEvidence);
    }

    /**
     * Return whether the suspect has been accused.
     *
     * @return Whether the suspect has been accused.
     */
    public boolean hasBeenAccused() {
        return beenAccused;
    }

    /**
     * Return the position of the suspect.
     *
     * @return The position of the suspect.
     */
    public Vector2 getPosition(){
        return this.mapPosition;
    }
}
