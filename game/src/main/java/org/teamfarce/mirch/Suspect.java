package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores information about a single suspect character.
 *
 * @author Jacob Wunwin
 */
public class Suspect extends MapEntity {
    DialogueTree dialogueTree;
    boolean beenAccused;
    boolean isMurderer;
    Vector2 mapPosition;
    public Vector2 moveStep;
    
    /**
     * Initialiser function.
     */
    Suspect(String filename, Vector2 startingPosition) {
    	this.filename = filename;
        this.beenAccused = false;
        this.isMurderer = false;
        this.mapPosition = startingPosition;
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
    boolean accuse(boolean hasEvidence) {
        return false;
    }
}
