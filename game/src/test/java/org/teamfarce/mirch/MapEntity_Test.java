/**
 *
 */
package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Test;
import org.teamfarce.mirch.Entities.MapEntity;
import org.teamfarce.mirch.map.Room;

import static org.junit.Assert.assertEquals;

/**
 * Test the Map Entity
 *
 * @author jacobwunwin
 */
public class MapEntity_Test extends GameTest
{
    String demoFileName = "clues/Axe.png";
    Texture demoTexture = new Texture(Gdx.files.internal(demoFileName));

    @Test
    public void test_setRoomGetRoom()
    {
        MapEntity mapEntity = new MapEntity(null, null, demoTexture);

        Room theRoom = new Room(0, "testRoom0.tmx", "Test Room 0");

        mapEntity.setRoom(theRoom);

        assertEquals(theRoom, mapEntity.getRoom());
    }

    @Test
    public void test_getName()
    {
        String input = "Test";
        MapEntity mapEntity = new MapEntity(input, null, demoTexture);
        assertEquals(input, mapEntity.getName());

    }

    @Test
    public void test_genDescription()
    {
        String input = "Test 1 2 3 4";
        MapEntity mapEntity = new MapEntity(null, input, demoTexture);
        assertEquals(input, mapEntity.getDescription());
    }

    @Test
    public void test_getTexture()
    {
        MapEntity mapEntity = new MapEntity(null, null, demoTexture);
        assertEquals(demoTexture, mapEntity.getTexture());
    }


}
