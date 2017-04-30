package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.entities.AbstractPerson;
import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.map.Room;
import org.teamfarce.mirch.screens.elements.InterviewResponseBox;
import org.teamfarce.mirch.screens.elements.InterviewResponseButton;

import java.util.ArrayList;


/**
 * The InterviewScreen contains GUI for these GameStates:
 * interviewStart, interviewQuestionStyle, interviewAccuse, interviewQuestion,
 * interviewQuestionClue
 */
public class InterviewScreen extends AbstractScreen {

    final static float X_OFFSET = 10;
    final static float Y_OFFSET = 20;
    final static float WIDTH = Gdx.graphics.getWidth() - (2 * X_OFFSET);
    final static float HEIGHT = Gdx.graphics.getHeight() - Y_OFFSET;
    public Stage interviewStage;
    private MIRCH game;
    private Skin uiSkin;
    private Suspect suspect = null;
    private Clue tempClue;
    private String tempStyle;

    private Label scoreLabel = null;

    /**
     * Constructor for Interview screen
     *
     * @param game   Reference to current game
     * @param uiSkin Skin reference for UI controls
     */
    public InterviewScreen(MIRCH game, Skin uiSkin) {
        super(game);
        this.game = game;
        this.uiSkin = uiSkin;
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

        suspect = game.getGameSnapshot().getSuspectForInterview();

        if (suspect == null) {
            throw new NullPointerException("No Suspect Defined for Interview Screen");
        }

        //Check if the suspect is locked from talking to or not
        if (suspect.speechLocked()) {
            game.getGameSnapshot().setState(GameState.interviewLock);
        } else {
            game.getGameSnapshot().setAllUnlocked();
        }

        //Setup vars needed to render dialogue & responses
        String responseBoxInstructions = "";
        String suspectDialogue = "";
        boolean useMultiRowResponseBox = false;
        ArrayList<InterviewResponseButton> buttonList = new ArrayList<>();
        InterviewResponseButton.EventHandler switchStateHandler = (result, clue) -> switchState(result);
        InterviewResponseButton.EventHandler clueHandler = (result, clue) -> questionClueHandler(result, clue);
        InterviewResponseButton.EventHandler styleHandler = (result, clue) -> questionStyleHandler(result);

        scoreLabel = new Label("Score: " + game.getGameSnapshot().getScore(), uiSkin, "white");
        scoreLabel.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 2);
        scoreLabel.setFontScale(2);

        //Check current GameState, and render appropriate GUI
        GameState currentState = game.getGameSnapshot().getState();
        switch (currentState) {
            case interviewStart:
                //Setup suspect's dialogue
                suspectDialogue = suspect.dialogue.get("introduction");

                //Set initial instructions for currentPlayer
                responseBoxInstructions = "What would you like to do?";

                //Setup buttons to Question, Accuse and Ignore
                buttonList.add(new InterviewResponseButton("Question the suspect", 0, null, switchStateHandler));

                if (game.getGameSnapshot().isMeansProven() && game.getGameSnapshot().isMotiveProven()) {
                    buttonList.add(new InterviewResponseButton("Accuse the suspect", 1, null, switchStateHandler));
                }

                buttonList.add(new InterviewResponseButton("Ignore the suspect", 2, null, switchStateHandler));

                break;


            case interviewQuestionClue:
                //Setup suspect's dialogue
                suspectDialogue = "";

                if (game.getGameSnapshot().journal.getQuestionableClues().size() != 0) {
                    //Ask currentPlayer how to respond
                    responseBoxInstructions = "What would you like to ask about";

                    //Setup buttons to Question, Accuse and Ignore

                    for (Clue c : game.getGameSnapshot().journal.getQuestionableClues()) {
                        buttonList.add(new InterviewResponseButton(c.getName(), 0, c, clueHandler));
                    }
                } else {
                    responseBoxInstructions = "You haven't found any clues yet to ask about";
                    buttonList.add(new InterviewResponseButton("Leave the interview", 4, null, switchStateHandler));
                }

                break;

            case interviewQuestionStyle:
                useMultiRowResponseBox = true;

                //Setup suspect's dialogue
                suspectDialogue = "";

                //Ask currentPlayer how to respond
                responseBoxInstructions = "How do you want to ask the question?";

                //Check personality level
                int personality = game.getGameSnapshot().getPersonality();

                //Setup buttons to Question, Accuse and Ignore, dependant on personality
                if (personality <= 5) {
                    buttonList.add(new InterviewResponseButton("Aggressively: " + game.currentPlayer.dialogue.get(tempClue, "AGGRESSIVE"), 0, null, styleHandler));
                }

                buttonList.add(new InterviewResponseButton("Conversational: " + game.currentPlayer.dialogue.get(tempClue, "CONVERSATIONAL"), 1, null, styleHandler));

                if (personality >= -5) {
                    buttonList.add(new InterviewResponseButton("Politely: " + game.currentPlayer.dialogue.get(tempClue, "POLITE"), 2, null, styleHandler));
                }

                break;

            case interviewQuestion:
                //Setup suspect's dialogue
                suspectDialogue = suspect.dialogue.get(tempClue, tempStyle);

                //Checks to see if a valid response has been provided
                if (suspectDialogue.length() == 0) {
                    suspectDialogue = suspect.dialogue.get("none");
                    game.getGameSnapshot().modifyScore(-2);
                } else {
                    game.getGameSnapshot().journal.addConversation(String.format("%s?: %s ", tempClue.getName(), suspectDialogue), suspect.getName());
                }

                //Ask currentPlayer how to respond
                responseBoxInstructions = "How would you like to respond?";
                buttonList.add(new InterviewResponseButton("Question the suspect again", 0, null, switchStateHandler));

                if (game.getGameSnapshot().isMeansProven() && game.getGameSnapshot().isMotiveProven()) {
                    buttonList.add(new InterviewResponseButton("Accuse the suspect", 1, null, switchStateHandler));
                }

                buttonList.add(new InterviewResponseButton("Ignore the suspect", 2, null, switchStateHandler));
                break;

            case interviewAccuse:
                //Check whether accusation is correct
                boolean hasEvidence = game.getGameSnapshot().isMeansProven() && game.getGameSnapshot().isMotiveProven();
                if (suspect.accuse(hasEvidence)) {
                    //Setup suspect's dialogue
                    suspectDialogue = "Oh dear, you've caught me red handed. I confess to killing them.";

                    //Inform user of result
                    responseBoxInstructions = "How would you like to respond?";
                    buttonList.add(new InterviewResponseButton("Arrest " + suspect.getName(), 3, null, switchStateHandler));
                } else {
                    //Setup suspect's dialogue
                    suspectDialogue = "Ha! You don't have the evidence to accuse me!";

                    //Inform user of result
                    responseBoxInstructions = "How would you like to respond?";
                    buttonList.add(new InterviewResponseButton("Apologise & leave the interview", 2, null, switchStateHandler));
                }
                break;

            case interviewLock:
                suspectDialogue = "If you don't want to talk to me. I don't want to talk to you.";
                responseBoxInstructions = "Maybe if I explore some more they will talk";
                buttonList.add(new InterviewResponseButton("Exit Interview", 4, null, switchStateHandler));
                break;
        }

        //Render Suspect's dialogue to the screen
        initSuspectBox(suspect, suspectDialogue);

        //Render Player's response options to the screen
        InterviewResponseBox responseBox = new InterviewResponseBox(responseBoxInstructions, buttonList, uiSkin, useMultiRowResponseBox);
        Table responseBoxTable = responseBox.getContent();

        //Position response box
        responseBoxTable.setX(430);
        if (useMultiRowResponseBox) {
            responseBoxTable.setY(170);
        } else {
            responseBoxTable.setY(220);
        }

        //Add response box to screen
        interviewStage.addActor(responseBoxTable);
        interviewStage.addActor(scoreLabel);
    }

    /**
     * This method is called when the currentPlayer has won the game
     */
    private void winGame() {
        game.getGUIController().narratorScreen.setButton("Return to Office", new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });

        String room = "";

        //Get the murder room name
        for (Room r : game.getGameSnapshot().map.getRooms()) {
            if (r.isMurderRoom()) {
                room = r.getName();
            }
        }

        game.getGUIController().narratorScreen.setSpeech("Congratulations! You solved it!\n\n" +
                "All along it was " + game.getGameSnapshot().murderer.getName() + " who killed " + game.getGameSnapshot().victim.getName() + " with " + game.getGameSnapshot().meansClue.getName() + " in the " + room + "\n\nI would never have been able to work that out!\n\nYou completed the game with a score of " + game.getGameSnapshot().getScore() + ", that's very impressive!");

        game.getGameSnapshot().gameWon = true;
        game.getGameSnapshot().setState(GameState.gameWon);
    }

    /**
     * Initialises GUI controls for Suspect's dialogue
     *
     * @param suspect         Reference to instance of Suspect class
     * @param suspectDialogue Speech for the suspect to say
     */
    private void initSuspectBox(Suspect suspect, String suspectDialogue) {
        Label suspectName = new Label(suspect.getName(), uiSkin);
        suspectName.setPosition(280, 540);
        suspectName.setFontScale(1.1f);
        this.interviewStage.addActor(suspectName);

        Label dialogue = new Label(suspectDialogue, uiSkin);
        dialogue.setPosition(280, 510);
        dialogue.setWidth(440);
        dialogue.setWrap(true);
        this.interviewStage.addActor(dialogue);

        Image suspectImage = new Image(new TextureRegion(suspect.getTexture(), 0, 0, AbstractPerson.SPRITE_WIDTH, AbstractPerson.SPRITE_HEIGHT));
        suspectImage.setSize(AbstractPerson.SPRITE_WIDTH * 2f, AbstractPerson.SPRITE_HEIGHT * 2f);
        suspectImage.setPosition(200, 475);
        this.interviewStage.addActor(suspectImage);
    }

    /**
     * Event handler that switches game state when currentPlayer selects a response
     *
     * @param result Int associated with each state
     */
    private void switchState(int result) {
        switch (result) {
            case 0: //Question
                game.getGameSnapshot().setState(GameState.interviewQuestionClue);
                tempClue = null;
                break;
            case 1: //Accuse
                game.getGameSnapshot().setState(GameState.interviewAccuse);
                break;
            case 2: //Ignore or return to map
                game.getGameSnapshot().setState(GameState.map);
                suspect.canMove = true;
                suspect.setLocked(true);
                suspect = null;
                game.getGameSnapshot().setSuspectForInterview(null);
                game.currentPlayer.clearTalkTo();
                break;
            case 3: //Game has been won
                winGame();
                break;
            case 4:
                game.getGameSnapshot().setState(GameState.map);
                suspect.canMove = true;
                suspect = null;
                game.getGameSnapshot().setSuspectForInterview(null);
                game.currentPlayer.clearTalkTo();
                break;
        }
    }

    /**
     * Handles result of currentPlayer selecting clue to question suspect about
     *
     * @param clue The clue the currentPlayer has selected
     * @param result Not used
     */
    private void questionClueHandler(int result, Clue clue) {
        switch (result) {
            case 0:
                tempClue = clue;
                game.getGameSnapshot().setState(GameState.interviewQuestionStyle);
                break;
        }
    }

    /**
     * Handles result of currentPlayer selecting question style to use when questioning suspect
     * Also updates personality meter based on result
     *
     * @param result Returns integer corresponding to question style type
     *               0 = Aggressive, 1 = Conversational, 2 = Polite
     */
    private void questionStyleHandler(int result) {
        switch (result) {
            case 0:
                tempStyle = "AGGRESSIVE";
                game.getGameSnapshot().modifyPersonality(-1);
                break;
            case 1:
                tempStyle = "CONVERSATIONAL";

                //When user chooses conversational dialogue, personality tends towards 0
                int personality = game.getGameSnapshot().getPersonality();
                if (personality > 0) {
                    game.getGameSnapshot().modifyPersonality(-1);
                } else if (personality < 0) {
                    game.getGameSnapshot().modifyPersonality(1);
                }

                break;
            case 2:
                tempStyle = "POLITE";
                game.getGameSnapshot().modifyPersonality(1);
                break;
        }
        game.getGameSnapshot().setState(GameState.interviewQuestion);
    }

    @Override
    public void show() {
        initInterviewStage();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(interviewStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        scoreLabel.setText("Score: " + game.getGameSnapshot().getScore());
        interviewStage.act();
        interviewStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        interviewStage.getViewport().update(width, height, false);
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
    }
}
