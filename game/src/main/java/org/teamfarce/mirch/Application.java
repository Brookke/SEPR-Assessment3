package org.teamfarce.mirch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;

class Application {
    public static void main(String[] args) {
    	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "MIRCH";
        config.width = 1366;
        config.height = 768;
        new LwjglApplication(new MIRCH(), config);
    }
}
