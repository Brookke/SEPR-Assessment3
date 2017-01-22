package org.teamfarce.mirch;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class to represent different map entities.
 *
 * @author Jacob W Unwin
 */
public class MapEntity {
    protected int id;
    protected int resourceIndex;
    protected Vector2 roomPosition;
    protected Room currentRoom;
    protected String name;
    protected String description;
    protected String filename;

    /**
     * Initialise class
     * @param id integer for identification
     * @param resourceIndex index for resources like sprites
     * @param dialogue
     * @param roomPosition stores position relative to currentroom
     * @param currentRoom a ref to the current room the suspect is in
     * @param name a string for the name
     * @param description a string description 
     */
    public MapEntity(
    	int id,
    	int resourceIndex,
    	String name,
    	String description,
    	String filename
    ){
        this.id = id;
        this.resourceIndex = resourceIndex;
        this.name = name;
        this.description = description;
        this.filename = filename;
    }
    
    /**
     * Allows the setting of the rooms position.
     * @param pos The provided position
     */
    public void setPosition(Vector2 pos) {
        this.roomPosition = pos;
    }
    
    
    /**
     * returns the id as an integer
     * @return id
     */
    public int getId(){
    	return this.id;
    }
    
    /**
     * returns the resourceindex as an integer
     * @return resourceIndex
     */
    public int getResourceIndex(){
    	return this.resourceIndex;
    }
    
    /**
     * Returns position of room as a Vector2.
     * @return roomPosition
     */
    public Vector2 getPosition() {
        return this.roomPosition;
    }
    
    /**
     * returns the current room the entity is within
     *@return currentRoom
     */
     public Room getRoom(){
    	 return this.currentRoom;
     }
     
     /**
      * 
      * @param room
      */
     public void setRoom(Room room){
    	 this.currentRoom = room;
     }
     
     /**
      * returns the name
      * @return name 
      */
     public String getName(){
    	 return this.name;
     }
     
     /**
      * returns the description string field
      * return description
      */
    public String getDescription(){
    	return this.description;
    }

}
