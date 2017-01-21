/**
 * 
 */
package org.teamfarce.mirch;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author jacobwunwin
 *
 */
public class DisplayMap {
	private SpriteBatch batch;

	DisplayMap(SpriteBatch batch){
		this.batch = batch;
	}
	
	/**
	 * Draws an array list of characters (suspects) onto the screen in their correct locations
	 * @param characters
	 * @param this.batch
	 */
	private void drawCharacters(ArrayList<RenderItem> characters){
		this.batch.begin();
		for (RenderItem character : characters){
			character.sprite.draw(this.batch);
		}
		this.batch.end();
	}
	
	/**
	 * Draws an array list of props onto the screen in their correct locations
	 * @param objects
	 * @param this.batch
	 */
	private void drawObjects(ArrayList<RenderItem> objects){
		this.batch.begin();
		for (RenderItem object : objects){
			object.sprite.draw(this.batch);
		}
		this.batch.end();
	}
	
	/**
	 * Draws an array list of rooms onto the screen in their correct locations
	 * @param rooms
	 * @param this.batch
	 */
	private void drawRooms(ArrayList<RenderItem> rooms){
		this.batch.begin();
		for (RenderItem room : rooms){
			room.sprite.draw(this.batch);
		}
		this.batch.end();
	}
	
	/**
	 * Draws an array list of doors onto the screen in their correct location
	 * @param doors
	 * @param this.batch
	 */
	private void drawDoors(ArrayList<RenderItem> doors){
		this.batch.begin();
		for (RenderItem door : doors){
			door.sprite.draw(this.batch);
		}
		this.batch.end();
	}
	
	/**
	 * Draws the map onto the screen.
	 * @param rooms
	 * @param doors
	 * @param objects
	 * @param characters
	 * @param this.batch
	 */
	public void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> doors, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters){
		this.drawRooms(rooms);
		this.drawDoors(doors);
		this.drawObjects(objects);
		this.drawCharacters(characters);
	}

}
