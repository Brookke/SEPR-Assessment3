package org.teamfarce.mirch;

import java.util.ArrayList;

/**
 * Creates an individual prop entity, extends from the MapEntity class.
 *
 * @author Jacob Wunwin
 */
public class Prop extends MapEntity {
    ArrayList<Clue> clue;

    /**
     * Initialises the object.
     */
    Prop() {
    }

    /**
     * Returns a Clue ArrayList of all clues attached to this prop.
     *
     * @return The list of clues
     */
    ArrayList<Clue> takeClue() {
        return this.clue;
    }
}

