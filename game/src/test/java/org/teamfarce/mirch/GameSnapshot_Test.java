package org.teamfarce.mirch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;


/**
 * 
 * @author jacobwunwin
 *
 */
public class GameSnapshot_Test {
	
	@Test
	public void test_getTime(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		assertSame(gameSnapshot.getTime(), gameSnapshot.time);
	}
	
	@Test
	public void test_incrementTime(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		int time = gameSnapshot.getTime();
		time++;
		
		gameSnapshot.incrementTime();
		assertSame(time, gameSnapshot.getTime());
	}
	
	@Test
	public void test_getRooms(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		assertSame(gameSnapshot.getRooms(), gameSnapshot.rooms);
	}
	
	@Test
	public void test_getProps(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		
		assertSame(gameSnapshot.getProps(), gameSnapshot.props);
	}
	
	@Test
	public void test_proveMeans(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		
		int addedMeans = 10;
		Clue clue = new Clue(0, addedMeans, "Clue description");
		ArrayList<Clue> clues = new ArrayList<Clue>();
		clues.add(clue);
		
		int means = gameSnapshot.meansProven;
		
		gameSnapshot.proveMeans(clues);
		
		assertSame(means + addedMeans, gameSnapshot.meansProven);
	}
	
	@Test
	public void test_proveMotive(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);

		int addedMotive = 10;
		Clue clue = new Clue(addedMotive, 0, "Clue description");
		ArrayList<Clue> clues = new ArrayList<Clue>();
		clues.add(clue);

		int motive = gameSnapshot.motiveProven;

		gameSnapshot.proveMotive(clues);

		assertSame(motive + addedMotive, gameSnapshot.motiveProven);
	}
	
	@Test
	public void test_isMeansProven(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		
		gameSnapshot.meansProven = 99; //test beneath the boundary
		assertFalse(gameSnapshot.isMeansProven());
		
		gameSnapshot.meansProven = 100; //test on the boundary
		assertTrue(gameSnapshot.isMeansProven());
		
		gameSnapshot.meansProven = 101; //test above the boundary
		assertTrue(gameSnapshot.isMeansProven());
	}
	
	@Test
	public void test_isMotiveProven(){
		GameSnapshot gameSnapshot = new GameSnapshot(null, null, null);
		
		gameSnapshot.motiveProven = 99; //test beneath the boundary
		assertFalse(gameSnapshot.isMotiveProven());
		
		gameSnapshot.motiveProven = 100; //test on the boundary
		assertTrue(gameSnapshot.isMotiveProven());
		
		gameSnapshot.motiveProven = 101; //test above the boundary
		assertTrue(gameSnapshot.isMotiveProven());
	}
	
}
