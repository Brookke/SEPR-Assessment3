package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.w3c.dom.Text;

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

    private Label speech;
    private Button prompt;

    final static int FRAMES_PER_LETTER = 2;

    private int currentFrames = 0;

    final static int SPEECH_MARGIN = Gdx.graphics.getWidth() / 10;

    public NarratorScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        //Set introduction speech
        setSpeech("Hello, my name is Sir Heslington.\n\n Last night, there was a murder in the Ron Cooke Hub! It is your job to try and find out who did it!" +
                "\n\nWander around the Hub and try to find clues and talk to the suspsects to work out who the murderer is!");
    }

    private void initStage()
    {
        narratorStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image background = new Image(new TextureRegion(Assets.loadTexture("rchimage.jpg")));
        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());

        TextField.TextFieldStyle textFieldStyle = uiSkin.get(TextField.TextFieldStyle.class);
        textFieldStyle.fontColor = Color.WHITE;

        speech = new Label(currentMessage, uiSkin);
        speech.getStyle().fontColor = Color.WHITE;
        speech.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.6f);
        speech.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 0.25f);

        prompt = new TextButton("Click to Continue", uiSkin);
        prompt.setSize(Gdx.graphics.getWidth() / 3, 50);
        prompt.setPosition(Gdx.graphics.getWidth() * 0.55f, Gdx.graphics.getHeight() * 0.25f);
        prompt.setVisible(false);
        prompt.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameSnapshot.setState(GameState.map);
            }
        });

        narratorStage.addActor(background);
        narratorStage.addActor(speech);
        narratorStage.addActor(prompt);
    }

    private void updateSpeech()
    {
        if (endMessage.equals(currentMessage))
        {
            prompt.setVisible(true);
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

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(narratorStage);
        Gdx.input.setInputProcessor(multiplexer);
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
