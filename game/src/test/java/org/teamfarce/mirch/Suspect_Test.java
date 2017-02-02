package org.teamfarce.mirch;
/**
 * 
 */

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import org.teamfarce.mirch.Entities.Suspect;

/**
 * @author jacobwunwin
 *
 */
public class Suspect_Test {

	@Test
	public void test_init(){
		String name = "ji";
		Vector2 position = new Vector2(0, 1);
		Suspect suspect = new Suspect(name, position);
		
		assertEquals(name, suspect.getFilename());
		assertEquals(position.x, suspect.getX(), 0);
        assertEquals(position.y, suspect.getY(), 0);
	}
	
	@Test
	public void test_accuse(){
		String name = "ji";
		Vector2 position = new Vector2(0, 1);
		Suspect suspect = new Suspect(name, position);
		
		//test if not murderer and no evidence 
		suspect.isMurderer = false;
		assertFalse(suspect.accuse(false));
		
		//test if not murderer and has evidence
		suspect.isMurderer = false;
		assertFalse(suspect.accuse(true));
		
		//test if is murderer and no evidence
		suspect.isMurderer = true;
		assertFalse(suspect.accuse(false));
		
		//test if is murderer and has evidence
		suspect.isMurderer = true;
		assertTrue(suspect.accuse(true));
	}
	
	@Test
	public void test_hasBeenAccused(){
		String name = "ji";
		Vector2 position = new Vector2(0, 1);
		Suspect suspect = new Suspect(name, position);
		
		//accuse murderer
		suspect.isMurderer = false;
		assertFalse(suspect.accuse(false));
		
		
		assertTrue(suspect.hasBeenAccused());
	}
	
	@Test
	public void test_setPosition(){
		String name = "ji";
		Vector2 position = new Vector2(0, 1);
		Suspect suspect = new Suspect(name, position);

		assertEquals(position.x, suspect.getX(), 0);
		assertEquals(position.y, suspect.getY(), 0);
	}
	
}
