package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

/**
 * 
 * @author jacobwunwin
 *
 */
public class GUIController {
	Gdx globalGdx;
	private JournalGUIController journalGUI;
	Stage controlStage;
	private GameSnapshot gameSnapshot;
	private Skin uiSkin;
	private SpriteBatch batch;
	private InterviewGUIController interviewController;
	
	GUIController(Skin skin, GameSnapshot gSnapshot, SpriteBatch batch){
		this.gameSnapshot = gSnapshot;
		this.uiSkin = skin;
		this.batch = batch;
		this.journalGUI = new JournalGUIController(this.uiSkin, this.gameSnapshot, this.batch);
		this.interviewController = new InterviewGUIController(this.uiSkin, this.gameSnapshot, this.batch);
		
		//++++CREATE MAIN CONRTOL STAGE++++
		final TextButton mapButton = new TextButton("Map", this.uiSkin);
		final TextButton journalButton = new TextButton("Journal", this.uiSkin);

		mapButton.setPosition(500, 700);
		journalButton.setPosition(650, 700);
		
		this.controlStage = new Stage();

		this.controlStage.addActor(mapButton);
		this.controlStage.addActor(journalButton);

		//add a listener for the show interview log button
		mapButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Map button was pressed");
				gameSnapshot.setState(GameState.map);
			}
		});

		//add a listener for the show interview log button
		journalButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Journal button was pressed");
				gameSnapshot.setState(GameState.journalHome);
			}
		});
	}
	
	void drawControlStage(){
		this.controlStage.draw();
	}
	
	void useJournalHomeView(){
		//Create an input multiplexer to take input from every stage
  	  InputMultiplexer multiplexer = new InputMultiplexer();
  	  multiplexer.addProcessor(this.journalGUI.journalStage);
  	  multiplexer.addProcessor(this.controlStage);
  	  Gdx.input.setInputProcessor(multiplexer);
  	  
  	  this.controlStage.draw(); //draw the global control buttons
  	  this.journalGUI.drawHome();
	}
	
	void useJournalCluesView(){
		//Create an input multiplexer to take input from every stage
	  	  InputMultiplexer multiplexer = new InputMultiplexer();
	  	  multiplexer.addProcessor(this.journalGUI.journalStage);
	  	  multiplexer.addProcessor(this.controlStage);
	  	  multiplexer.addProcessor(this.journalGUI.journalCluesStage);
	  	  Gdx.input.setInputProcessor(multiplexer);
	  	  
	  	  this.controlStage.draw(); //draw the global control buttons
	  	  this.journalGUI.drawClues();
	}
	
	void useJournalNotepadView(){
		//Create an input multiplexer to take input from every stage
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this.journalGUI.journalStage);
		multiplexer.addProcessor(this.journalGUI.journalNotepadStage);
		multiplexer.addProcessor(this.controlStage);
		Gdx.input.setInputProcessor(multiplexer);
		
		this.drawControlStage();
		this.journalGUI.drawNotepad();
	}
	
	void useJournalInterviewView(){
		//Create an input multiplexer to take input from every stage
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this.journalGUI.journalStage);
		multiplexer.addProcessor(this.journalGUI.journalQuestionsStage);
		multiplexer.addProcessor(this.controlStage);
		Gdx.input.setInputProcessor(multiplexer);

		this.drawControlStage();
		this.journalGUI.drawInterviewLog();
	}
	
	void initialiseInterviewGUI(Suspect suspect, Sprite player){
		this.interviewController.initInterviewStage(suspect, player);
	}
	
	void drawInterviewGUI(){
		Gdx.input.setInputProcessor(this.interviewController.interviewStage);
		this.interviewController.displayInterviewStage();
	}

}
