package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Screen;
import org.teamfarce.mirch.MIRCH;

/**
 * Created by brookehatton on 31/01/2017.
 */
public abstract class AbstractScreen implements Screen {
    protected final MIRCH game;

    public AbstractScreen(MIRCH game) {
        this.game = game;
    }

    @Override
    public abstract void show();

    @Override
    public abstract void render(float delta);

    @Override
    public abstract void resize(int width, int height);

    @Override
    public abstract void pause();


    @Override
    public abstract void resume();

    @Override
    public abstract void hide();

    @Override
    public abstract void dispose();

}
