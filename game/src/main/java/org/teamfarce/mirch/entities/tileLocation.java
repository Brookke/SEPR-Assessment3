package org.teamfarce.mirch.entities;

import org.teamfarce.mirch.Vector2Int;

/**
 * A interface for all objects that require to be positioned on the map in terms of tiles
 */
public interface tileLocation {

    /**
     * Gets the x location of the object in terms of tiles
     *
     * @return x tiles
     */
    int getTileX();

    /**
     * Gets the y location of the object in terms of tiles
     *
     * @return y tile
     */
    int getTileY();

    /**
     * Gets the tile coordinates in terms of a Vector int
     *
     * @return
     */
    Vector2Int getTileCoordinates();

    /**
     * Sets the tile location of the object
     *
     * @param vector the vector location to set it to
     */
    void setTileCoordinates(Vector2Int vector);

    /**
     * Sets the tile location of the object
     *
     * @param x the x tile
     * @param y the y tile
     */
    void setTileCoordinates(int x, int y);

}
