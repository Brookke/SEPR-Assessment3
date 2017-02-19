/**
 *
 */
package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.entities.Clue;

import static org.junit.Assert.*;

/**
 * Tests the clues class
 *
 * @author jacobwunwin
 */
public class Clue_Test
{

    Clue clue;

    @Before
    public void before()
    {
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, false);
    }

    /**
     * Test the initialiser function
     */
    @Test
    public void getInfo()
    {
		assertEquals("Test Clue", clue.getName());
		assertEquals("Text Description", clue.getDescription());
		assertEquals(0, clue.getResourceX());
		assertEquals(0, clue.getResourceY());
    }

    @Test
    public void isMotive()
    {
        assertFalse(clue.isMotiveClue());
        clue.setMotiveClue();
        assertTrue(clue.isMotiveClue());
    }

    @Test
    public void isMeans()
    {
        assertFalse(clue.isMeansClue());
        clue = new Clue("Test Clue", "Test Description", "clueSheet.png", 0, 0, true);
        assertTrue(clue.isMeansClue());
    }
}
