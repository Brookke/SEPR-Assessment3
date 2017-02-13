package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
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

        String introSpeech = "You have been been invited to a lock-in costume party with some of the richest people around. There has been a murder, one of the guests has killed another.\n\n" +
                                "The murderer instantly regretted their decision and has tried their hardest to cover up their tracks. All the clues have been hidden around the Ron Cooke Hub by the murderer so that they can avoid being discovered.\n\n" +
                                "NOT SO FAST! You're not the only detective that got called to the scene, there will be other detectives trying to solve the case at the same time.\n\n" +
                                "You must go around each room trying to find the clues that have been hidden. You must also question the guests to see if they are know anything about the murder! Try to solve the case before any other detective!";

        //Set introduction speech
        setSpeech(introSpeech).show();
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
        speech.setWrap(true);

        prompt = new TextButton("Click to Continue", uiSkin);
        prompt.setSize(Gdx.graphics.getWidth() / 3, 50);
        prompt.setPosition(Gdx.graphics.getWidth() * 0.45f, Gdx.graphics.getHeight() * 0.25f);
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

    public NarratorScreen setSpeech(String speech)
    {
        endMessage = speech;
        currentMessage = "";

        return this;
    }

    public void makeVisible()
    {
        gameSnapshot.setState(GameState.narrator);
    }

    @Override
    public void show()
    {
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
