package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Settings;

import java.util.Map;
import java.util.Set;

/**
 * Created by joeshuff on 18/02/2017.
 */
public class FindClueScreen extends AbstractScreen {

    private GameSnapshot snapshot;
    private Skin uiSkin;

    private Stage clueStage;

    private Clue displayingClue;
    private Image clueImage;

    private float timeSinceLast = 0f;
    private float timePerIncrease = 0.5f;

    public FindClueScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.snapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

    }

    private void initScreen()
    {
        clueStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image screenShot = MapScreen.recentFrame;
        screenShot.setHeight(Gdx.graphics.getHeight());
        screenShot.setWidth(Gdx.graphics.getWidth());

        displayingClue = game.player.getClueFound();

        clueImage = new Image(new TextureRegion(displayingClue.getTexture(), displayingClue.getResourceX() * 128, displayingClue.getResourceY() * 128, 128, 128));

        clueImage.setSize(Settings.TILE_SIZE, Settings.TILE_SIZE);
        clueImage.setPosition(displayingClue.getX(), displayingClue.getY());

        clueStage.addActor(screenShot);
        clueStage.addActor(clueImage);
    }

    @Override
    public void show() {
        initScreen();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(clueStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {

        timeSinceLast += delta;

        if (timeSinceLast >= timePerIncrease)
        {
            timeSinceLast = 0f;
            clueImage.setSize(clueImage.getWidth() * 1.1f, clueImage.getHeight() * 1.1f);
            clueImage.setPosition(clueImage.getX() * -1.1f, clueImage.getY() * -1.1f);
        }

        clueStage.act();
        clueStage.draw();
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
