/**
 * 
 */
package org.teamfarce.mirch;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * @author jacobwunwin
 *
 */
public class InputController {
	
	InputController(){
		
	}
	
	//returns a new movement vector for the player
	Vector2 fetchPlayerPositionUpdate(){
		// process keyboard touch and translate the player  	
	
		Vector2 move = new Vector2(0, 0);

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			move.y += 1;
		} 
		if (Gdx.input.isKeyPressed(Input.Keys.S)){
			move.y -= 1;	  
		} 
		if (Gdx.input.isKeyPressed(Input.Keys.A)){
			move.x -= 1;
		} 
		if (Gdx.input.isKeyPressed(Input.Keys.D)){
			move.x += 1;
		}
		
		return move;
	}
	
	void fetchMouseInput(){
		
	}
	
	/**
	 * Detects whether a Sprite has been clicked by the mouse. Returns true if this is the case.
	 * @param theSprite
	 * @param mouse
	 * @return
	 */
	private boolean isSpritePressed(Sprite theSprite, Vector3 mouse){
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
	
	boolean isObjectClicked(ArrayList<RenderItem> object, Camera camera){
		// process user touch input
		if (Gdx.input.isTouched()) {

			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			for (int i = 0; i < object.size(); i++){
				if (this.isSpritePressed(object.get(i).sprite, touchPos)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	RenderItem getClickedObject(ArrayList<RenderItem> object, Camera camera){
		// process user touch input
		if (Gdx.input.isTouched()) {

			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			for (int i = 0; i < object.size(); i++){
				if (this.isSpritePressed(object.get(i).sprite, touchPos)){
					return object.get(i);
				}
			}
		}
		
		return null;
	}
}
