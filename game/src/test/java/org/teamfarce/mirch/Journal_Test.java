/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

/**
 * Tests the journal class
 * @author jacobwunwin
 *
 */
public class Journal_Test {
	
	@Test
	public void test_addProp(){
		Journal journal = new Journal();
		
		Prop prop = new Prop("myProp", null, new Vector2());
		journal.addProp(prop);
		
		assertEquals(prop, journal.foundProps.get(0));
	}
	
	@Test
	public void test_addClue(){
		Journal journal = new Journal();
		
		Clue clue = new Clue(10, 15, "myClue");
		
		journal.addClue(clue);
		
		assertEquals(clue, journal.foundClues.get(0));
		
	}
	
	@Test
	public void test_getProps(){
		Journal journal = new Journal();
		
		Prop prop = new Prop("myProp", null, new Vector2());
		journal.addProp(prop);
		
		assertEquals(prop, journal.foundProps.get(0));
	}
}
