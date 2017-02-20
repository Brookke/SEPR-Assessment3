package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;

/**
 * This is the screen which displays the narrator and a defined speech
 *
 * To show the screen with the defined speech you do the following
 *
 * Have access to the game object (MIRCH). then do
 *
 * game.guiController.narratorScreen.setSpeech("");
 */
public class NarratorScreen extends AbstractScreen {

    /**
     * This is how many render frames that have to occur before the next letter is added to the currentMessage
     */
    final static int FRAMES_PER_LETTER = 1;
    /**
     * These are the variables used to show and draw the scene
     */
    public Stage narratorStage;

    private Skin uiSkin;
    /**
     * This is the current string that is being displayed on the screen.
     */
    private String currentMessage = "";
    /**
     * This is the full message that is to be displayed. Will gradually be added to the above variable
     */
    private String endMessage = "";
    /**
     * These are the 2 objects that are rendered onto the screen
     */
    private Label speech;
    private Button prompt;
    /**
     * The current amount of render frames since the last added letter. Reset to 0 once FRAMES_PER_LETTER is exceeded
     */
    private int currentFrames = 0;

    /**
     * This is the main constructor for the screen
     *
     * @param game   - Reference to the Game instance
     * @param uiSkin - The game snapshot reference
     */
    public NarratorScreen(MIRCH game, Skin uiSkin) {
        super(game);

        this.uiSkin = uiSkin;

        String introSpeech = "You have been invited to a lock-in costume party with some of the richest people around. There has been a murder, one of the guests has killed " + game.gameSnapshot.victim.getName() + "\n\n" +
                "The murderer instantly regretted their decision and has tried their hardest to cover up their tracks. All the clues have been hidden around the Ron Cooke Hub by the murderer so that they can avoid being discovered.\n\n" +
                "NOT SO FAST! You're not the only detective that got called to the scene, there will be other detectives trying to solve the case at the same time.\n\n" +
                "You must go around each room trying to find the clues that have been hidden. You must also question the guests to see if they know anything about the murder! Try to solve the case before any other detective!";

        //Set introduction speech
        setSpeech(introSpeech);
        setButton("Start Game", new Runnable() {
            @Override
            public void run() {
                game.gameSnapshot.setState(GameState.map);
            }
        });
    }

    /**
     * This method initiates the objects to the stage
     */
    private void initStage() {
        narratorStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image background = new Image(new TextureRegion(Assets.loadTexture("narratorBackground.png")));
        background.setHeight(Gdx.graphics.getHeight());
        background.setWidth(Gdx.graphics.getWidth());

        speech = new Label(currentMessage, uiSkin, "white");
        speech.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.6f);
        speech.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 0.25f);
        speech.setWrap(true);

        narratorStage.addActor(background);
        narratorStage.addActor(speech);
        narratorStage.addActor(prompt);
    }

    /**
     * This method is called once a render loop. It adds to the current message if it meets the necessary
     * requirements
     */
    public void updateSpeech() {
        if (endMessage.equals(currentMessage)) {
            prompt.setVisible(true);
            return;
        }

        currentMessage = currentMessage + endMessage.charAt(currentMessage.length());

        try {
            speech.setText(currentMessage);
        } catch (Exception e) {
        }
    }

    @Override
    public void show() {
        initStage();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(narratorStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        currentFrames++;

        if (currentFrames >= FRAMES_PER_LETTER) {
            currentFrames = 0;
            updateSpeech();
        }

        narratorStage.act();
        narratorStage.draw();
    }

    /**
     * This method returns the who speech that is to be shown on the narrator screen
     *
     * @return String - `endMessage`
     */
    public String getSpeech() {
        return endMessage;
    }

    /**
     * This method sets the endMessage to the defined string
     *
     * @param speech - The message to be displayed to the screen
     * @return this
     */
    public NarratorScreen setSpeech(String speech) {
        endMessage = speech;
        currentMessage = "";

        return this;
    }

    /**
     * This method sets the buttons text and the clickable action so that this screen
     * can be used for more than one situation
     *
     * @return this
     */
    public NarratorScreen setButton(String text, Runnable runnable) {
        try {
            prompt = new TextButton(text, uiSkin);
            prompt.setSize(Gdx.graphics.getWidth() / 3, 50);
            prompt.setPosition(Gdx.graphics.getWidth() * 0.45f, Gdx.graphics.getHeight() * 0.25f);
            prompt.setVisible(false);
            prompt.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    runnable.run();
                }
            });
        } catch (Exception e) {
        }

        return this;
    }

    /**
     * This method returns the String that is currently being shown on the Narrator screen
     *
     * @return String - `currentMessage`
     */
    public String getCurrentSpeech() {
        return currentMessage;
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
