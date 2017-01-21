/**
 * 
 */
package org.teamfarce.mirch;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author jacobwunwin
 *
 */
public class DisplayController {
	private SpriteBatch batch;
	private DisplayMap displayMap;
	private GUIController guiController;
	private Skin uiSkin;
	private GameSnapshot gameSnapshot;
	
	
	/**
	 * Controls the initial character traits selection at the start of the game.
	 * Selection is made through a series of pop up windows.
	 * Returns an aray of integers containing the trait selections.
	 * @return
	 */
	private int[] drawCharacterSelection(){
		int[] values  = new int[3];
		String backstory = "Intro";
		JOptionPane.showMessageDialog(null,backstory, "Character Creation",JOptionPane.PLAIN_MESSAGE);
		Object[] options1 = {"Aggresive",
                "Neutral",
                "Passive"};
		Object[] options2 = {"Ruthless",
                "Neutral",
                "Kind"};
		Object[] options3 = {"Perceptive",
                "Crazy",
                "Evil"};
		values[0] = JOptionPane.showOptionDialog(null,
						"How agressive is your character?",
						"Character Creation",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options1,
						options1[2]);
		values[1] = JOptionPane.showOptionDialog(null,
						"How compassionate is your character?",
						"Character Creation",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options2,
						options2[2]);
		values[2] = JOptionPane.showOptionDialog(null,
						"Choose a defining trait for your character",
						"Character Creation",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options3,
						options3[2]);
		return values;
	}
	


	/**
	 * Creates a pop up window with text announcing that the user has found the referenced prop
	 * @param prop
	 */
	public void drawItemDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. It has been added to your Journal.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Creates a pop up window with text announcing that the user has already found the referenced prop
	 * @param prop
	 */
	public void drawItemAlreadyFoundDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. You've already found this item.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	DisplayController(Skin skin, GameSnapshot gSnapshot, SpriteBatch batch){
		this.batch = batch;
		this.displayMap = new DisplayMap(batch);
		this.uiSkin = skin;
		this.gameSnapshot = gSnapshot;
		this.guiController = new GUIController(uiSkin, gameSnapshot, this.batch);
	}
	
	void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> doors, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters){
		this.displayMap.drawMap(rooms, doors, objects, characters);
		this.guiController.drawControlStage();
		Gdx.input.setInputProcessor(this.guiController.controlStage);
	}
	
	GUIController drawGUI(){
		return this.guiController;
	}
	

}
