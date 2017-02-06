package org.teamfarce.mirch.Entities;


import com.badlogic.gdx.graphics.g2d.Sprite;
import org.teamfarce.mirch.Vector2Int;

/**
 * This class defines the clues that the player needs to find in order to solve the murder.
 */
public class Clue extends Sprite implements tileLocation
{

    private final String name;
    private final String description;
    private Vector2Int tileLocation = new Vector2Int(0,0);

    /**
     * Creates a clue
     *
     * @param name        the name of the clue i.e. what it is
     * @param description describes what the clue is
     * @param filename     the texture region of the clue
     */
    public Clue(String name, String description, int impliesMotiveRating, int impliesMeansRating, String filename)
    {
        this.name = name;
        this.description = description;


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
            return c.getName().equals(this.getName());
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Gets the x tileLocation of the object in terms of tiles
     *
     * @return x tiles
     */
    @Override
    public int getTileX() {
        return this.tileLocation.x;
    }

    /**
     * Gets the y tileLocation of the object in terms of tiles
     *
     * @return y tile
     */
    @Override
    public int getTileY() {
        return this.tileLocation.y;
    }

    /**
     * Gets the tile coordinates in terms of a Vector int
     *
     * @return
     */
    @Override
    public Vector2Int getTileCoordinates() {
        return this.tileLocation;
    }

    /**
     * Sets the tile tileLocation of the object
     *
     * @param x the x tile
     * @param y the y tile
     */
    @Override
    public void setTileCoordinates(int x, int y) {

        this.tileLocation.x = x;
        this.tileLocation.y = y;

    }

    /**
     * Sets the tile tileLocation of the object
     *
     * @param vector the tileLocation tileLocation to set it to
     */
    @Override
    public void setTileCoordinates(Vector2Int vector) {
        this.tileLocation = vector;
    }

}
