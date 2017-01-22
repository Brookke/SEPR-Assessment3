/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

/**
 * @author jacobwunwin
 *
 */
public class Room_Test {
	
	@Test
	public void test_init(){
		String file = "test";
		Vector2 position = new Vector2(1, 1);
		Room room = new Room(file, position);
		
		assertEquals(room.filename, file);
		assertEquals(room.position, position);
	}
}
