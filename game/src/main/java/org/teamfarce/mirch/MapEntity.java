package org.teamfarce.mirch;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author jacobwunwin
 * Base class to represent different map entities.
 */
public class MapEntity {
	int id;
	Vector2 roomPosition;
	Room currentRoom;
	String name;
	String description;
	
	/**
	 * Initialiser class
	 */
	MapEntity(){
		
	}
	
	/**
	 * Returns position of room as a Vector2
	 * @return
	 */
	Vector2 getPosition(){
		return this.roomPosition;
	}
	
	/**
	 * Allows the setting of the rooms position - parameter is a Vector2 Position
	 * @param pos
	 */
	void setPosition(Vector2 pos){
		this.roomPosition = pos;
	}
	
}
