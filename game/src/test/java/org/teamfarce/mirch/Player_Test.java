package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.Entities.Player;
import org.teamfarce.mirch.GameTest;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.map.Room;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by joeshuff on 15/02/2017.
 */
public class Player_Test extends GameTest {

    Player p;

    @Before
    public void start()
    {
        p = new Player("Name", "Desc", "Detective_Sprite.png");
    }

    @Test
    public void test_aStar()
    {
        Vector2Int start = new Vector2Int(0, 0);

        Vector2Int dest = new Vector2Int(4, 4);

        p.setTileCoordinates(start);
        p.setRoom(new Room(0, "testMap.tmx", "name"));


        Vector2Int c1 = new Vector2Int(0,0);
        Vector2Int c2 = new Vector2Int(1,0);
        Vector2Int c3 = new Vector2Int(2,0);
        Vector2Int c4 = new Vector2Int(3,0);
        Vector2Int c5 = new Vector2Int(4,0);
        Vector2Int c6 = new Vector2Int(4,1);
        Vector2Int c7 = new Vector2Int(4,2);
        Vector2Int c8 = new Vector2Int(4,3);
        Vector2Int c9 = new Vector2Int(4,4);
        List<Vector2Int> list = Arrays.asList(c1,c2,c3,c4,c5,c6,c7,c8,c9);
        assertEquals(list, p.aStarPath(dest));
    }

}
