package org.teamfarce.mirch;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MIRCH extends ApplicationAdapter{
	private Texture titleScreen;
	private SpriteBatch batch;
	
	//generate different stages for the Journal
	private Stage journalStage;
	private Stage journalCluesStage;
	private Stage journalQuestionsStage;
	
	private Sprite journalSprite;
	
	private Stage controlStage;
	
	private Skin skin;
	private Skin uiSkin;
	
	private ArrayList<RenderItem> rooms;
	private ArrayList<RenderItem> objects;
	private ArrayList<RenderItem> characters;
	
	private float move = 5;

	
	private Sprite player;
	private GameState state;
	
	private OrthographicCamera camera;
	
	private boolean isObjectPressed(Sprite theSprite, Vector3 mouse){
		boolean toReturn = false;
		
		float x1 = theSprite.getX();
		float y1 = theSprite.getY();
		float x2 = x1 + theSprite.getWidth();
		float y2 = y1 + theSprite.getHeight();
		
		if ((mouse.x > x1) && (mouse.x < x2)){
			if ((mouse.y > y1) && (mouse.y < y2)){
				toReturn = true;
			}
		}
		
		return toReturn;
	}
	
	private void drawCharacters(ArrayList<RenderItem> characters, SpriteBatch batch){
		batch.begin();
		for (RenderItem character : characters){
			character.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawObjects(ArrayList<RenderItem> objects, SpriteBatch batch){
		batch.begin();
		for (RenderItem object : objects){
			object.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawRooms(ArrayList<RenderItem> rooms, SpriteBatch batch){
		batch.begin();
		for (RenderItem room : rooms){
			room.sprite.draw(batch);
		}
		batch.end();
	}
	
	private void drawMapControls(){
		
	}
	
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
	
	private void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters, SpriteBatch batch){
		drawRooms(rooms, batch);
		drawObjects(objects, batch);
		drawCharacters(characters, batch);
		drawMapControls();
	}
	
	private void drawNotebook(){
		
	}
	
	private void drawCharacterDialogue(){
		
	}
	
	private void drawItemDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. It has been added to your Journal.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	

	@Override
	public void create() {
		state = GameState.map;

		titleScreen = new Texture(Gdx.files.internal("assets/Detective_sprite.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1366, 768);

		batch = new SpriteBatch();
		
		//initialise the player sprite
		player = new Sprite(titleScreen);
		player.setPosition(100, 100);
		
		//initialise journal stages
		journalStage = new Stage();
		journalCluesStage = new Stage();
		journalQuestionsStage = new Stage();
		
		controlStage = new Stage(); //initialise a new stage to hold control buttons

		uiSkin = new Skin(Gdx.files.internal("assets/skins/uiskin.json")); //load ui skin from assets
		
		//create a sprite for the journal background
		Texture journalBackground = new Texture(Gdx.files.internal("assets/journal.png"));
		journalSprite = new Sprite(journalBackground);
		journalSprite.setPosition(220, 100);
		
		//++++CREATE JOURNAL HOME STAGE++++
		
		//Create buttons and labels
		final TextButton cluesButton = new TextButton("Clues", uiSkin);
		final TextButton questionsButton = new TextButton("Interview Log", uiSkin);
		Label journalLabel = new Label ("Journal", uiSkin);
		
		//textButton.setPosition(200, 200);
		journalLabel.setPosition(360, 600);
		journalLabel.setColor(Color.BLACK);
		journalLabel.setFontScale(1.5f);
		
		
		cluesButton.setPosition(380, 400);
		questionsButton.setPosition(350, 350);
		//journalStage.addActor(textButton);
		journalStage.addActor(cluesButton);
		journalStage.addActor(journalLabel);
		journalStage.addActor(questionsButton);
		
		// Add a listener to the clues button
		cluesButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Clues button was pressed");
				state = GameState.journalClues;
			}
		});

		//add a listener for the show interview log button
		questionsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Questions button was pressed");
				state = GameState.journalQuestions;
			}
		});
		
		//++++CREATE JOURNAL CLUES STAGE++++
		
		//Create labels
		Label clueLabel = new Label("Clues", uiSkin);
		
		clueLabel.setColor(Color.BLACK);
		clueLabel.setFontScale(1.5f);
		
		clueLabel.setPosition(750, 600);
		
		journalCluesStage.addActor(clueLabel);
		
		//++++CREATE JOURNAL INTERVIEW STAGE++++
		
		//Create labels
		Label questionsLabel = new Label("Interview Log", uiSkin);

		questionsLabel.setColor(Color.BLACK);
		questionsLabel.setFontScale(1.5f);

		questionsLabel.setPosition(720, 600);

		journalQuestionsStage.addActor(questionsLabel);
		
		//++++CREATE MAIN CONTROL BUTTONS STAGE++++
		
		final TextButton mapButton = new TextButton("Map", uiSkin);
		final TextButton journalButton = new TextButton("Journal", uiSkin);
		
		mapButton.setPosition(500, 700);
		journalButton.setPosition(650, 700);
		
		controlStage.addActor(mapButton);
		controlStage.addActor(journalButton);
		
		//add a listener for the show interview log button
		mapButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Map button was pressed");
				state = GameState.map;
			}
		});
		
		//add a listener for the show interview log button
		journalButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Journal button was pressed");
				state = GameState.journalHome;
			}
		});
		
		//Create an input multiplexer to take input from every stage
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(journalStage);
		multiplexer.addProcessor(journalQuestionsStage);
		multiplexer.addProcessor(journalCluesStage);
		multiplexer.addProcessor(controlStage);
		Gdx.input.setInputProcessor(multiplexer);
		

		objects = new ArrayList<RenderItem>();

		//create an example prop
		Prop prop = new Prop();
		prop.description = "A bloody axe...";

		Texture texture = new Texture(Gdx.files.internal("assets/objects/Axe.png"));
		objects.add(new RenderItem(new Sprite(texture), prop));

		objects.get(objects.size() - 1).sprite.setPosition(500,500);

		rooms = new ArrayList<RenderItem>();


		characters = new ArrayList<RenderItem>();

		//drawCharacterSelection();
	      
	}
	
	@Override
	public void render() {
	      Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      camera.update();
	      batch.setProjectionMatrix(camera.combined);
	      	      
	      
	      //Draw the map here
	      if (state == GameState.map){
	    	  drawMap(rooms, objects, characters, batch);
	    	  batch.begin();
	    	  player.draw(batch);
	    	  batch.end();
	    	  
	    	  controlStage.draw();

	    	  // process keyboard touch   	  
	    	  if(Gdx.input.isKeyPressed(Input.Keys.W)){
	    		  player.translate(0, move);
	    	  } 
	    	  if (Gdx.input.isKeyPressed(Input.Keys.S)){
	    		  player.translate(0, -move);
	    	  } 
	    	  if (Gdx.input.isKeyPressed(Input.Keys.A)){
	    		  player.translate(-move,  0);
	    	  } 
	    	  if (Gdx.input.isKeyPressed(Input.Keys.D)){
	    		  player.translate(move, 0);
	    	  }



	    	  // process user touch input
	    	  if (Gdx.input.isTouched()) {
	    		  Vector3 touchPos = new Vector3();
	    		  touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    		  camera.unproject(touchPos);

	    		  boolean clicked = false;
	    		  int length = rooms.size();
	    		  int i = 0;
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(rooms.get(i).sprite, touchPos);
	    			  if (clicked){
	    				  //handle touch input for room
	    			  }
	    			  i++;
	    		  }

	    		  length = objects.size();
	    		  i = 0;
	    		  System.out.println(length);
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(objects.get(i).sprite, touchPos);

	    			  if (clicked){
	    				  //handle touch input for objects
	    				  System.out.println("Object clicked!");
	    				  drawItemDialogue((Prop) objects.get(i).object);
	    				  //add the prop to the journal
	    			  }
	    			  i++;
	    		  }

	    		  length = characters.size();
	    		  i = 0;
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(characters.get(i).sprite, touchPos);

	    			  if (clicked){
	    				  //handle touch input for character
	    			  }
	    			  i++;
	    		  }

	    	  }
	      
	    	  //Draw the journal here
	      } else if (state == GameState.journalHome){
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  
	      } else if (state == GameState.journalClues){
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  journalCluesStage.draw();
	    	  
	    	  
	      } else if (state == GameState.journalQuestions){
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  journalQuestionsStage.draw();
	    	  
	    	  
	    	  
	      } else if (state == GameState.dialogue){
	      
	      
	    	  
	    	  
	    	  
	      }
		
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}