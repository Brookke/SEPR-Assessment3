package org.teamfarce.mirch.screens.elements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;


/**
 * Top status bar in game
 */
public class StatusBar {
    /**
     * Height of bar
     */
    public static final int HEIGHT = 40; //Used to set height of status bar

    /**
     * Item count
     */
    private static final int ITEM_COUNT = 5; //Used to set width of controls on bar

    /**
     * Width of bar
     */
    private static final int WIDTH = Gdx.graphics.getWidth() / ITEM_COUNT;

    /**
     * The stage to render the elements to
     */
    public Stage stage;

    private TextButton scoreLabel;
    private TextButton personalityMeter;

    /**
     * Game snapshot instance
     */
    private GameSnapshot gameSnapshot;

    /**
     * The initializer for the StatusBar
     * Sets up UI controls and adds them to the stage ready for rendering
     */
    public StatusBar(GameSnapshot snapshot, Skin uiSkin) {
        gameSnapshot = snapshot;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table statusBar = new Table();
        statusBar.setSize(Gdx.graphics.getWidth(), HEIGHT);
        statusBar.setPosition(0, Gdx.graphics.getHeight() - HEIGHT);
        statusBar.row().height(HEIGHT);
        statusBar.defaults().width(WIDTH);


        scoreLabel = new TextButton("Score: " + gameSnapshot.getScore(), uiSkin);
        statusBar.add(scoreLabel).uniform();

        TextButton mapButton = new TextButton("Map", uiSkin);
        statusBar.add(mapButton).uniform();

        TextButton journalButton = new TextButton("Journal", uiSkin);
        statusBar.add(journalButton).uniform();

        personalityMeter = new TextButton(getPersonalityMeterValue(), uiSkin);
        statusBar.add(personalityMeter).uniform();

        /* Event handlers */
        //add a listener for the show interview log button
        mapButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("map button was pressed");
                gameSnapshot.setState(GameState.map);
            }
        });

        //add a listener for the show interview log button
        journalButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Journal button was pressed");
                gameSnapshot.setState(GameState.journalClues);
            }
        });

        stage.addActor(statusBar);
    }

    /**
     * This method returns the String that is to represent the players current personality
     *
     * @return String - Representing their personality
     */
    private String getPersonalityMeterValue() {
        int personalityScore = gameSnapshot.getPersonality();
        String result = "";

        if (personalityScore > 5) {
            result = "Very polite";
        } else if (personalityScore <= 5 && personalityScore >= 2) {
            result = "Quite polite";
        } else if (personalityScore <= 1 && personalityScore >= -1) {
            result = "Conversational";
        } else if (personalityScore <= -2 && personalityScore >= -5) {
            result = "Quite aggressive";
        } else if (personalityScore < -5) {
            result = "Very aggressive";
        }
        return "Your Personality: " + result;
    }

    /**
     * Render function to display the status bar
     * Usage: call within the render() method in a screen
     */
    public void render() {
        scoreLabel.setText("Score: " + gameSnapshot.getScore());
        personalityMeter.setText(getPersonalityMeterValue());
        stage.act();

        stage.draw();
    }

    /**
     * Used for window resize event
     *
     * @param width  - updated width
     * @param height - updated height
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * This disposes all the elements
     */
    public void dispose() {
        stage.dispose();
    }
}
