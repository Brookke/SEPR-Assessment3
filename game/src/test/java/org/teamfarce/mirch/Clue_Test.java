/**
 *
 */
package org.teamfarce.mirch;

import org.junit.Test;
import org.teamfarce.mirch.entities.Clue;

import static org.junit.Assert.*;

/**
 * Tests the clues class
 *
 * @author jacobwunwin
 */
public class Clue_Test extends GameTest {

    Clue clue;

    /**
     * Test the initialiser function
     */
    @Test
    public void getInfo() {
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, false);

        assertEquals("Test Clue", clue.getName());
        assertEquals("Test Description", clue.getDescription());
        assertEquals(0, clue.getResourceX());
        assertEquals(0, clue.getResourceY());
    }

    @Test
    public void isMotive() {
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, false);
        assertFalse(clue.isMotiveClue());
        clue.setMotiveClue();
        assertTrue(clue.isMotiveClue());
    }

    @Test
    public void isMeans() {
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, false);
        assertFalse(clue.isMeansClue());
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, true);
        assertTrue(clue.isMeansClue());
    }
}
