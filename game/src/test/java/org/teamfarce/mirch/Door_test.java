/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;


/**
 * Tests the door class
 * @author jacobwunwin
 *
 */
public class Door_test {
	
	@Test
	public void test_init(){
		float startX = 0;
		float endX = 1;
		float startY = 2;
		float endY = 3;
		
		Door door = new Door(startX, startY, endX, endY);
		assertSame((int) startX, (int) door.startX);
		assertSame((int) endX, (int) door.endX);
		assertSame((int) startY, (int) door.startY);
		assertSame((int) endY, (int) door.endY);
	}
}
