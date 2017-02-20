package org.teamfarce.mirch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.map.Map;
import org.teamfarce.mirch.map.Room;
import org.teamfarce.mirch.screens.NarratorScreen;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author jacobwunwin
 */
public class Suspect_Test {

    private Suspect suspect;

    @Before
    public void initTests() {
        String name = "ji";
        Vector2Int position = new Vector2Int(0, 1);

        MIRCH game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(game, new Map(game), new ArrayList<Room>(), new ArrayList<Suspect>(), null);
        game.gameSnapshot.victim = new Suspect(game, "Test", "test", "Colin.png", new Vector2Int(0, 0), null);
        game.guiController = new GUIController(game);
        game.guiController.narratorScreen = new NarratorScreen(game, new Skin());

        suspect = new Suspect(game, name, "Description", "Colin.png", position, null);
    }

    @Test
    public void init() {
        String name = "ji";
        Vector2Int position = new Vector2Int(0, 1);

        assertEquals(name, suspect.getName());
        assertEquals(position.x, suspect.getTileX(), 0);
        assertEquals(position.y, suspect.getTileY(), 0);
    }

    @Test
    public void hasBeenAccused() {
        suspect.setKiller();
        assertFalse(suspect.accuse(false));

        assertTrue(suspect.hasBeenAccused());
    }

    @Test
    public void setPosition() {
        Vector2Int position = new Vector2Int(0, 1);

        assertEquals(position.x, suspect.getTileX(), 0);
        assertEquals(position.y, suspect.getTileY(), 0);
    }

    @Test
    public void setKiller() {
        assertFalse(suspect.isKiller());
        suspect.setKiller();
        assertTrue(suspect.isKiller());
    }

    @Test
    public void setVictim() {
        assertFalse(suspect.isVictim());
        suspect.setVictim();
        assertTrue(suspect.isVictim());
    }

}
