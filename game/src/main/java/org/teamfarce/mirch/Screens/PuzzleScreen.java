package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.PuzzleGame;

/**
 * Created by jacks on 16/03/2017.
 */
public class PuzzleScreen extends org.teamfarce.mirch.screens.AbstractScreen {

    public Stage puzzleStage;
    private MIRCH game;
    private GameSnapshot gameSnapshot;
    private Skin uiSkin;
    private org.teamfarce.mirch.screens.elements.StatusBar statusBar;

    /**
     * @param game   Reference
     * @param uiSkin
     */
    public PuzzleScreen(MIRCH game, Skin uiSkin) {
        super(game);
        this.game = game;
        this.gameSnapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new org.teamfarce.mirch.screens.elements.StatusBar(game.gameSnapshot, uiSkin);
    }

    /**
     * Initialises the GUI controls for the JournalScreen
     * Is called within show() to ensure data is up to date
     */
    private void initStage() {
        //Initialise stage used to show journal contents
        puzzleStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Create table elements for the puzzle
        Label puzzleTitle = new Label("Nine Shall Pass", uiSkin);
        TextButton value10 = new TextButton( "10", uiSkin);
        TextButton value9 = new TextButton( "9", uiSkin);
        TextButton value8 = new TextButton( "8", uiSkin);
        TextButton value7 = new TextButton( "7", uiSkin);
        TextButton value6 = new TextButton( "6", uiSkin);
        TextButton value5 = new TextButton( "5", uiSkin);
        TextButton value4 = new TextButton( "4", uiSkin);
        TextButton value3 = new TextButton( "3", uiSkin);

        puzzleTitle.setFontScale(2);
        value9.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //value9.setText("Done Click");
                gameSnapshot.puzzleGame.setPuzzleWon();
                gameSnapshot.setState(GameState.map);
            }
        });



        Table puzzleContainer = new Table();
        puzzleContainer.row().colspan(8);
        puzzleContainer.defaults().width(80);
        puzzleContainer.add(puzzleTitle);
        puzzleContainer.row();
        puzzleContainer.add( value10 );
        puzzleContainer.add( value9 );
        puzzleContainer.add( value8 );
        puzzleContainer.add( value7 );
        puzzleContainer.add( value6 );
        puzzleContainer.add( value5 );
        puzzleContainer.add( value4 );
        puzzleContainer.add( value3 );

        puzzleContainer.setBounds(10, 20, Gdx.graphics.getWidth() - (20), Gdx.graphics.getHeight() - 20);
        //Set background image for journal
        Texture journalBackground = new Texture(Gdx.files.internal("Open_journal.png"));
        TextureRegion tr = new TextureRegion(journalBackground);
        TextureRegionDrawable trd = new TextureRegionDrawable(tr);
        puzzleContainer.setBackground(trd);

        puzzleStage.addActor(puzzleContainer);
    }

    /**
     * Called whenever JournalScreen is about to become visible
     */
    @Override
    public void show() {
        initStage();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(puzzleStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Render function to display the JournalScreen
     */
    @Override
    public void render(float delta) {
        puzzleStage.act();
        puzzleStage.draw();
        statusBar.render();
    }

    /**
     * Used for window resize event
     *
     * @param width  - updated width
     * @param height - updated height
     */
    @Override
    public void resize(int width, int height) {
        puzzleStage.getViewport().update(width, height, false);
        statusBar.resize(width, height);
    }

    /**
     * Pause method
     */
    @Override
    public void pause() {
    }

    /**
     * Resume method
     */
    @Override
    public void resume() {
    }

    /**
     * Hide method
     */
    @Override
    public void hide() {
    }

    /**
     * Disposes of JournalScreen resources
     */
    @Override
    public void dispose() {
        puzzleStage.dispose();
        statusBar.dispose();
    }

}
