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
import org.teamfarce.mirch.*;
import org.teamfarce.mirch.Entities.AbstractPerson;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.Screens.Elements.InterviewResponseBox;
import org.teamfarce.mirch.Screens.Elements.InterviewResponseButton;
import org.teamfarce.mirch.Screens.Elements.StatusBar;

import java.util.ArrayList;
import java.util.Set;


/**
 * The InterviewScreen contains GUI for these GameStates:
 * interviewStart, interviewQuestion, interviewAccuse
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

    private Suspect suspect = null;

    /**
     * Constructor for Interview screen
     * @param game Reference to current game
     * @param uiSkin Skin reference for UI controls
     */
    public InterviewScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }


    /**
     * Prepares the stage ready to render on screen
     * Adds GUI controls to the stage
     */
    private void initInterviewStage() {
        //Initialise stage used to show interview contents
        interviewStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Create table for a background image
        Table interviewContainer = new Table();
        interviewContainer.setBounds(X_OFFSET, Y_OFFSET, WIDTH, HEIGHT);
        Texture background = new Texture(Gdx.files.internal("dialogue_b.png"));
        TextureRegion tr = new TextureRegion(background);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        interviewContainer.setBackground(trd);
        interviewStage.addActor(interviewContainer);

        suspect = gameSnapshot.getSuspectForInterview();

        if (suspect == null)
        {
            throw new NullPointerException("No Suspect Defined for Interview Screen");
        }

        //Setup vars needed to render dialogue & responses
        String responseBoxInstructions = "";
        String suspectDialogue = "";
        ArrayList<InterviewResponseButton> buttonList = new ArrayList<>();
        InterviewResponseButton.EventHandler switchStateHandler = (result, clue) -> switchState(result);
        InterviewResponseButton.EventHandler clueHandler = (result, clue) -> questionClue(result, clue);

        //Check current GameState, and render appropriate GUI
        GameState currentState = gameSnapshot.getState();
        switch (currentState) {
            case interviewStart:
                //Setup suspect's dialogue
                suspectDialogue = suspect.dialogue.get("introduction");

                //Set initial instructions for player
                responseBoxInstructions = "What would you like to do?";

                //Setup buttons to Question, Accuse and Ignore
                buttonList.add(new InterviewResponseButton("Question the suspect", 0, null, switchStateHandler));
                buttonList.add(new InterviewResponseButton("Accuse the suspect", 1,null, switchStateHandler));
                buttonList.add(new InterviewResponseButton("Leave the interview", 2,null, switchStateHandler));

                break;


            case interviewQuestionClue:
                //Setup suspect's dialogue
                suspectDialogue = "";

                //Ask player how to respond
                responseBoxInstructions = "What would you like to ask about";

                //Setup buttons to Question, Accuse and Ignore

                //TODO: update to journal
                for (Clue c : game.gameSnapshot.getClues()) {
                    if (!c.isMotiveClue()) {
                        buttonList.add(new InterviewResponseButton(c.getName(),0, c, clueHandler));
                    }
                }

                break;

            case interviewQuestion:
                //Setup suspect's dialogue
                suspectDialogue = "Hmm, this is a reply";

                //Ask player how to respond
                responseBoxInstructions = "How would you like to respond?";

                //Setup buttons to Question, Accuse and Ignore
                buttonList.add(new InterviewResponseButton("Nicely", 0,null, switchStateHandler));
                buttonList.add(new InterviewResponseButton("Neutrally", 1,null, switchStateHandler));
                buttonList.add(new InterviewResponseButton("Angrily", 2,null, switchStateHandler));

                break;

            case interviewAccuse:
                //Check whether accusation is correct
                boolean hasEvidence = gameSnapshot.isMeansProven() && gameSnapshot.isMotiveProven();
                if (suspect.accuse(hasEvidence)) {
                    //Setup suspect's dialogue
                    suspectDialogue = "Oh dear, you've caught me red handed. I confess to killing them.";

                    //Inform user of result
                    responseBoxInstructions = "How would you like to respond?";
                    buttonList.add(new InterviewResponseButton("Arrest " + suspect.getName(), 3,null, switchStateHandler));

                } else {
                    //Setup suspect's dialogue
                    suspectDialogue = "Ha! You don't have the evidence to accuse me!";

                    //Inform user of result
                    responseBoxInstructions = "How would you like to respond?";
                    buttonList.add(new InterviewResponseButton("Apologise & leave the interview", 2,null, switchStateHandler));
                }
                break;
        }

        //Render Suspect's dialogue to the screen
        initSuspectBox(suspect, suspectDialogue);

        //Render Player's response options to the screen
        InterviewResponseBox responseBox = new InterviewResponseBox(responseBoxInstructions, buttonList, uiSkin);
        Table responseBoxTable = responseBox.getContent();
        responseBoxTable.setPosition(450, 220);
        interviewStage.addActor(responseBoxTable);
    }


    /**
     * Initialises GUI controls for Suspect's dialogue
     * @param suspect Reference to instance of Suspect class
     * @param suspectDialogue Speech for the suspect to say
     */
    private void initSuspectBox(Suspect suspect, String suspectDialogue) {
        Label suspectName = new Label(suspect.getName(), uiSkin);
        suspectName.setPosition(280, 540);
        suspectName.setFontScale(1.1f);
        this.interviewStage.addActor(suspectName);

        Label dialogue = new Label(suspectDialogue, uiSkin);
        dialogue.setPosition(280, 510);
        this.interviewStage.addActor(dialogue);

        Image suspectImage = new Image(new TextureRegion(suspect.getTexture(), 0, 0, AbstractPerson.SPRITE_WIDTH, AbstractPerson.SPRITE_HEIGHT));
        suspectImage.setSize(AbstractPerson.SPRITE_WIDTH * 2f, AbstractPerson.SPRITE_HEIGHT * 2f);
        suspectImage.setPosition(200, 475);
        this.interviewStage.addActor(suspectImage);
    }

    /**
     * Event handler that switches game state when player selects a response
     * @param result Int associated with each state
     */
    private void switchState(int result) {
        switch (result) {
            case 0: //Question
                gameSnapshot.setState(GameState.interviewQuestionClue);
                break;
            case 1: //Accuse
                gameSnapshot.setState(GameState.interviewAccuse);
                break;
            case 2: //Ignore or return to map
                gameSnapshot.setState(GameState.map);
                suspect.canMove = true;
                suspect = null;
                gameSnapshot.setSuspectForInterview(null);
                game.player.clearTalkTo();
                break;
            case 3: //Game has been won
                gameSnapshot.gameWon = true;
                gameSnapshot.setState(GameState.gameWon);
                break;
        }
    }

    private void questionClue(int result, Clue clue) {
        System.out.println(clue.getName());
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
