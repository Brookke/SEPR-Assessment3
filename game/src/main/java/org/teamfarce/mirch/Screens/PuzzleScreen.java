package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
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

    public String codeEntered = "CODE";
    public String codeNeeded = "CODE42";

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

    public class BookActor extends Actor{
        Texture texture;
        float actorX = 0, actorY = 0;
        String codeDigit;

        public BookActor(String ImagePath, String bookNumber, float xPos, float yPos){
            setTouchable(Touchable.enabled);
            texture = new Texture(Gdx.files.internal(ImagePath));
            codeDigit = bookNumber;
            actorX = xPos;
            actorY = yPos;
            setBounds(actorX,actorY,texture.getWidth(),texture.getHeight());
            addListener(new InputListener(){
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(codeDigit.contentEquals("Clear")){
                        codeEntered = "CODE";
                    }else {
                        codeEntered += codeDigit;
                    }
                    System.out.println(codeEntered);
                    if(codeEntered.contentEquals(codeNeeded)){
                        System.out.println("MATCHED");
                        gameSnapshot.puzzleGame.setPuzzleWon();
                        gameSnapshot.setState(GameState.map);
                    }

                    return true;
                }
            });
        }

        @Override
        public void draw(Batch batch, float alpha){
            batch.draw(texture,actorX,actorY);
        }
    }

    /**
     * Initialises the GUI controls for the JournalScreen
     * Is called within show() to ensure data is up to date
     */
    private void initStage() {
        //Initialise stage used to show journal contents
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        //positioning
        float col1 = (width/2) + (width/16);
        float col2 = col1 + ((width/64));
        float col3 = col2 + ((width/64));

        float row1 = (height/2) + ((height/64)*14) + (height/128);
        float row2 = (height/2) + ((height/64)*2);
        float row3 = ((height/2) - ((height/16)*3)) + ((height/64)*2);

        puzzleStage = new Stage(new FitViewport(width, height));
        //Set background image for puzzle screen
        Image backgroundImage = new Image(new TextureRegion(new Texture(Gdx.files.internal("PuzzleBookcase.png"))));
        backgroundImage.setWidth(Gdx.graphics.getWidth());
        backgroundImage.setHeight(Gdx.graphics.getHeight());

        BookActor book1 = new BookActor("puzzlePieces/book1.png", "1", col1, row1);
        BookActor book2 = new BookActor("puzzlePieces/book2.png", "2", col2, row1);
        BookActor book3 = new BookActor("puzzlePieces/book3.png", "3", col3, row1);
        BookActor book4 = new BookActor("puzzlePieces/book4.png", "4", col1-(width/(128/4)), row2);
        BookActor book5 = new BookActor("puzzlePieces/book5.png", "5", col2, row2);
        BookActor book6 = new BookActor("puzzlePieces/book6.png", "6", col3, row2);
        BookActor book7 = new BookActor("puzzlePieces/book7.png", "7", col1, row3);
        BookActor book8 = new BookActor("puzzlePieces/book8.png", "8", col2, row3);
        BookActor book9 = new BookActor("puzzlePieces/book9.png", "9", col3, row3);
        BookActor bookClear = new BookActor("puzzlePieces/bookClear.png", "Clear", col1, (height/2)-((height/16)*5));

        puzzleStage.addActor(backgroundImage);
        puzzleStage.addActor(book1);
        puzzleStage.addActor(book2);
        puzzleStage.addActor(book3);
        puzzleStage.addActor(book4);
        puzzleStage.addActor(book5);
        puzzleStage.addActor(book6);
        puzzleStage.addActor(book7);
        puzzleStage.addActor(book8);
        puzzleStage.addActor(book9);
        puzzleStage.addActor(bookClear);

    }

    /**
     * Called whenever PuzzleScreen is about to become visible
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
