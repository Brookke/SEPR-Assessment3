package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Created by Jason on 04/02/2017.
 */
public class GUIController_Test {

    MIRCH game;

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
        guiController.update();

        assertSame(guiController.currentState, GameState.map);
        assertSame(game.getScreen(), guiController.mapScreen);
    }

    @Test
    public void test_journal_screen(){
        game.gameSnapshot.setState(GameState.journalHome);

        GUIController guiController = new GUIController(game);
        guiController.update();

        assertSame(guiController.currentState, GameState.journalHome);
        assertSame(game.getScreen(), guiController.journalScreen);
    }
}
