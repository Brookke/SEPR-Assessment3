package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import com.badlogic.gdx.InputMultiplexer;

import org.teamfarce.mirch.MIRCH;

/**
 * Created by vishal on 15/02/2017.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * The width of the menu
     */
    private static final int WIDTH = Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8;


    //Initialising necessary objects and variables

    /**
     * This is the referencing to the game snapshot
     */
    private GameSnapshot gameSnapshot;

    /**
     * the stage to render the menu to
     */
    public Stage stage;

    /**
     * This is the camera for the menu
     */
    private OrthographicCamera camera;

    /**
     * This is the sprite batch of the menu that elements are rendered on.
     */
    private SpriteBatch batch;

    /**
     * Constructor for the menu
     *
     * @param game - The game object the menu is being loaded for
     */
    public MainMenuScreen(final MIRCH game, Skin uiSkin) {

        super(game);
        this.gameSnapshot = game.gameSnapshot;

        //Initialising new stage
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //Loading the menu
        initMenu(game);
    }

    private void initMenu(final MIRCH game) {
        //Initialising the skin used for the buttons and the label
        Skin glassySkin;
        glassySkin = new Skin(Gdx.files.internal("skins/skin_glassy/glassy-ui.json"));

        //Setting the background
        Image background = new Image(new TextureRegion(Assets.loadTexture("rch3.jpg")));

        //Creating the label containing text and determining  its size and location on screen
        Label text;

        text = new Label("Welcome to the Lorem Ipsum Murder Mystery Game!", glassySkin);
        TextButton newGameButton = new TextButton("New Game", glassySkin);

        text.setFontScale(2, 2);

        text.setBounds(Gdx.graphics.getWidth() / 2 - text.getWidth(), Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 3 + Gdx.graphics.getHeight() / 16, text.getWidth(), text.getHeight());

        //Creating the buttons and setting their positions
        newGameButton.setPosition(WIDTH, (float) (Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)-120));
        TextButton settings = new TextButton("Settings", glassySkin );
        settings.setPosition(WIDTH+30, (Gdx.graphics.getHeight() / 2)-90);
        TextButton quit = new TextButton("Quit", glassySkin);
        quit.setPosition(WIDTH+32, (Gdx.graphics.getHeight() / 4)-60 );

        //Loading the buttons onto the stage
        stage.addActor(background);
        stage.addActor(text);
        stage.addActor(settings);
        stage.addActor(newGameButton);
        stage.addActor(quit);


        //Making the "New Game" button clickable and causing it to start the game
        newGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                gameSnapshot.setState(GameState.narrator);
            }
        });

        //Making the "Quit" button clickable and causing it to close the game
        quit.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });
        //Making the "Settings" button clickable and causing it to load the settings screen
        settings.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //Change to settings screen once its been made
            }
        });
    }

    public void makeVisible()
    {
        gameSnapshot.setState(GameState.menu);
    }

    /**
     * This method is called to render the main menu to the stage
     */
    public void render(float delta)
    {
        //Determining the background colour of the menu
        Gdx.gl.glClearColor(135, 206, 235, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Rendering the buttons
        stage.act();
        stage.draw();
    }

    @Override
    public void show()
    {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
    }

    /**
     * This method disposes of all elements
     */
    public void dispose()
    {
        //Called when disposing the main menu
        stage.dispose();
        batch.dispose();
    }

    /**
     * This method is called when the window is resized.
     *
     * @param width  - The new width
     * @param height - The new height
     */
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
    };
