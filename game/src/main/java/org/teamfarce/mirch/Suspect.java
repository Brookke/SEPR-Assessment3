package org.teamfarce.mirch;

/**
 * Stores information about a single suspect character.
 *
 * @author Jacob Wunwin
 */
public class Suspect extends MapEntity {
    DialogueTree dialogueTree;
    boolean beenAccused;
    boolean isMurderer;

    /**
     * Initialiser function.
     */
    Suspect() {
        this.beenAccused = false;
        this.isMurderer = false;
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
