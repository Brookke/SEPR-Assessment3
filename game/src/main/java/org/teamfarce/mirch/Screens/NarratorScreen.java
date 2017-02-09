package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
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

    private String currentMessage = "";
    private String endMessage = "";

    private TextArea speech;

    final static int FRAMES_PER_LETTER = 2;

    private int currentFrames = 0;

    final static int SPEECH_MARGIN = Gdx.graphics.getWidth() / 10;

    public NarratorScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        setSpeech("Hello, my name is Sir Heslington, kill me");
    }

    private void initStage()
    {
        narratorStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image background = new Image(new TextureRegion(Assets.loadTexture("rchimage.jpg")));
        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());

        String testText = "";

        for (int i = 0; i <= 1000; i ++)
        {
            testText = testText + "Aas";
        }

        testText = testText + "END";

        speech = new TextArea(currentMessage, uiSkin);
        speech.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.6f);
        speech.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 0.25f);

        narratorStage.addActor(background);
        narratorStage.addActor(speech);
    }

    private void updateSpeech()
    {
        if (endMessage.equals(currentMessage))
        {
            return;
        }

        currentMessage = currentMessage + endMessage.charAt(currentMessage.length());

        speech.setText(currentMessage);
    }

    public void setSpeech(String speech)
    {
        endMessage = speech;
        currentMessage = "";
    }

    @Override
    public void show() {
        initStage();
    }

    @Override
    public void render(float delta) {

        currentFrames ++;

        if (currentFrames >= FRAMES_PER_LETTER)
        {
            currentFrames = 0;
            updateSpeech();
        }

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
