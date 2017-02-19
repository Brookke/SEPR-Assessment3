package org.teamfarce.mirch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.screens.FindClueScreen;
import org.teamfarce.mirch.screens.NarratorScreen;

/**
 * Created by joeshuff on 19/02/2017.
 */
public class FindClueScreen_Test extends GameTest {

    private FindClueScreen screen;

    private MIRCH game;

    @Test
    public void constructor()
    {
        Skin skin = new Skin();
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, null, null, 100, 100);
        game.gameSnapshot.victim = new Suspect(game, "Test", "test", "Colin.png", new Vector2Int(0, 0), null);
        screen = new FindClueScreen(game, skin);
    }
}
