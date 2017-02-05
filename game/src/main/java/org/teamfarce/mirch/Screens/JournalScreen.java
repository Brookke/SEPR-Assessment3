package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.Elements.StatusBar;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class JournalScreen extends AbstractScreen
{
    private MIRCH game;

    public Stage journalStage;
    private StatusBar statusBar;

    public JournalScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;

        initStage();

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }

    private void initStage() {
        journalStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table table = new Table();
        table.setBounds(80, 20, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 20);

        Texture journalBackground = new Texture(Gdx.files.internal("Open_journal.png"));
        TextureRegion tr = new TextureRegion(journalBackground);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        table.setBackground(trd);

        journalStage.addActor(table);
    }

    @Override
    public void show()
    {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(journalStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta)
    {
        journalStage.act();
        journalStage.draw();
        statusBar.render();
    }

    @Override
    public void resize(int width, int height)
    {
        journalStage.getViewport().update(width, height, true);
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
        journalStage.dispose();
        statusBar.dispose();
    }
}
