package org.teamfarce.mirch.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.Settings;

/**
 * This class defines the clues that the player needs to find in order to solve the murder.
 */
public class Clue extends MapEntity {
    /**
     * This stores whether this clue is a motive clue
     */
    private boolean motiveClue = false;

    /**
     * This stores whether this clue is the murder weapon
     */
    private boolean meansClue = false;

    /**
     * This stores the tile location of the clue asset on the clue sprite sheet
     */
    private int resourceX = 0;
    private int resourceY = 0;

    /**
     * Creates a clue
     *
     * @param name        the name of the clue i.e. what it is
     * @param description describes what the clue is
     * @param filename    the texture region of the clue
     */
    public Clue(String name, String description, String filename, int resourceX, int resourceY, boolean meansClue) {
        super(name, description, new TextureRegion(Assets.loadTexture("clues/" + filename), resourceX * 128, resourceY * 128, 128, 128));
        this.resourceX = resourceX;
        this.resourceY = resourceY;
        setSize(Settings.TILE_SIZE, Settings.TILE_SIZE);

        if (name.contains("Motive")) motiveClue = true;

        this.meansClue = meansClue;
    }

    /**
     * This method checks equality of this Clue object and another object.
     *
     * @param obj - The clue object.
     * @return - Returns True if it is of the type Clue and the names are exactly the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Clue) {
            Clue c = (Clue) obj;
            return c.getName().equals(this.getName()) && c.getDescription().equals(this.description);
        }

        return false;
    }

    /**
     * Returns whether the clue is a motive clue or not
     *
     * @return `motiveClue`
     */
    public boolean isMotiveClue() {
        return motiveClue;
    }

    /**
     * Returns whether the clue is a means clue or not
     *
     * @return `meansClue`
     */
    public boolean isMeansClue() {
        return meansClue;
    }

    /**
     * Returns the x tile location of the resource
     *
     * @return `resourceX`
     */
    public int getResourceX() {
        return resourceX;
    }

    /**
     * Returns the y tile location of the resource
     *
     * @return `resourceY`
     */
    public int getResourceY() {
        return resourceY;
    }

    /**
     * This method sets the clue to be a motive clue
     */
    public void setMotiveClue() {
        this.motiveClue = true;
    }
}
