/**
 * 
 */
package org.teamfarce.mirch;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * The RenderItem class is used by the front end to link a front-end sprite with its back end Object
 * @author jacobwunwin
 *
 */
public class RenderItem {
	public Sprite sprite;
	public Object object;
	
	/**
	 * Initialise the renderItem
	 * @param sprite
	 * @param object
	 */
	public RenderItem(Sprite sprite, Object object){
		this.sprite = sprite;
		this.object = object;
	}
}
