/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Test;
import org.teamfarce.mirch.Entities.MapEntity;
import org.teamfarce.mirch.map.Room;

/**
 * Test the Map Entity
 * @author jacobwunwin
 *
 */
public class MapEntity_Test extends GameTest {
	
	@Test
	public void test_setRoomGetRoom(){
		MapEntity mapEntity = new MapEntity(null, null,  new Texture(Gdx.files.internal("clues/Axe.png")));

		Room theRoom = new Room(0, "testMap.tmx", "test");
		mapEntity.setRoom(theRoom);
		assertEquals(theRoom, mapEntity.getRoom());
	}

	@Test
	public void test_getName(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(input, null, new Texture(Gdx.files.internal("clues/Axe.png")));
		assertEquals(input, mapEntity.getName());
		
	}

	@Test
	public void test_genDescription(){
		String input = "Test 1 2 4";
		MapEntity mapEntity = new MapEntity(null, input, new Texture(Gdx.files.internal("clues/Axe.png")));
		assertEquals(input, mapEntity.getDescription());
	}


	@Test
	public void test_getFileName(){
        Texture input = new Texture(Gdx.files.internal("clues/Axe.png"));
		MapEntity mapEntity = new MapEntity(null, null, input);
		assertEquals(input, mapEntity.getTexture());
	}
	

}
