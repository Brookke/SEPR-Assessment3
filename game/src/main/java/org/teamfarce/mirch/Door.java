package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Door {
	float startX;
	float startY;
	float endX;
	float endY;
	Sprite sprite;
	Door (float startX, float startY, float endX, float endY, String texture){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.sprite = new Sprite(new Texture(Gdx.files.internal("assets/" + texture)));
		this.sprite.setX(startX);
		this.sprite.setY(startY);
		this.sprite.setRegion(startX, startY, endX - startX, endY - startY);
	}
}
