package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.MIRCH;

/**
 * Created by joeshuff on 09/02/2017.
 */
public class NarratorScreen extends AbstractScreen {

    private MIRCH game;
    private GameSnapshot gameSnapshot;

    public Stage narratorStage;
    private Skin uiSkin;

    public NarratorScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;
    }

    private void initStage()
    {
        narratorStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image background = new Image(new TextureRegion(Assets.loadTexture("rchimage.jpg")));
        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());

        Image narrator = new Image(new TextureRegion(Assets.loadTexture("duck.png")));
        narrator.setHeight(Gdx.graphics.getHeight() / 4);
        narrator.setWidth(Gdx.graphics.getWidth() / 5);

        narratorStage.addActor(background);
    }

    @Override
    public void show() {
        initStage();
    }

    @Override
    public void render(float delta) {
        narratorStage.act();
        narratorStage.draw();
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
}
