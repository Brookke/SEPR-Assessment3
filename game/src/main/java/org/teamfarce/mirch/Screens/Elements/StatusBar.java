package org.teamfarce.mirch.Screens.Elements;


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
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Miscellaneous.Score;

/**
 * Top status bar in game
 */
public class StatusBar
{
    /**
     * Height of bar
     */
    public static final int HEIGHT = 40; //Used to set height of status bar

    /**
     * Item count
     */
    private static final int ITEM_COUNT = 4; //Used to set width of controls on bar

    /**
     * Width of bar
     */
    private static final int WIDTH = Gdx.graphics.getWidth() / ITEM_COUNT;

    /**
     * The stage to render the elements to
     */
    public Stage stage;

    /**
     * Game snapshot instance
     */
    private GameSnapshot gameSnapshot;

    /**
     * The initializer for the StatusBar
     * Sets up UI controls and adds them to the stage ready for rendering
     */
    public StatusBar(GameSnapshot snapshot, Skin uiSkin)
    {
        gameSnapshot = snapshot;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table statusBar = new Table();
        statusBar.setSize(Gdx.graphics.getWidth(), HEIGHT);
        statusBar.setPosition(0, Gdx.graphics.getHeight() - HEIGHT);
        statusBar.row().height(HEIGHT);
        statusBar.defaults().width(WIDTH);

        TextButton scoreLabel = new TextButton("Score: "+ MIRCH.score.getScore(), uiSkin);
        //scoreLabel.setAlignment(Align.center, Align.center);
        statusBar.add(scoreLabel).uniform();

        TextButton mapButton = new TextButton("Map", uiSkin);
        statusBar.add(mapButton).uniform();

        TextButton journalButton = new TextButton("Journal", uiSkin);
        statusBar.add(journalButton).uniform();

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

        stage.addActor(statusBar);
    }

    /**
     * Render function to display the status bar
     * Usage: call within the render() method in a screen
     */
    public void render()
    {
        stage.act();
        stage.draw();
    }

    /**
     * Used for window resize event
     *
     * @param width  - updated width
     * @param height - updated height
     */
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    /**
     * This disposes all the elements
     */
    public void dispose()
    {
        stage.dispose();
    }
}
