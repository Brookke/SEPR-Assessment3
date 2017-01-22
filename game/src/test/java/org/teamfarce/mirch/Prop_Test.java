/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

/**
 * @author jacobwunwin
 *
 */
public class Prop_Test {
	
	@Test
	public void test_init(){
		String filename = "file";
		Room room = new Room(filename, null);
		Vector2 roomPos = new Vector2(1, 2);
		Prop prop = new Prop(filename, room, roomPos);
		
		assertEquals(filename, prop.filename);
		assertEquals(room, prop.currentRoom);
		assertEquals(roomPos, prop.roomPosition);
	}
	
	@Test
	public void test_getClues(){
		Clue clue = new Clue (0, 0, null);
		ArrayList<Clue> clues = new ArrayList<Clue>();
		clues.add(clue);
		
		String filename = "file";
		Room room = new Room(filename, null);
		Vector2 roomPos = new Vector2(1, 2);
		Prop prop = new Prop(filename, room, roomPos);
		prop.clue = clues;
		
		assertEquals(clues, prop.clue);
	}

}
