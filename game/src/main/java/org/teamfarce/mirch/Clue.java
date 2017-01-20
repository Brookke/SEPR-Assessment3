package org.teamfarce.mirch;

/**
 * Stores information about a single clue.
 *
 * @author Jacob Wunwin
 */
public class Clue {
	//score values until you can accuse someone
    int provesMotive;
    int provesMean;
    String name;

    /**
     * Initialiser function.
     */
    Clue(int motiveScore, int meansScore, String name) {
    	this.provesMotive = motiveScore;
    	this.provesMean = meansScore;
    	this.name = name;
    }
}
