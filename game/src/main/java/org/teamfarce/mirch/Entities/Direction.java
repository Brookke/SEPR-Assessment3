package org.teamfarce.mirch.Entities;

/**
 * Created by brookehatton on 02/02/2017.
 */
public enum Direction
{
    NORTH(0,1), SOUTH(0,-1), EAST(1,0), WEST(-1,0);

    private int dx, dy;

    /**
     * @param dx x coordinate.
     * @param dy y coordinate.
     */
    Direction(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Getter for dx.
     *
     * @return returns the value of dx.
     */
    public int getDx()
    {
        return this.dx;
    }

    /**
     * Getter for dy.
     *
     * @return returns the value of dy.
     */
    public int getDy()
    {
        return this.dy;
    }


}
