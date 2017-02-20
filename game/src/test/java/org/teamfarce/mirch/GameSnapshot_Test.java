package org.teamfarce.mirch;

import org.junit.Test;

import static org.junit.Assert.assertSame;


/**
 * @author jacobwunwin
 */
public class GameSnapshot_Test {

    @Test
    public void getTime() {
        GameSnapshot gameSnapshot = new GameSnapshot(null, null, null, null, null);
        assertSame(gameSnapshot.getTime(), gameSnapshot.time);
    }

    @Test
    public void getRooms() {
        GameSnapshot gameSnapshot = new GameSnapshot(null, null, null, null, null);
        assertSame(gameSnapshot.getRooms(), gameSnapshot.rooms);
    }

    @Test
    public void getClues() {
        GameSnapshot gameSnapshot = new GameSnapshot(null, null, null, null, null);
        assertSame(gameSnapshot.getClues(), gameSnapshot.clues);
    }
}
