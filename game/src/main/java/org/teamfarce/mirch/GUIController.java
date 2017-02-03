package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.teamfarce.mirch.Screens.AbstractScreen;
import org.teamfarce.mirch.Screens.MapScreen;
import org.teamfarce.mirch.Screens.StartScreen;

/**
 * Generates and controls all GUI screens
 * from the GUI controller.
 *
 * @author jasonmash
 */
public class GUIController {
    public MIRCH game;
    public Skin uiSkin;
    public AbstractScreen currentScreen;

    public AbstractScreen mapScreen;
    public AbstractScreen startScreen;

    GUIController(MIRCH game) {
        this.game = game;

        uiSkin = new Skin(Gdx.files.internal("skins/skin_pretty/skin.json")); //load ui skin from assets

        mapScreen = new MapScreen(game, uiSkin);
        startScreen = new StartScreen(game, uiSkin);

        showMapScreen(); //TODO: change this to startScreen when implemented
    }

    public void showMapScreen() {
        currentScreen = mapScreen;
        updateScreen();
    }

    public void showStartScreen() {
        currentScreen = startScreen;
        updateScreen();
    }

    public void updateScreen() {
        this.game.setScreen(currentScreen);
    }
}
