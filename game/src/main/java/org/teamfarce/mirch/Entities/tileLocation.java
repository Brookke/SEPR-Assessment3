package org.teamfarce.mirch.Entities;

import org.teamfarce.mirch.Vector2Int;

/**
 * Created by brookehatton on 04/02/2017.
 */
public interface tileLocation
{
    int getTileX();
    int getTileY();
    void setTileCoordinates(int x, int y);
    void setTileCoordinates(Vector2Int vector);

}
