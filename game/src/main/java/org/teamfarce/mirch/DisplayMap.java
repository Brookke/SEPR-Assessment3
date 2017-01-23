package org.teamfarce.mirch;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Displays the map on the screen.
 */
public class DisplayMap {
    private SpriteBatch batch;

    /**
     * Initialises the map class.
     *
     * @param batch The sprite batch to utilise.
     */
    DisplayMap(SpriteBatch batch){
        this.batch = batch;
    }

    /**
     * Draws an array list of doors onto the screen in their correct location.
     *
     * @param items The items to draw.
     */
    private void drawRenderItems(List<RenderItem> items) {
        this.batch.begin();
        for (RenderItem item: items){
            item.sprite.draw(this.batch);
        }
        this.batch.end();
    }

    // This function should be removed at some point since it takes three parameters of the same
    // type and performs exactly the same operation on them.
    /**
     * Draws the map onto the screen.
     *
     * @param rooms The rooms to draw.
     * @param doors The doors to draw.
     * @param objects The objects to draw.
     * @param characters The characters to draw.
     */
    public void drawMap(
        List<RenderItem> rooms,
        List<RenderItem> doors,
        List<RenderItem> objects,
        List<RenderItem> characters
    ) {
        this.drawRenderItems(rooms);
        this.drawRenderItems(doors);
        this.drawRenderItems(objects);
        this.drawRenderItems(characters);
    }
}
