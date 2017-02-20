package org.teamfarce.mirch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.screens.NarratorScreen;

import static org.junit.Assert.assertEquals;

/**
 * Created by joeshuff on 13/02/2017.
 */
public class NarratorScreen_Test extends GameTest {

    private NarratorScreen screen;

    private MIRCH game;

    @Before
    public void init_tests() {
        Skin skin = new Skin();
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, null, null);
        game.gameSnapshot.victim = new Suspect(game, "Test", "test", "Colin.png", new Vector2Int(0, 0), null);
        screen = new NarratorScreen(game, skin);
    }

    @Test
    public void setSpeech() {
        screen.setSpeech("Test Speech");

        assertEquals("Test Speech", screen.getSpeech());
    }

    @Test
    public void updateSpeech() {
        screen.setSpeech("Test Speech");
        screen.updateSpeech();


        assertEquals(screen.getCurrentSpeech(), "T");
    }

}
