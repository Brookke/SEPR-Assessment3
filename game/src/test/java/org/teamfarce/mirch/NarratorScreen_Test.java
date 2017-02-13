package org.teamfarce.mirch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.Screens.NarratorScreen;

import static org.junit.Assert.*;

/**
 * Created by joeshuff on 13/02/2017.
 */
public class NarratorScreen_Test extends GameTest {

    private NarratorScreen screen;

    private MIRCH game;

    @Before
    public void init_tests()
    {
        Skin skin = new Skin();
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, 0, 0);
        screen = new NarratorScreen(game, skin);
    }

    @Test
    public void test_setSpeech()
    {
        screen.setSpeech("Test Speech");

        assertEquals("Test Speech", screen.getSpeech());
    }

    @Test
    public void test_UpdateSpeech()
    {
        screen.setSpeech("Test Speech");
        screen.updateSpeech();

        assertEquals(screen.getCurrentSpeech(), "T");
    }

}
