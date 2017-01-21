/**
 * 
 */
package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

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
}
