package org.teamfarce.mirch;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MIRCH extends ApplicationAdapter{
	private Texture titleScreen;
	private SpriteBatch batch;
	
	private ArrayList<Sprite> rooms;
	private ArrayList<Sprite> objects;
	private ArrayList<Sprite> characters;
	
	private float move = 5;

	
	private Sprite player;
	
	private OrthographicCamera camera;
	
	private boolean isObjectPressed(Sprite theSprite, Vector3 mouse){
		return false;
	}
	
	private void drawSprites(){
		
	}
	
	private void drawObjects(){
		
	}
	
	private void drawRooms(){
		
	}
	
	private void drawPlayer(){
		
	}
	
	private void drawMapControls(){
		
	}
	
	private void drawCharacterSelection(){
		
	}
	
	private void drawMap(){
		drawRooms();
		drawObjects();
		drawSprites();
		drawPlayer();
		drawMapControls();
	}
	
	private void drawNotebook(){
		
	}
	
	private void drawCharacterDialogue(){
		
	}
	
	private void drawItemDialogue(){
		
	}
	
	

	@Override
	public void create() {
	      titleScreen = new Texture(Gdx.files.internal("assets/Detective_sprite.png"));
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 1366, 768);
	      
	      batch = new SpriteBatch();
	      
	      player = new Sprite(titleScreen);
	      player.setPosition(100, 100);

	      
	      rooms = new ArrayList<Sprite>();
	      characters = new ArrayList<Sprite>();
	      objects = new ArrayList<Sprite>();
	      
	}
	
	@Override
	public void render() {
	      Gdx.gl.glClearColor(1, 1, 1, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      camera.update();
	      batch.setProjectionMatrix(camera.combined);
	      	      
	      
	      //draw output
	      batch.begin();
	      player.draw(batch);
	      batch.end();
	      
	      // process keyboard touch   	  
	      if(Gdx.input.isKeyPressed(Input.Keys.W)){
	    	  player.translate(0, move);
	      } 
	      if (Gdx.input.isKeyPressed(Input.Keys.S)){
	    	  player.translate(0, -move);
	      } 
	      if (Gdx.input.isKeyPressed(Input.Keys.A)){
	    	  player.translate(-move,  0);
	      } 
	      if (Gdx.input.isKeyPressed(Input.Keys.D)){
	    	  player.translate(move, 0);
	      }

	      
	      
	      // process user touch input
	      if (Gdx.input.isTouched()) {
	         Vector3 touchPos = new Vector3();
	         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	         camera.unproject(touchPos);
	         
	         boolean clicked = false;
	         int length = rooms.size();
	         int i = 0;
	         while (!clicked && (i < length)){
	        	 clicked = isObjectPressed(rooms.get(i), touchPos);
	        	 i++;
	        	 if (clicked){
	        		 //handle touch input for room
	        	 }
	         }
	         
	         length = objects.size();
	         i = 0;
	         while (!clicked && (i < length)){
	        	 clicked = isObjectPressed(objects.get(i), touchPos);
	        	 i++;
	        	 if (clicked){
	        		 //handle touch input for objects
	        	 }
	         }
	         
	         length = characters.size();
	         i = 0;
	         while (!clicked && (i < length)){
	        	 clicked = isObjectPressed(characters.get(i), touchPos);
	        	 i++;
	        	 if (clicked){
	        		 //handle touch input for character
	        	 }
	         }
	         
	      }
		
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}