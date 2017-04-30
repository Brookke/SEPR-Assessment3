package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.MIRCH;

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
        this.gameSnapshot = game.getGameSnapshot();
        this.uiSkin = uiSkin;

        statusBar = new org.teamfarce.mirch.screens.elements.StatusBar(game.getGameSnapshot(), uiSkin);
    }

    /**
     * Initialises the GUI controls for the JournalScreen
     * Is called within show() to ensure data is up to date
     */
    private void initStage() {
        //Initialise stage used to show journal contents
        puzzleStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //Create table for the whole game
        TextButton nameLabel = new TextButton("Name", uiSkin);
        TextField nameText = new TextField("", uiSkin);
        Label addressLabel = new Label("Address:", uiSkin);
        TextField addressText = new TextField("", uiSkin);
        Table puzzleContainer = new Table();
        puzzleContainer.add(nameLabel);
        puzzleContainer.add(nameText).width(100);
        puzzleContainer.row();
        puzzleContainer.add(addressLabel);
        puzzleContainer.add(addressText).width(100);

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
