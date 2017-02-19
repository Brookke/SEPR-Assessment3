package org.teamfarce.mirch.entities;


import org.teamfarce.mirch.Assets;

/**
 * This class defines the clues that the player needs to find in order to solve the murder.
 */
public class Clue extends MapEntity
{

    private boolean motiveClue = false;

    /**
     * Creates a clue
     *
     * @param name        the name of the clue i.e. what it is
     * @param description describes what the clue is
     * @param filename    the texture region of the clue
     */
    public Clue(String name, String description, String filename)
    {
        super(name, description, Assets.loadTexture("clues/" + filename));

    }

    /**
     * This method checks equality of this Clue object and another object.
     *
     * @param obj - The clue object.
     * @return - Returns True if it is of the type Clue and the names are exactly the same
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Clue) {
            Clue c = (Clue) obj;
            return c.getName().equals(this.getName()) && c.getDescription().equals(this.description);
        }

        return false;
    }

    public boolean isMotiveClue() {
        return motiveClue;
    }

    public void setMotiveClue() {
        this.motiveClue = true;
    }
}
