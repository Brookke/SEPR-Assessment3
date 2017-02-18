package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.*;
import org.teamfarce.mirch.Entities.Clue;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.Set;

/**
 * Created by joeshuff on 18/02/2017.
 */
public class FindClueScreen extends AbstractScreen {

    private GameSnapshot snapshot;
    private Skin uiSkin;

    private Stage clueStage;

    private Clue displayingClue;
    private Image clueImage;
    private Image clueBox;

    private Vector2Int goalPos = new Vector2Int(0, 0);
    private Vector2Int goalSize = new Vector2Int(15 * Settings.TILE_SIZE, 15 * Settings.TILE_SIZE);

    private float ANIM_TIME = 1f;
    private float soFarAnim = 0f;

    public FindClueScreen(MIRCH game, Skin uiSkin)
    {
        super(game);
        this.snapshot = game.gameSnapshot;
        this.uiSkin = uiSkin;
    }

    private void initScreen()
    {
        soFarAnim = 0f;
        clueStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Image screenShot = MapScreen.recentFrame;
        screenShot.setHeight(Gdx.graphics.getHeight());
        screenShot.setWidth(Gdx.graphics.getWidth());

        goalPos = new Vector2Int((Gdx.graphics.getWidth() / 2) - (goalSize.getX() / 2), (Gdx.graphics.getHeight() / 2) - (goalSize.getY() / 2));

        clueBox = new Image(Assets.loadTexture("clues/clueBox.png"));
        clueBox.setSize(Settings.TILE_SIZE  * 1.1f, Settings.TILE_SIZE * 1.1f);

        displayingClue = game.player.getClueFound();
        clueImage = new Image(new TextureRegion(displayingClue.getTexture(), displayingClue.getResourceX() * 128, displayingClue.getResourceY() * 128, 128, 128));

        clueImage.setSize(Settings.TILE_SIZE, Settings.TILE_SIZE);
        int[] res = tileToScreenPos(displayingClue.getTileX(), displayingClue.getTileY());
        clueImage.setPosition(res[0], res[1]);
        clueBox.setPosition(res[0], res[1]);

        clueStage.addActor(screenShot);
        clueStage.addActor(clueBox);
        clueStage.addActor(clueImage);
    }

    public int[] tileToScreenPos(int tileX, int tileY)
    {
        Vector3 res = game.orthoCam.project(new Vector3(tileX * 32, tileY * 32, 0));
        return new int[]{(int) res.x, (int) res.y};
    }

    @Override
    public void show() {
        MapScreen.grabScreenshot = false;

        initScreen();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(clueStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta)
    {
        if (soFarAnim < ANIM_TIME)
        {
            soFarAnim += delta;

            float nextWidth = Interpolation.linear.apply(clueImage.getWidth(), goalSize.getX(), soFarAnim / ANIM_TIME);
            float nextHeight = Interpolation.linear.apply(clueImage.getHeight(), goalSize.getY(), soFarAnim / ANIM_TIME);

            float nextXPos = Interpolation.linear.apply(clueImage.getX(), goalPos.getX(), soFarAnim / ANIM_TIME);
            float nextYPos = Interpolation.linear.apply(clueImage.getY(), goalPos.getY(), soFarAnim / ANIM_TIME);

            clueBox.setSize(nextWidth * 1.1f, nextHeight * 1.1f);
            clueBox.setPosition(nextXPos * 0.95f, nextYPos * 0.95f);
            clueImage.setSize(nextWidth, nextHeight);
            clueImage.setPosition(nextXPos, nextYPos);

            if (soFarAnim >= ANIM_TIME)
            {
                TextButton button = new TextButton("Continue", uiSkin);
                button.setSize(Gdx.graphics.getWidth() / 4, 50);
                button.setPosition((Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2), clueBox.getY() - (2 * button.getHeight()));
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.gameSnapshot.setState(GameState.map);

                        /**
                         * Add clue to some list for the journal.
                         * Note down if we found the means clue/ all motive clue
                         */

                        game.player.getRoom().removeClue(displayingClue);
                        game.player.clearFound();
                    }
                });
                clueStage.addActor(button);
            }
        }

        clueStage.act();
        clueStage.draw();
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
