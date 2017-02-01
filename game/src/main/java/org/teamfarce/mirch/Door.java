package org.teamfarce.mirch;


import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * The Door Class stores the location of a single door on the map. A door is a rectangle which is drawn on the map
 * to allow unhindered transfer between rooms.
 *
 * @author jacobwunwin
 */
public class Door extends Sprite
{
    public float startX;
    public float startY;
    public float endX;
    public float endY;
    Face face;

    /**
     * Initialise the door by setting the location of its bottom left corner and top right corner.
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    Door(float startX, float startY, float endX, float endY, Face face)
    {
        super(Assets.doorwayTexture);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.face = face;
    }

    enum Face
    {
        horizontal, vertical;
    }

}
