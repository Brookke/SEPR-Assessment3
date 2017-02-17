package org.teamfarce.mirch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.teamfarce.mirch.Entities.Suspect;

/**
 * @author jacobwunwin
 *
 */
public class Suspect_Test {

    private Suspect suspect;

    @Before
    public void init()
    {
        String name = "ji";
        Vector2Int position = new Vector2Int(0, 1);

        MIRCH game = new MIRCH();
        game.gameSnapshot =  new GameSnapshot(game, null, null, null, null, 0, 0);

        suspect = new Suspect(game, name, "Description", "Detective_Sprite.png", position, "");
    }

	@Test
	public void test_init(){
		String name = "ji";
		Vector2Int position = new Vector2Int(0, 1);

		assertEquals(name, suspect.getName());
		assertEquals(position.x, suspect.getTileX(), 0);
        assertEquals(position.y, suspect.getTileY(), 0);
	}

	@Test
	public void test_hasBeenAccused()
    {
		suspect.isMurderer = false;
		assertFalse(suspect.accuse(false));

		assertTrue(suspect.hasBeenAccused());
	}

	@Test
	public void test_setPosition()
    {
		Vector2Int position = new Vector2Int(0, 1);

		assertEquals(position.x, suspect.getTileX(), 0);
		assertEquals(position.y, suspect.getTileY(), 0);
	}

	@Test
    public void test_setKiller()
    {
        assertFalse(suspect.isKiller());
        suspect.setKiller();
        assertTrue(suspect.isKiller());
    }

    @Test
    public void test_setVictim()
    {
        assertFalse(suspect.isVictim());
        suspect.setVictim();
        assertTrue(suspect.isVictim());
    }

}
