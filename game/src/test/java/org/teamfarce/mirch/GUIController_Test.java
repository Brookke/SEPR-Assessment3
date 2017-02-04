package org.teamfarce.mirch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.teamfarce.mirch.Screens.AbstractScreen;
import org.teamfarce.mirch.Screens.JournalScreen;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Created by Jason on 04/02/2017.
 */
public class GUIController_Test extends GameTest {

    MIRCH game;

    //Init empty screen for testing
    AbstractScreen screen1 = new AbstractScreen(game) {
        @Override
        public void show() {}
        @Override
        public void render(float delta) {}
        @Override
        public void resize(int width, int height) {}
        @Override
        public void pause() {}
        @Override
        public void resume() {}
        @Override
        public void hide() {}
        @Override
        public void dispose() {}
    };

    AbstractScreen screen2 = new AbstractScreen(game) {
        @Override
        public void show() {}
        @Override
        public void render(float delta) {}
        @Override
        public void resize(int width, int height) {}
        @Override
        public void pause() {}
        @Override
        public void resume() {}
        @Override
        public void hide() {}
        @Override
        public void dispose() {}
    };

    @Before
    public void init_tests()
    {
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, 0, 0);
    }

    @Test
    public void test_constructor(){
        game.gameSnapshot.setState(GameState.map);

        GUIController guiController = new GUIController(game);

        //Check the currentState of the guiController is null until .update() is called
        assertSame(guiController.currentState, null);
    }

    @Test
    public void test_map_screen(){
        game.gameSnapshot.setState(GameState.map);

        GUIController guiController = new GUIController(game);
        guiController.journalScreen = screen1;
        guiController.mapScreen = screen2;
        guiController.update();

        assertSame(guiController.currentState, GameState.map);
        assertSame(game.getScreen(), guiController.mapScreen);
        assertNotSame(game.getScreen(), guiController.journalScreen);
    }

    @Test
    public void test_journal_screen(){
        game.gameSnapshot.setState(GameState.journalHome);

        GUIController guiController = new GUIController(game);
        guiController.journalScreen = screen1;
        guiController.mapScreen = screen2;
        guiController.update();

        assertSame(guiController.currentState, GameState.journalHome);
        assertSame(game.getScreen(), guiController.journalScreen);
        assertNotSame(game.getScreen(), guiController.mapScreen);
    }
}
