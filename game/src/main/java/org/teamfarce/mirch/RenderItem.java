/**
 * 
 */
package org.teamfarce.mirch;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author jacobwunwin
 *
 */
public class RenderItem {
	public Sprite sprite;
	public Object object;
	
	public RenderItem(Sprite sprite, Prop prop) {
		// TODO Auto-generated constructor stub
		this.sprite = sprite;
		this.object = prop;
	}

	public RenderItem(Sprite sprite, Object object){
		this.sprite = sprite;
		this.object = object;
	}
}
