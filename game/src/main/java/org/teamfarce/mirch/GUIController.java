package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Generates and controlls all GUI elements in the Game. the GUIController also handles inputs
 * from the GUI controller.
 *
 * @author jacobwunwin
 */
public class GUIController
{
    Gdx globalGdx;
    Stage controlStage;
    private JournalGUIController journalGUI;
    private GameSnapshot gameSnapshot;
    private Skin uiSkin;
    private SpriteBatch batch;
    private InterviewGUIController interviewController;

    /**
     * Initialises the GUI controller
     *
     * @param skin
     * @param gSnapshot
     * @param batch
     */
    GUIController(Skin skin, GameSnapshot gSnapshot, SpriteBatch batch)
    {
        this.gameSnapshot = gSnapshot;
        this.uiSkin = skin;
        this.batch = batch;
        this.journalGUI = new JournalGUIController(this.uiSkin, this.gameSnapshot, this.batch);
        this.interviewController = new InterviewGUIController(this.uiSkin, this.gameSnapshot, this.batch);

        //++++CREATE MAIN CONRTOL STAGE++++
        final TextButton mapButton = new TextButton("Map", this.uiSkin);
        final TextButton journalButton = new TextButton("Journal", this.uiSkin);

        mapButton.setPosition(500, 700);
        journalButton.setPosition(650, 700);

        this.controlStage = new Stage();

        this.controlStage.addActor(mapButton);
        this.controlStage.addActor(journalButton);

        //add a listener for the show interview log button
        mapButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                System.out.println("Map button was pressed");
                gameSnapshot.setState(GameState.map);
            }
        });

        //add a listener for the show interview log button
        journalButton.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                System.out.println("Journal button was pressed");
                gameSnapshot.setState(GameState.journalHome);
            }
        });
    }

    /**
     * Draws the GUI control button
     */
    void drawControlStage()
    {
        this.controlStage.draw();
    }

    /**
     * Draw the journal home view
     */
    void useJournalHomeView()
    {
        //Create an input multiplexer to take input from every stage
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.journalGUI.journalStage);
        multiplexer.addProcessor(this.controlStage);
        Gdx.input.setInputProcessor(multiplexer);

        this.controlStage.draw(); //draw the global control buttons
        this.journalGUI.drawHome();
    }

    /**
     * Draw the journal clues view
     */
    void useJournalCluesView()
    {
        //Create an input multiplexer to take input from every stage
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.journalGUI.journalStage);
        multiplexer.addProcessor(this.controlStage);
        multiplexer.addProcessor(this.journalGUI.journalCluesStage);
        Gdx.input.setInputProcessor(multiplexer);

        this.controlStage.draw(); //draw the global control buttons
        this.journalGUI.drawClues();
    }

    /**
     * Draw the journal notepad view
     */
    void useJournalNotepadView()
    {
        //Create an input multiplexer to take input from every stage
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.journalGUI.journalStage);
        multiplexer.addProcessor(this.journalGUI.journalNotepadStage);
        multiplexer.addProcessor(this.controlStage);
        Gdx.input.setInputProcessor(multiplexer);

        this.drawControlStage();
        this.journalGUI.drawNotepad();
    }

    /**
     * draw the current journal interview view
     */
    void useJournalInterviewView()
    {
        //Create an input multiplexer to take input from every stage
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this.journalGUI.journalStage);
        multiplexer.addProcessor(this.journalGUI.journalQuestionsStage);
        multiplexer.addProcessor(this.controlStage);
        Gdx.input.setInputProcessor(multiplexer);

        this.drawControlStage();
        this.journalGUI.drawInterviewLog();
    }

    /**
     * Initialise the interview gui with a suspect and a player
     *
     * @param suspect
     * @param player
     */
    void initialiseInterviewGUI(Suspect suspect, Sprite player)
    {
        this.interviewController.initInterviewStage(suspect, player);
    }

    /**
     * Draw the interview GUI
     */
    void drawInterviewGUI()
    {
        Gdx.input.setInputProcessor(this.interviewController.interviewStage);
        this.interviewController.display();
    }

    /**
     * Draw the accuse GUI
     */
    void drawAccuseGUI()
    {
        Gdx.input.setInputProcessor(this.interviewController.interviewStage);
        this.interviewController.display();
    }

    /**
     * Draw the win/end screen
     */
    void drawWinScreen()
    {
        Texture texture = new Texture(Gdx.files.internal("assets/win_screen.png"));
        Sprite winScreen = new Sprite(texture);
        winScreen.setPosition(240, 100);
        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);

        this.batch.begin();
        winScreen.draw(batch);
        font.draw(batch, "You have Won! You accused the right person!", 550, 600);
        font.draw(batch, "You took this many moves: " + this.gameSnapshot.getTime(), 600, 400);
        this.batch.end();
    }

}
