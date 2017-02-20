package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.entities.Player;
import org.teamfarce.mirch.map.Room;

import static org.junit.Assert.assertEquals;

/**
 * Created by joeshuff on 15/02/2017.
 */
public class Player_Test extends GameTest {

    Player p = null;

    @Before
    public void start() throws Dialogue.InvalidDialogueException {
        Dialogue dialogue = new Dialogue("template.JSON", false);
        p = new Player(null, "Test Name", "Desc", "Detective_sprite.png", dialogue);
    }

    @Test
    public void getPlayername() {
        assertEquals("Fail - Not returning correct playername", p.getName(), "Test Name");
    }

    @Test
    public void aStar() {
        Vector2Int start = new Vector2Int(0, 0);

        Vector2Int dest = new Vector2Int(4, 4);

        p.setTileCoordinates(start);
        p.setRoom(new Room(0, "testMap.tmx", "name"));

        String desiredSolution = "[(0,0), (1,0), (2,0), (3,0), (4,0), (4,1), (4,2), (4,3), (4,4)]";
        assertEquals(desiredSolution, p.aStarPath(dest).toString());
    }

}
