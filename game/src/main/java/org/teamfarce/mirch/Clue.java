package org.teamfarce.mirch;

/**
 * Stores information about a single clue.
 *
 * @author Jacob Wunwin
 */
public class Clue {
	//score values until you can accuse someone
    public int provesMotive;
    public int provesMean;
    public String name;

    /**
     * Initialiser function.
     */
    Clue(int motiveScore, int meansScore, String name) {
    	this.provesMotive = motiveScore;
    	this.provesMean = meansScore;
    	this.name = name;
    }
}
