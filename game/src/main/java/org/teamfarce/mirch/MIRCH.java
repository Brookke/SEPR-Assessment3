package org.teamfarce.mirch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MIRCH extends ApplicationAdapter{
	private Texture titleScreen;
	private SpriteBatch batch;
	private OrthographicCamera camera;


	@Override
	public void create() {
	      titleScreen = new Texture(Gdx.files.internal("assets/title.png"));
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 1366, 768);
	      batch = new SpriteBatch();
	}
	
	@Override
	public void render() {
	      Gdx.gl.glClearColor(1, 1, 1, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      camera.update();
	      batch.setProjectionMatrix(camera.combined);
	      batch.begin();
	      batch.draw(titleScreen, 0, 0);
	      batch.end();
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}