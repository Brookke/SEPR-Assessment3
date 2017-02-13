/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import org.junit.Test;
import org.teamfarce.mirch.Entities.MapEntity;
import org.teamfarce.mirch.map.Room;

/**
 * Test the Map Entity
 * @author jacobwunwin
 *
 */
public class MapEntity_Test extends GameTest{
	String demoFileName = "clues/Axe.png";

	@Test
	public void test_setRoomGetRoom(){
		MapEntity mapEntity = new MapEntity(null, null, demoFileName);

		Room theRoom = new Room(0, "testRoom0.tmx", "Test Room 0");

		mapEntity.setRoom(theRoom);

		assertEquals(theRoom, mapEntity.getRoom());
	}

	@Test
	public void test_getName(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(input, null, demoFileName);
		assertEquals(input, mapEntity.getName());
		
	}

	@Test
	public void test_genDescription(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(null, input, demoFileName);
		assertEquals(input, mapEntity.getDescription());
	}

	@Test
	public void test_getFileName(){
		MapEntity mapEntity = new MapEntity(null, null, demoFileName);
		assertEquals(demoFileName, mapEntity.getFilename());
	}
	

}
