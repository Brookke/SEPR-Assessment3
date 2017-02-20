package org.teamfarce.mirch.entities;

/**
 * Created by brookehatton on 02/02/2017.
 */
public enum Direction {
    NORTH(0, 1), SOUTH(0, -1), EAST(1, 0), WEST(-1, 0);

    private int dx, dy;

    /**
     * @param dx x coordinate.
     * @param dy y coordinate.
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Getter for dx.
     *
     * @return returns the value of dx.
     */
    public int getDx() {
        return this.dx;
    }

    /**
     * Getter for dy.
     *
     * @return returns the value of dy.
     */
    public int getDy() {
        return this.dy;
    }

    /**
     * This method takes the current direction and gets the opposite direction
     *
     * @return (Direction) the opposite direction to this
     */
    public Direction getOpposite() {
        if (this == Direction.NORTH) return Direction.SOUTH;
        if (this == Direction.EAST) return Direction.WEST;
        if (this == Direction.SOUTH) return Direction.NORTH;
        if (this == Direction.WEST) return Direction.EAST;

        return null;
    }

}
