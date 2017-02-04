package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.teamfarce.mirch.Screens.AbstractScreen;
import org.teamfarce.mirch.Screens.MapScreen;
import org.teamfarce.mirch.Screens.JournalScreen;

/**
 * Generates and controls all GUI screens
 * from the GUI controller.
 *
 * @author jasonmash
 */
public class GUIController {
    public MIRCH game;
    public Skin uiSkin;
    public GameState currentState;

    public AbstractScreen mapScreen;
    public AbstractScreen journalScreen;

    GUIController(MIRCH game) {
        this.game = game;

        uiSkin = new Skin(Gdx.files.internal("skins/skin_pretty/skin.json")); //load ui skin from assets

        mapScreen = new MapScreen(game, uiSkin);
        journalScreen = new JournalScreen(game, uiSkin);
    }


    public void update() {

        //Clear background
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Get latest game state
        GameState newState = game.gameSnapshot.getState();

        //Check if screen needs changing
        if (currentState != newState) {

            //Set new screen depending on current game state
            switch (newState) { //TODO: Handle other states
                case map:
                    this.game.setScreen(mapScreen);
                    break;
                case journalHome:
                    this.game.setScreen(journalScreen);
                    break;
                default:
                    this.game.setScreen(mapScreen);
                    break;
            }

            //Update current screen to reflect changed screen
            currentState = newState;
        }

    }
}
