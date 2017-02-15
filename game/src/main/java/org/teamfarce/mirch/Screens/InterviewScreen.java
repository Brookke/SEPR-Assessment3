package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.Elements.InterviewResponseBox;
import org.teamfarce.mirch.Screens.Elements.InterviewResponseButton;
import org.teamfarce.mirch.Screens.Elements.StatusBar;
import org.teamfarce.mirch.Vector2Int;

import java.util.ArrayList;

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

        //Create table to contain background image
        Table interviewContainer = new Table();
        interviewContainer.setBounds(X_OFFSET, Y_OFFSET, WIDTH, HEIGHT);

        //Set background image for stage
        Texture background = new Texture(Gdx.files.internal("dialogue_b.png"));
        TextureRegion tr = new TextureRegion(background);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        interviewContainer.setBackground(trd);

        interviewStage.addActor(interviewContainer);

        Suspect suspect = new Suspect("Donald Trump", "POTUS", "Trumpo_sprite.png", new Vector2Int(1,1), null);
        initSuspectBox(suspect, "Hi, how can I help?");

        GameState currentState = gameSnapshot.getState();
        switch (currentState) {
            case interviewStart:
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

    private void initSuspectBox(Suspect suspect, String suspectDialogue) {
        Label suspectName = new Label(suspect.getName(), uiSkin);
        suspectName.setPosition(280, 540);
        suspectName.setFontScale(1.1f);
        this.interviewStage.addActor(suspectName);


        Label dialogue = new Label(suspectDialogue, uiSkin);
        dialogue.setPosition(280, 510);
        this.interviewStage.addActor(dialogue);

        Image suspectImage = new Image(suspect.getTexture());
        suspectImage.setPosition(200, 500);
        this.interviewStage.addActor(suspectImage);
    }



    private void initStartInterview() {
        ArrayList<InterviewResponseButton> buttonList = new ArrayList<>();

        InterviewResponseButton.EventHandler eventHandler = (result) -> handleStartResponse(result);
        buttonList.add(new InterviewResponseButton("Question the suspect", 0, eventHandler));
        buttonList.add(new InterviewResponseButton("Accuse the suspect", 1, eventHandler));
        buttonList.add(new InterviewResponseButton("Leave the interview", 2, eventHandler));

        InterviewResponseBox responseBox = new InterviewResponseBox("What would you like to do?", buttonList, uiSkin);

        Table responseBoxTable = responseBox.getContent();
        responseBoxTable.setPosition(450, 220);

        interviewStage.addActor(responseBoxTable);
    }

    private void handleStartResponse(int result) {
        switch (result) {
            case 0: //Question
                gameSnapshot.setState(GameState.interviewQuestion);
                break;
            case 1: //Accuse
                gameSnapshot.setState(GameState.interviewQuestion);
                break;
            case 2: //Ignore
                gameSnapshot.setState(GameState.map);
                break;
        }
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
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        interviewStage.dispose();
        statusBar.dispose();
    }
}
