package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.Elements.StatusBar;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class JournalScreen extends AbstractScreen
{
    //TODO: This stuff is for testing purposes
    private StatusBar statusBar;

    public JournalScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }

    @Override
    public void show()
    {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta)
    {
        statusBar.render();
    }

    @Override
    public void resize(int width, int height)
    {
        statusBar.resize(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        statusBar.dispose();
    }
}
