package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.screens.AbstractScreen;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Created by Jason on 04/02/2017.
 */
public class GUIController_Test extends GameTest {

    MIRCH game;

    //Init empty screens for testing
    //This is done twice as we need two different objects to compare
    //No point loading actual game screens as they have extra dependencies
    AbstractScreen screen1 = new AbstractScreen(game) {
        @Override
        public void show() {
        }

        @Override
        public void render(float delta) {
        }

        @Override
        public void resize(int width, int height) {
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void hide() {
        }

        @Override
        public void dispose() {
        }
    };
    AbstractScreen screen2 = new AbstractScreen(game) {
        @Override
        public void show() {
        }

        @Override
        public void render(float delta) {
        }

        @Override
        public void resize(int width, int height) {
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void hide() {
        }

        @Override
        public void dispose() {
        }
    };

    @Before
    public void init_tests() {
        game = new MIRCH();
        game.gameSnapshot = new GameSnapshot(null, null, null, null, null);
    }

    @Test
    public void constructor() {

        //Init GUIController
        GUIController guiController = new GUIController(game);

        //Check the currentState of the guiController is null as .update() hasn't been called
        assertSame(guiController.currentState, null);
        assertSame(game.getScreen(), null);
    }

    @Test
    public void screenCanBeSet() {

        //Init GUIController
        GUIController guiController = new GUIController(game);
        guiController.journalScreen = screen1;
        guiController.mapScreen = screen2;

        assertSame(guiController.currentState, null);
        assertSame(game.getScreen(), null);

        //Set MapScreen as active
        game.gameSnapshot.setState(GameState.map);
        guiController.update();

        //Check MapScreen is active
        assertSame(guiController.currentState, GameState.map);
        assertSame(game.getScreen(), guiController.mapScreen);
        assertNotSame(game.getScreen(), guiController.journalScreen);
    }

    @Test
    public void screenCanBeChanged() {

        //Init GUIController with active MapScreen
        game.gameSnapshot.setState(GameState.map);
        GUIController guiController = new GUIController(game);
        guiController.journalScreen = screen1;
        guiController.mapScreen = screen2;
        guiController.update();

        //Check MapScreen is active
        assertSame(guiController.currentState, GameState.map);
        assertSame(game.getScreen(), guiController.mapScreen);
        assertNotSame(game.getScreen(), guiController.journalScreen);

        //Switch to JournalScreen
        game.gameSnapshot.setState(GameState.journalClues);
        guiController.update();

        //Check JournalScreen is active
        assertSame(guiController.currentState, GameState.journalClues);
        assertSame(game.getScreen(), guiController.journalScreen);
        assertNotSame(game.getScreen(), guiController.mapScreen);
    }
}
