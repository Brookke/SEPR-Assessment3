package org.teamfarce.mirch;

import java.util.ArrayList;

import javax.swing.JOptionPane;

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
	
	private ArrayList<RenderItem> rooms;
	private ArrayList<RenderItem> objects;
	private ArrayList<RenderItem> characters;
	
	private float move = 5;

	
	private Sprite player;
	
	private OrthographicCamera camera;
	
	private boolean isObjectPressed(Sprite theSprite, Vector3 mouse){
		boolean toReturn = false;
		
		float x1 = theSprite.getX();
		float y1 = theSprite.getY();
		float x2 = x1 + theSprite.getWidth();
		float y2 = y1 + theSprite.getHeight();
		
		if ((mouse.x > x1) && (mouse.x < x2)){
			if ((mouse.y > y1) && (mouse.y < y2)){
				toReturn = true;
			}
		}
		
		return toReturn;
	}
	
	private void drawCharacters(ArrayList<RenderItem> characters, SpriteBatch batch){
		batch.begin();
		for (RenderItem character : characters){
			character.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawObjects(ArrayList<RenderItem> objects, SpriteBatch batch){
		batch.begin();
		for (RenderItem object : objects){
			object.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawRooms(ArrayList<RenderItem> rooms, SpriteBatch batch){
		batch.begin();
		for (RenderItem room : rooms){
			room.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawMapControls(){
		
	}
	
	private void drawCharacterSelection(){
		
	}
	
	private void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters, SpriteBatch batch){
		drawRooms(rooms, batch);
		drawObjects(objects, batch);
		drawCharacters(characters, batch);
		drawMapControls();
	}
	
	private void drawNotebook(){
		
	}
	
	private void drawCharacterDialogue(){
		
	}
	
	private void drawItemDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. It has been added to your Journal.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	

	@Override
	public void create() {
	      titleScreen = new Texture(Gdx.files.internal("assets/Detective_sprite.png"));
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 1366, 768);
	      
	      batch = new SpriteBatch();
	      
	      player = new Sprite(titleScreen);
	      player.setPosition(100, 100);

	      
	      
	      objects = new ArrayList<RenderItem>();
	      
	      //create an example prop
	      Prop prop = new Prop();
	      prop.description = "A bloody axe...";
	      
	      Texture texture = new Texture(Gdx.files.internal("assets/objects/Axe.png"));
	      objects.add(new RenderItem(new Sprite(texture, 300, 300), prop));
	    		  
	      rooms = new ArrayList<RenderItem>();
	      
	      
	      characters = new ArrayList<RenderItem>();
	      
	}
	
	@Override
	public void render() {
	      Gdx.gl.glClearColor(1, 1, 1, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      camera.update();
	      batch.setProjectionMatrix(camera.combined);
	      	      
	      
	      //draw output
	      
	      drawMap(rooms, objects, characters, batch);
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
	        	 clicked = isObjectPressed(rooms.get(i).sprite, touchPos);
	        	 if (clicked){
	        		 //handle touch input for room
	        	 }
	        	 i++;
	         }
	         
	         length = objects.size();
	         i = 0;
	         System.out.println(length);
	         while (!clicked && (i < length)){
	        	 clicked = isObjectPressed(objects.get(i).sprite, touchPos);
	        	 
	        	 if (clicked){
	        		 //handle touch input for objects
	        		 System.out.println("Object clocked!");
	        		 drawItemDialogue((Prop) objects.get(i).object);
	        	 }
	        	 i++;
	         }
	         
	         length = characters.size();
	         i = 0;
	         while (!clicked && (i < length)){
	        	 clicked = isObjectPressed(characters.get(i).sprite, touchPos);
	        	 
	        	 if (clicked){
	        		 //handle touch input for character
	        	 }
	        	 i++;
	         }
	         
	      }
		
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}