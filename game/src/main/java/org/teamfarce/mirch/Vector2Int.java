package org.teamfarce.mirch;


import org.teamfarce.mirch.entities.Direction;

/**
 * This class defines a 2D vector to represent coordinates within the game.
 */
public class Vector2Int {
    /**
     * x and y coordinates of the object
     */
    public int x = 0;// initialise x as 0
    public int y = 0;// initialise y as 0

    /**
     * Creates a new Vector2Int object.
     *
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    public Vector2Int(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x coordinate.
     *
     * @return returns value of x for this object.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for y coordinate.
     *
     * @return returns value of y for this object.
     */
    public int getY() {
        return this.y;
    }

    /**
     * This is a method override which allows the use of equality in the newly defined Vector2Int objects.
     *
     * @param obj - The Vector2Int object.
     * @return Returns true or false depending on whether the equality is true or false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector2Int other = (Vector2Int) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }

    /**
     * This method returns a String representation of the Vector2Int object
     *
     * @return String - Representing the values stored in this object
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * This method returns the hashCode for this object. It is used in hashmaps
     *
     * @return hashCode formatted as xCoordinate00ycoordinate
     */
    @Override
    public int hashCode() {
        return Integer.valueOf(x + "00" + y);
    }

    /**
     * This method returns the direction of the `other` position from the `self` position
     *
     * @param other - The Vector2Int to compare
     * @return Direction - The direction the other position is in
     */
    public Direction dirBetween(Vector2Int other) {
        if (x - other.getX() == 1) return Direction.EAST;
        if (x - other.getX() == -1) return Direction.WEST;
        if (y - other.getY() == 1) return Direction.NORTH;
        if (y - other.getY() == -1) return Direction.SOUTH;

        return Direction.NORTH;
    }

    public Vector2Int scl(int scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }
}
