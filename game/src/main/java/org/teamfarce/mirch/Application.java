package org.teamfarce.mirch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Initiates the Java Program. Executable for program can be found at: https://teamfarce.github.io/MIRCH/executable/
 *
 * @author jacobwunwin
 */
class Application {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration(); //create a new libGDX objext
        config.title = "MIRCH"; //configure the title
        config.width = 1366; //configure the width and height
        config.height = 768;
        new LwjglApplication(new MIRCH(), config); //start the game
    }
}
