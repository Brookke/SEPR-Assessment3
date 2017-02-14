package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.Elements.StatusBar;

/**
 * Created by Jason on 13/02/2017.
 */
public class InterviewScreen extends AbstractScreen {

    private MIRCH game;
    private GameSnapshot gameSnapshot;

    public Stage interviewStage;
    private Skin uiSkin;
    private StatusBar statusBar;

    final static float X_OFFSET = 10;
    final static float Y_OFFSET = 20;
    final static float WIDTH = Gdx.graphics.getWidth() - (2 * X_OFFSET);
    final static float HEIGHT = Gdx.graphics.getHeight() - Y_OFFSET;

    public InterviewScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }

    private void initInterviewStage() {
        //Initialise stage used to show interview contents
        interviewStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Create table to represent journal "book"
        Table interviewContainer = new Table();
        interviewContainer.setBounds(X_OFFSET, Y_OFFSET, WIDTH, HEIGHT);

        //Set background image for stage
        Texture background = new Texture(Gdx.files.internal("dialogue_b.png"));
        TextureRegion tr = new TextureRegion(background);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        interviewContainer.setBackground(trd);

        interviewStage.addActor(interviewContainer);

        initCharacterBox();

        GameState currentState = gameSnapshot.getState();
        switch (currentState) {
            case interviewStart:
                //TODO: Show character with Question, Accuse, Ignore buttons
                initStartInterview();
                break;
            case interviewQuestion:
                //TODO: Show dialogue and possible responses
                break;
            case interviewAccuse:
                //TODO: Show user whether accusation is correct
                break;
            default:
                break;
        }
    }

    private void initCharacterBox() {
        Label comment = new Label("Hi! I'm character name, how can I help?", uiSkin);
        comment.setPosition(200, 550);
        comment.setFontScale(1.2f);
        this.interviewStage.addActor(comment);
    }

    private void initStartInterview() {
        Label comment = new Label("What would you like to do?", uiSkin);
        comment.setPosition(500, 280);
        comment.setFontScale(1.2f);
        this.interviewStage.addActor(comment);

        TextButton question = new TextButton("Question the suspect", uiSkin);
        question.setPosition(500, 220);
        interviewStage.addActor(question);

        TextButton accuse = new TextButton("Accuse the suspect", uiSkin);
        accuse.setPosition(700, 220);
        interviewStage.addActor(accuse);

        TextButton ignore = new TextButton("Leave the interview", uiSkin);
        ignore.setPosition(900, 220);
        interviewStage.addActor(ignore);
    }

    @Override
    public void show() {
        initInterviewStage();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(interviewStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        interviewStage.act();
        interviewStage.draw();
        statusBar.render();
    }

    @Override
    public void resize(int width, int height) {
        interviewStage.getViewport().update(width, height, false);
        statusBar.resize(width, height);
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
        interviewStage.dispose();
        statusBar.dispose();
    }
}
