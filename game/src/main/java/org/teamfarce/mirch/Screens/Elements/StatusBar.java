package org.teamfarce.mirch.Screens.Elements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Top status bar in game
 */
public class StatusBar
{
    /**
     * Height of bar
     */
    public static final int HEIGHT = 40; //Used to set height of status bar

    /**
     * Item count
     */
    private static final int ITEM_COUNT = 4; //Used to set width of controls on bar

    /**
     * Width of bar
     */
    private static final int WIDTH = Gdx.graphics.getWidth() / ITEM_COUNT;

    /**
     * Background colour
     */
    private static final Color BACKGROUND_COLOR = new Color(0,0,0, 128);

    /**
     * The stage to render the elements to
     */
    public Stage stage;

    /**
     * Styles
     */
    private Skin uiSkin;

    /**
     * The initializer for the StatusBar
     * Sets up UI controls and adds them to the stage ready for rendering
     */
    public StatusBar()
    {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //TODO: Sort out skins to make this consistent with rest of app
        uiSkin = new Skin(Gdx.files.internal("skins/skin_pretty/skin.json"));

        Table statusBar = new Table();
        statusBar.setSize(Gdx.graphics.getWidth(), HEIGHT);
        statusBar.setPosition(0, Gdx.graphics.getHeight() - HEIGHT);
        statusBar.row().height(HEIGHT);
        statusBar.defaults().width(WIDTH);

        Label scoreLabel = new Label("Score: 0", uiSkin);
        scoreLabel.setAlignment(Align.center, Align.center);
        statusBar.add(scoreLabel).uniform();

        TextButton mapButton = new TextButton("Map", uiSkin);
        statusBar.add(mapButton).uniform();

        TextButton inventoryButton = new TextButton("Journal", uiSkin);
        statusBar.add(inventoryButton).uniform();

        stage.addActor(statusBar);
    }

    /**
     * Render function to display the status bar
     * Usage: call within the render() method in a screen
     */
    public void render()
    {
        stage.act();
        stage.draw();
    }

    /**
     * Used for window resize event
     *
     * @param width  - updated width
     * @param height - updated height
     */
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    /**
     * This disposes all the elements
     */
    public void dispose()
    {
        stage.dispose();
    }
}
