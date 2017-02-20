package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.teamfarce.mirch.screens.*;

/**
 * Generates and controls all GUI screens
 * from the GUI controller.
 *
 * @author jasonmash
 */
public class GUIController {
    /**
     * Reference to main game, used to set current screen and access GameState
     */
    public MIRCH game;

    /**
     * Global skin used to render Scene2D controls
     */
    public Skin uiSkin;

    /**
     * State of the game last time update() was called
     */
    public GameState currentState;

    /**
     * Used to present the map screen when game state changes
     */
    public AbstractScreen mapScreen;

    /**
     * Used to present the journal screen when game state changes
     */
    public AbstractScreen journalScreen;

    /**
     * Used to present the interview screen when game state changes
     */
    public AbstractScreen interviewScreen;

    /**
     * Used to draw the narrator and his text
     */
    public NarratorScreen narratorScreen;

    public MainMenuScreen menuScreen;

    /**
     * Used to draw the screen for finding a clue
     */
    public FindClueScreen findClueScreen;

    /**
     * Constructor for GUIController, initialises required variables
     *
     * @param game Used to set current screen, and access GameState
     */
    GUIController(MIRCH game) {
        this.game = game;

        uiSkin = new Skin(Gdx.files.internal("skins/skin_pretty/skin.json")); //load ui skin from assets
    }


    /**
     * Initialises AbstractScreens ready to display later
     */
    public void initScreens() {
        mapScreen = new MapScreen(game, uiSkin);
        journalScreen = new JournalScreen(game, uiSkin);
        interviewScreen = new InterviewScreen(game, uiSkin);
        narratorScreen = new NarratorScreen(game, uiSkin);
        findClueScreen = new FindClueScreen(game, uiSkin);
        menuScreen = new MainMenuScreen(game, uiSkin);
    }


    /**
     * Updates current GUI screen
     * Usage: use within render() methods
     */
    public void update() {

        //Clear background
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Get latest game state
        GameState newState = game.gameSnapshot.getState();

        //Check if screen needs changing
        if (currentState != newState) {

            //Set new screen depending on current game state
            switch (newState) {
                case map:
                    this.game.setScreen(mapScreen);
                    break;
                case menu:
                    this.game.setScreen(menuScreen);
                    break;
                case narrator:
                    this.game.setScreen(narratorScreen);
                    break;
                case gameWon:
                    this.game.setScreen(narratorScreen);
                    break;
                case journalClues:
                    this.game.setScreen(journalScreen);
                    break;
                case journalNotepad:
                    this.game.setScreen(journalScreen);
                    break;
                case journalQuestions:
                    this.game.setScreen(journalScreen);
                    break;
                case interviewStart:
                    this.game.setScreen(interviewScreen);
                    break;
                case interviewQuestionClue:
                    this.game.setScreen(interviewScreen);
                    break;
                case interviewQuestionStyle:
                    this.game.setScreen(interviewScreen);
                    break;
                case interviewQuestion:
                    this.game.setScreen(interviewScreen);
                    break;
                case interviewAccuse:
                    this.game.setScreen(interviewScreen);
                    break;
                case findClue:
                    this.game.setScreen(findClueScreen);
                    break;
                default:
                    break;
            }

            //Update current screen to reflect changed screen
            currentState = newState;
        }

    }
}
