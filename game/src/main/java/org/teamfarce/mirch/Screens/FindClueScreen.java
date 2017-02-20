package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.*;
import org.teamfarce.mirch.entities.Clue;
import org.teamfarce.mirch.screens.elements.StatusBar;

/**
 * This class displays a clue with its information when it has just been found
 */
public class FindClueScreen extends AbstractScreen {

    private GameSnapshot snapshot;
    private Skin uiSkin;

    private Stage clueStage;

    private Clue displayingClue;
    private Image clueImage;
    private Image clueBox;

    private Label motiveLabel = null;
    private Image nameBackground;
    private Label name;

    private Image descBackground = null;
    private Label description = null;

    private Button continueButton = null;

    private Vector2Int goalPos = new Vector2Int(0, 0);
    private Vector2Int goalSize = new Vector2Int(15 * Settings.TILE_SIZE, 15 * Settings.TILE_SIZE);

    private boolean rotate = false;

    private float ANIM_TIME = 1f;
    private float soFarAnim = 0f;

    private StatusBar statusBar;

    /**
     * Initialiser
     *
     * @param game   - Reference to the main game class
     * @param uiSkin - The skin to render objects with
     */
    public FindClueScreen(MIRCH game, Skin uiSkin) {
        super(game);
        this.snapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;

        statusBar = new StatusBar(game.gameSnapshot, uiSkin);
    }

    /**
     * Initialise all the components on the screen
     */
    private void initScreen() {
        soFarAnim = 0f;
        ANIM_TIME = 1f;
        rotate = false;
        continueButton = null;
        clueStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image screenShot = MapScreen.recentFrame;
        screenShot.setHeight(Gdx.graphics.getHeight());
        screenShot.setWidth(Gdx.graphics.getWidth());

        goalSize = new Vector2Int(15 * Settings.TILE_SIZE, 15 * Settings.TILE_SIZE);
        goalPos = new Vector2Int((Gdx.graphics.getWidth() / 2) - (goalSize.getX() / 2), (Gdx.graphics.getHeight() / 2) - (goalSize.getY() / 2));

        clueBox = new Image(Assets.loadTexture("clues/clueBox.png"));
        clueBox.setSize(Settings.TILE_SIZE * 1.1f, Settings.TILE_SIZE * 1.1f);

        displayingClue = game.player.getClueFound();
        clueImage = new Image(new TextureRegion(displayingClue.getTexture(), displayingClue.getResourceX() * 128, displayingClue.getResourceY() * 128, 128, 128));

        clueImage.setSize(Settings.TILE_SIZE, Settings.TILE_SIZE);
        int[] res = new int[]{Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2};
        clueImage.setPosition(res[0], res[1]);
        clueBox.setPosition(res[0], res[1]);

        clueStage.addActor(screenShot);
        clueStage.addActor(clueBox);
        clueStage.addActor(clueImage);
    }

    /**
     * Show the window
     */
    @Override
    public void show() {
        MapScreen.grabScreenshot = false;

        initScreen();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(clueStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Render the screen
     *
     * @param delta - Time since last frame in seconds
     */
    @Override
    public void render(float delta) {
        if (soFarAnim < ANIM_TIME * 0.5f) {
            soFarAnim += delta;

            //This interpolation is linear but using it exponentially creates a really nice fading effect.

            float nextWidth = Interpolation.linear.apply(clueImage.getWidth(), goalSize.getX(), soFarAnim / ANIM_TIME);
            float nextHeight = Interpolation.linear.apply(clueImage.getHeight(), goalSize.getY(), soFarAnim / ANIM_TIME);

            float nextXPos = Interpolation.linear.apply(clueImage.getX(), goalPos.getX(), soFarAnim / ANIM_TIME);
            float nextYPos = Interpolation.linear.apply(clueImage.getY(), goalPos.getY(), soFarAnim / ANIM_TIME);

            clueBox.setSize(nextWidth, nextHeight);
            clueBox.setPosition(nextXPos, nextYPos);
            clueImage.setSize(nextWidth, nextHeight);
            clueImage.setPosition(nextXPos, nextYPos);

            if (rotate) {
                clueBox.rotateBy(15);
                clueImage.rotateBy(15);
            }

            if (soFarAnim >= ANIM_TIME * 0.5f) {
                if (continueButton != null) {
                    game.gameSnapshot.setState(GameState.map);

                    game.gameSnapshot.journal.addClue(displayingClue);
                } else {
                    addAllToStage();
                }
            }
        }

        clueStage.act();
        clueStage.draw();

        if (rotate) {
            statusBar.render();
        }
    }

    /**
     * This adds the buttons and clue descriptors to the screen. It shows them once the clue has moved to the centre of the screen
     */
    public void addAllToStage() {
        continueButton = new TextButton("Continue", uiSkin);
        continueButton.setSize(Gdx.graphics.getWidth() / 4, 50);
        continueButton.setPosition((Gdx.graphics.getWidth() / 2) - (continueButton.getWidth() / 2), clueBox.getY() - (2 * continueButton.getHeight()));
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAll();

                rotate = true;

                goalPos = new Vector2Int((int) (Gdx.graphics.getWidth() * 0.6), Gdx.graphics.getHeight());
                goalSize = new Vector2Int(0, 0);

                soFarAnim = 0f;
                ANIM_TIME = 2f;

                game.player.getRoom().removeClue(displayingClue);
                game.player.clearFound();
            }
        });

        if (displayingClue.getName().contains("Motive")) {
            motiveLabel = new Label(displayingClue.getDescription(), uiSkin);
            motiveLabel.setSize(clueImage.getWidth() * 0.6f, clueImage.getHeight());
            motiveLabel.setAlignment(Align.center);
            motiveLabel.setPosition((Gdx.graphics.getWidth() / 2) - (motiveLabel.getWidth() / 2), clueImage.getY());
            motiveLabel.setWrap(true);

            clueStage.addActor(motiveLabel);
        } else {
            Pixmap pixMap = new Pixmap((int) (clueBox.getWidth() / 2), (int) clueBox.getHeight(), Pixmap.Format.RGBA8888);
            pixMap.setColor(0, 0, 0, 0.9f);
            pixMap.fill();

            descBackground = new Image(new Texture(pixMap));

            float posX = clueBox.getX() + clueBox.getWidth() + (Gdx.graphics.getWidth() - clueBox.getWidth()) / 8;

            descBackground.setPosition(posX, clueBox.getY());
            clueStage.addActor(descBackground);

            description = new Label(displayingClue.getDescription(), uiSkin, "white");
            description.setSize(descBackground.getWidth() * 0.9f, descBackground.getHeight() * 0.9f);
            description.setAlignment(Align.top);
            description.setPosition(descBackground.getX() + descBackground.getWidth() * 0.05f, descBackground.getY() + descBackground.getHeight() * 0.05f);
            description.setWrap(true);

            clueStage.addActor(description);
        }

        Pixmap pixMap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10, Pixmap.Format.RGBA8888);
        pixMap.setColor(0, 0, 0, 0.9f);
        pixMap.fill();

        nameBackground = new Image(new Texture(pixMap));
        nameBackground.setPosition(0, Gdx.graphics.getHeight() * 0.88f);

        name = new Label(displayingClue.getName(), uiSkin, "white");
        name.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10);
        name.setAlignment(Align.center);
        name.setPosition(nameBackground.getX(), nameBackground.getY());
        name.setWrap(true);

        clueStage.addActor(nameBackground);
        clueStage.addActor(name);
        clueStage.addActor(continueButton);
    }

    /**
     * This method hides the UI elements of this screen.
     *
     * Used when doing the animation of the clue going into the journal
     */
    private void hideAll() {
        name.setVisible(false);
        nameBackground.setVisible(false);
        continueButton.setVisible(false);

        if (motiveLabel != null) motiveLabel.setVisible(false);

        if (descBackground != null) descBackground.setVisible(false);

        if (description != null) description.setVisible(false);
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
