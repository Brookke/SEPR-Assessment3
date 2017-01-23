package org.teamfarce.mirch;


/**
 * The Door Class stores the location of a single door on the map. A door is a rectangle which is drawn on the map
 * to allow unhindered transfer between rooms.
 * @author jacobwunwin
 *
 */
public class Door {
	public float startX;
	public float startY;
	public float endX;
	public float endY;
	
	enum Face {
		horizontal, vertical;
	}
	
	Face face;
	
	/**
	 * Initialise the door by setting the location of its bottom left corner and top right corner.
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	Door (float startX, float startY, float endX, float endY, Face face){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.face = face;	
	}
	
}
