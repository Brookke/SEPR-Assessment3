package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class manages all the assets.
 */
public class Assets {
    /**
     * These variables store the textures for the corresponding arrow directions
     */
    public static TextureRegion UP_ARROW;
    public static TextureRegion DOWN_ARROW;
    public static TextureRegion LEFT_ARROW;
    public static TextureRegion RIGHT_ARROW;

    /**
     * This it the animation for the clue glint to be drawn where a clue is hidden
     */
    public static Animation CLUE_GLINT;

    /**
     * @param file - The file that contains the textures.
     * @return Returns the new texture.
     */
    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * This loads all of the necessary textures
     */
    public static void load() {
        Texture arrows = loadTexture("arrows.png");
        LEFT_ARROW = new TextureRegion(arrows, 0, 0, 32, 32);
        RIGHT_ARROW = new TextureRegion(arrows, 32, 0, 32, 32);
        DOWN_ARROW = new TextureRegion(arrows, 0, 32, 32, 32);
        UP_ARROW = new TextureRegion(arrows, 32, 32, 32, 32);

        Texture glintFile = loadTexture("clues/glintSheet.png");
        TextureRegion[][] splitFrames = TextureRegion.split(glintFile, 32, 32);
        TextureRegion[] frames = splitFrames[0];

        CLUE_GLINT = new Animation(0.1f, frames);
    }

    /**
     * This method takes a direction and returns the corresponding arrow asset for that direction
     *
     * @param direction - The direction to fetch
     * @return (TextureRegion) the corresponding TextureRegion
     */
    public static TextureRegion getArrowDirection(String direction) {
        if (direction.equals("NORTH")) {
            return UP_ARROW;
        } else if (direction.equals("SOUTH")) {
            return DOWN_ARROW;
        } else if (direction.equals("WEST")) {
            return LEFT_ARROW;
        } else if (direction.equals("EAST")) {
            return RIGHT_ARROW;
        }

        return null;
    }

}
