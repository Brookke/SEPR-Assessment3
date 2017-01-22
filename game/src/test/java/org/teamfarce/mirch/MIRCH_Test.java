/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Tests functions in the MIRCH class
 * @author jacobwunwin
 *
 */
public class MIRCH_Test extends MIRCH{
	
	/*
	@Test
	public void test_getCurrentRoom(){
		//generate a room render item
		Room roomObj = new Room("room_1", null);
		Texture roomTexture = new Texture(Gdx.files.internal("assets/tests/500x500.png"));
		Sprite roomSprite = new Sprite(roomTexture);

		RenderItem room = new RenderItem(roomSprite, roomObj);
		
		assertEquals(500, roomSprite.getHeight(), 0);
		
		ArrayList<RenderItem> roomList = new ArrayList<RenderItem>();
		roomList.add(room); //generate an array list of rooms for testing
		
	}*/
	
	@Test
	public void test_inDoor(){
		Door door = new Door(0, 0, 100, 100);
		ArrayList<Door> doors = new ArrayList<Door>();
		doors.add(door);
		
		Sprite sprite = new Sprite();
		sprite.setPosition(0f, 0f);
		
		//test for when character is inside door
		boolean response = inDoor(doors, sprite);
		assertTrue("Failure - character not found to be in door", response);
		
		//test for when character is outside of door
		sprite.setPosition(200, 200);
		response = inDoor(doors, sprite);
		assertFalse("Failure - character found to be in door", response);
	}
	
	
}
