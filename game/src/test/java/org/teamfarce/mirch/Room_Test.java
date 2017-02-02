/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

/**
 * Tests functions in the room class
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
		assertEquals(room.getX(), position.x, 0);
		assertEquals(room.getY(), position.y, 0);
	}
}
