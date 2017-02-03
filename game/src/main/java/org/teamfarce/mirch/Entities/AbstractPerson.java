package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Vector2Int;

import java.util.Comparator;

/**
 * Created by brookehatton on 01/02/2017.
 */
public abstract class AbstractPerson extends MapEntity
{


    Direction direction;
    /**
     * Initialise the entity.
     *
     * @param name        The name of the entity.
     * @param description The description of the entity.
     * @param filename    The filename of the image to display for the entity.
     */
    public AbstractPerson(String name, String description, String filename)
    {
        super(name, description, filename);
    }

    /**
     * This controls the movement of a person
     */
    public abstract void move(Direction dir);


    /**
     * This class is to sort a list of AbstractPerson in highest Y coordinate to lowest Y coordinate
     * <p>
     * It is used to render NPCs and the Player in the correct order to avoid it appearing as though someone
     * is standing on top of someone else
     */
    public static class PersonPositionComparator implements Comparator<AbstractPerson>
    {
        /**
         * This method compares the 2 objects.
         *
         * @param o1 - The first object to compare
         * @param o2 - The second object to compare
         * @return (int) if <0 o1 is considered to be first in the list
         */
        @Override
        public int compare(AbstractPerson o1, AbstractPerson o2)
        {
            return o2.getTileCoordinates().y - o1.getTileCoordinates().y;
        }
    }

}
