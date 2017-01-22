package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.dialogue.DialogueTree;

/**
 * Stores information about a single suspect character.
 *
 * @author Jacob Wunwin
 */
public class Suspect extends MapEntity {
    DialogueTree dialogueTree;
    private boolean beenAccused;
    boolean isMurderer;
    Vector2 mapPosition;
    Vector2 moveStep;

    /**
     * Initialiser function
     * @param dialogue
     * @param roomPosition stores position relative to currentroo perm
     * @param currentRoom a ref to the current room the suspect is in
     * @param name a string for the name
     * @param description a string description 
     */
    public Suspect(
        DialogueBox dialogue,
        String name,
        String description,
        String filename,
        Vector2 startingPosition,
        DialogueTree dialogueTree
    ) {
        super(name, description, filename);

        this.dialogueTree = dialogueTree;
        this.beenAccused = false;
        this.isMurderer = false;
        this.mapPosition = startingPosition;
        this.moveStep = new Vector2(0, 0);
        this.dialogueTree = dialogueTree;
    }

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

	public boolean hasBeenAccused() {
		return beenAccused;
	}
	
	public Vector2 getPosition(){
		return this.mapPosition;
	}


}
