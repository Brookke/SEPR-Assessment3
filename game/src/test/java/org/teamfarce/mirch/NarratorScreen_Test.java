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
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, 0, 0);
    }

    @Test
    public void test_setSpeech()
    {
        Skin skin = new Skin();

        screen = new NarratorScreen(game, skin);

        screen.setSpeech("Test Speech");

        assertEquals("Test Speech", screen.getSpeech());
    }

}
