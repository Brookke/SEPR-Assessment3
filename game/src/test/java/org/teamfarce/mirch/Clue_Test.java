/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author jacobwunwin
 *
 */
public class Clue_Test {
	
	@Test
	public void test_init(){
		int provesMotive = 100;
		int provesMean = 50;
		String name = "test name";
		
		Clue clue = new Clue(provesMotive, provesMean, name);
		
		assertSame(provesMotive, clue.provesMotive);
		assertSame(provesMean, clue.provesMean);
		assertSame(name, clue.name);
	}
}
