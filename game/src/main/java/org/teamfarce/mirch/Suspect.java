package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores information about a single suspect character.
 *
 * @author Jacob Wunwin
 */
public class Suspect extends MapEntity {
    DialogueTree dialogueTree;
    private boolean beenAccused;
    private boolean isMurderer;
    Vector2 mapPosition;
    Vector2 moveStep;
    
//constructors, accessor functions, and a couple of rudimentary implementation of key functions for mapentity, suspect, and prop classes.
    /**
     * Initialiser function
     * @param id integer for identification
     * @param resourceIndex index for resources like sprites
     * @param dialogue
     * @param roomPosition stores position relative to currentroom
     * @param currentRoom a ref to the current room the suspect is in
     * @param name a string for the name
     * @param description a string description 
     */
    public Suspect(
        int id,
        int resourceIndex,
        DialogueBox dialogue,  
        String name, 
        String description,
        String filename,
        Vector2 startingPosition,
        DialogueTree dialogueTree
        ) {
    	super(id, resourceIndex, dialogue, name, description, filename);
    	
    	this.dialogueTree = dialogueTree;
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
        return this.isMurderer&&!this.beenAccused&&hasEvidence;
    }
}
