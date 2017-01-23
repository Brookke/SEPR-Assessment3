package org.teamfarce.mirch;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The display controller controls the drawing of all items/objects/GUI elements to the display.
 * <p>
 * It also controls the handling of GUI element inputs
 * </p>
 */
public class DisplayController {
    private DisplayMap displayMap;
    private GUIController guiController;
    private Skin uiSkin;

    /**
     * Initialises the DisplayController.
     *
     * @param skin The skin to use for the ui.
     * @param gSnapshot The game snapshot to use.
     * @param batch The spitebatch to pass to the gui controller.
     */
    DisplayController(Skin skin, GameSnapshot gSnapshot, SpriteBatch batch) {
        this.displayMap = new DisplayMap(batch);
        this.uiSkin = skin;
        this.guiController = new GUIController(uiSkin, gSnapshot, batch);
    }

    

    /**
     * Creates a pop up window with text announcing that the user has found the referenced prop.
     *
     * @param prop The prop to draw the dialogue for.
     */
    public void drawItemDialogue(Prop prop) {
        String output;
        output = String.format(
            "You find: \"%1$s\". It has been added to your journal.", prop.description
        );
        JOptionPane.showMessageDialog(null, output, prop.name, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates a pop up window with text announcing that the user has already found the referenced
     * prop.
     *
     * @param prop The prop to create a window.
     */
    public void drawItemAlreadyFoundDialogue(Prop prop) {
        String output;
        output = String.format(
            "You find: \"%1$s\". You've already found the item.", prop.description
        );
        JOptionPane.showMessageDialog(null, output, prop.name, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Draws the map to the screen.
     *
     * @param rooms The rooms to draw.
     * @param doors The doors to draw.
     * @param objects The objects to draw.
     * @param characters The characters to draw.
     */
    void drawMap(
        ArrayList<RenderItem> rooms,
        ArrayList<RenderItem> doors,
        ArrayList<RenderItem> objects,
        ArrayList<RenderItem> characters
    ){
        this.displayMap.drawMap(rooms, doors, objects, characters);
        this.guiController.drawControlStage();
        Gdx.input.setInputProcessor(this.guiController.controlStage);
    }

    /**
     * Returns the drawGUI object.
     *
     * @return The internal gui controller.
     */
    GUIController drawGUI(){
        return this.guiController;
    }
}
