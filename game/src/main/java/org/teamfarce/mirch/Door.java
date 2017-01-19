package org.teamfarce.mirch;



public class Door {
	float startX;
	float startY;
	float endX;
	float endY;
	float xOffset; //the x offset from the desired wall location
	float yOffset; //the y offset from the desired wall location
	Door (float startX, float startY, float endX, float endY){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;

	}
}
