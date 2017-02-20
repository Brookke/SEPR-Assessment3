package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.entities.Direction;
import org.teamfarce.mirch.map.Room;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by joeshuff on 26/11/2016.
 */
public class Room_Test extends GameTest {

    Room room0, room1;


    @Before
    public void before() {

        room0 = new Room(0, "testRoom0.tmx", "Test Room 0");
        room1 = new Room(1, "testRoom1.tmx", "Test Room 1");

        room0.addTransition(new Room.Transition().setFrom(0, 4).setTo(room1, 0, 0, Direction.EAST));
    }

    @Test
    public void getTransition() {
        assertEquals(room1, room0.getTransitionData(0, 4).getNewRoom());
        assertEquals("Test Room 1", room0.getTransitionData(0, 4).getNewRoom().getName());
        assertEquals(new Vector2Int(0, 0), room0.getTransitionData(0, 4).newTileCoordinates);
        assertEquals(null, room0.getTransitionData(0, 0));
        assertEquals(Direction.EAST, room0.getTransitionData(0, 4).newDirection);
    }

    @Test
    public void addTransition() {
        room1.addTransition(new Room.Transition().setFrom(0, 0).setTo(room0, 0, 4, Direction.NORTH));
        assertEquals(room0, room1.getTransitionData(0, 0).getNewRoom());
    }

    @Test
    public void walkable() {
        assertEquals(true, room0.isWalkableTile(0, 0));
        assertEquals(false, room0.isWalkableTile(0, 1));
        assertEquals(false, room0.isWalkableTile(-10, -5));
    }

    @Test
    public void trigger() {
        assertEquals(true, room0.isTriggerTile(0, 4));
        assertEquals(false, room0.isTriggerTile(3, 3));
    }

    @Test
    public void matRotation() {
        assertEquals("NORTH", room0.getMatRotation(0, 4));
        assertEquals("SOUTH", room1.getMatRotation(0, 0));
    }

}
