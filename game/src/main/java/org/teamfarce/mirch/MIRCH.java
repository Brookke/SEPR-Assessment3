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
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MIRCH extends ApplicationAdapter{
	private Texture titleScreen;
	private Texture doorwayTexture;
	private SpriteBatch batch;
	private GameSnapshot gameSnapshot;
	
	//generate different stages for the Journal
	private Stage journalStage;
	private Stage journalCluesStage;
	private Stage journalQuestionsStage;
	
	private Sprite journalSprite;
	
	private Stage questioningStage;
		
	private Stage controlStage;
	
	private Skin skin;
	private Skin uiSkin;
	
	private Table cluesTable;
	private Table questionsTable;
	
	private ArrayList<RenderItem> rooms;
	private ArrayList<RenderItem> objects;
	private ArrayList<RenderItem> characters;
	private ArrayList<RenderItem> doors;
	
	private float move = 3; //sets the speed at which the player move
	private float characterMove = 1f;
	private int moveStep = 50;
	private int step; //stores the current loop number
	private int characterWidth = 60;

	private Sprite player;
	private GameState state;
	private Sprite character;
	
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
	
	private void drawDoors(ArrayList<RenderItem> doors, SpriteBatch batch){
		batch.begin();
		for (RenderItem door : doors){
			door.sprite.draw(batch);
		}
		batch.end();
	}
	
	private RenderItem getCurrentRoom(ArrayList<RenderItem> rooms, Sprite player){
		for (RenderItem room : rooms){
			
			if ((player.getX() > room.sprite.getX()) && (player.getX() + player.getWidth() < room.sprite.getX() + room.sprite.getWidth())){
				if ((player.getY()> room.sprite.getY()) && (player.getY() + player.getHeight() < room.sprite.getY() + room.sprite.getHeight())){
					return room;
				}
			}
		}
		return new RenderItem(new Sprite(), new Object());
	}
	
	private boolean inDoor(ArrayList<Door> doors, Sprite player){
		boolean toReturn = false;
		System.out.println("Checking door");
		for (Door door: doors){
			System.out.println(door.startX);
			System.out.println(player.getX());
			System.out.println(door.endX);
			if ((player.getX() > door.startX - (characterWidth / 2)) && (player.getX() < door.endX - (characterWidth / 2))){ //reduce by characterWidth/2 as sprites are located from bottom left corner
				System.out.println("in x");
				
				if ((player.getY() > door.startY - 50) && (player.getY() < door.endY + 50)){
					toReturn = true;
					System.out.println("in y");
				}
			}
		}
		
		return toReturn;
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
	
	private void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> doors, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters, SpriteBatch batch){
		drawRooms(rooms, batch);
		drawDoors(doors, batch);
		drawObjects(objects, batch);
		drawCharacters(characters, batch);
		drawMapControls();
	}
	
	private void drawNotebook(){
		
	}
	
	//Draws the clues list onto the screen
	private void genJournalCluesStage(Table cluesTable, Journal journal){
		cluesTable.reset(); //reset the table
		//System.out.println(journal.getProps().size());
		//loop through each prop in the journal, adding it to the table
		for (Prop prop : journal.getProps()){
			Label label = new Label (prop.name + " : " + prop.description, uiSkin);
			cluesTable.add(label).width(280f); //set a maximum width on the row of 300 pixels
			cluesTable.row(); //end the row
		}
		
		/*String reallyLongString = "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
		        + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
		        + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n";
		
		Label clabel = new Label(reallyLongString, uiSkin);
		
		cTable.add(clabel);
		cTable.row();*/
	}
	
	
	private void genJournalQuestionsStage(Table qTable, Journal journal){
		qTable.reset(); //reset the table

		Label tlabel = new Label (journal.conversations, uiSkin);
		qTable.add(tlabel).width(300f); //set a maximum width on the row of 300 pixels
		qTable.row(); //end the row
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
		//++++INITIALISE THE GAME++++
		
		doorwayTexture = new Texture(Gdx.files.internal("assets/door.png")); //create the doorway texture

		
		step = 0; //initialise the step variable
		//create temporary required items, eventually ScenarioBuilder will generate these
		ArrayList<Suspect> tempSuspects = new ArrayList<Suspect>();
		
		Suspect tempSuspect = new Suspect("Devil_sprite.png", new Vector2(400, 400));
		
		tempSuspects.add(tempSuspect);
		
		ArrayList<Room> tempRooms = new ArrayList<Room>();
		
		Room temp2 = new Room("Classroom_2.png", new Vector2(200, 490));
		tempRooms.add(temp2);
		
		Room temp1 = new Room("Classroom_1.png", new Vector2(200, 200)); //generate a sample room for testign purposes
		tempRooms.add(temp1);

		ArrayList<Prop> tempProps = new ArrayList<Prop>();

		Prop prop = new Prop("Axe.png", temp1, new Vector2(50, 50)); //generate a sample prop for testing purposes
		prop.description = "A bloody axe...";
		prop.name = "Axe";
		tempProps.add(prop);

		ArrayList<Door> tempDoors = new ArrayList<Door>();
		tempDoors.add(new Door(300, 490, 350, 520));
		
		gameSnapshot = new GameSnapshot(tempSuspects, tempProps, tempRooms, tempDoors); //generate the GameSnapshot object
		
		//generate RenderItems from each room
		rooms = new ArrayList<RenderItem>();
		for (Room room : gameSnapshot.getRooms()){
			Sprite newSprite = new Sprite(new Texture(Gdx.files.internal("assets/rooms/" + room.filename)));
			newSprite.setPosition(room.position.x, room.position.y); //generate a sprite for the room
			rooms.add(new RenderItem(newSprite, (Room) room)); //create a new renderItem for the room
		}
		
		//generate RenderItems for each prop
		objects = new ArrayList<RenderItem>();
		for (Prop sprop : gameSnapshot.getProps()){
			Sprite newSprite = new Sprite(new Texture(Gdx.files.internal("assets/objects/" + sprop.filename)));
			newSprite.setPosition(sprop.currentRoom.position.x + sprop.roomPosition.x, sprop.currentRoom.position.y + sprop.roomPosition.y);
			objects.add(new RenderItem(newSprite, sprop));
		}
		
		//generate RenderItems for each suspect
		characters = new ArrayList<RenderItem>();
		for (Suspect suspect : gameSnapshot.getSuspects()){
			Sprite newSprite = new Sprite(new Texture(Gdx.files.internal("assets/characters/" + suspect.filename)));
			newSprite.setPosition(suspect.mapPosition.x, suspect.mapPosition.y);
			characters.add(new RenderItem(newSprite, (Suspect) suspect));
		}
		
		doors = new ArrayList<RenderItem>();
		for (Door door : gameSnapshot.getDoors()){
			Texture newTexture = new Texture(Gdx.files.internal("assets/door.png"));
			
			Sprite newSprite = new Sprite(doorwayTexture);
			float xScale = (door.endX - door.startX)/(newSprite.getWidth());
			float yScale = (door.endY - door.startY)/(newSprite.getHeight());		
			//newSprite.setScale(xScale, yScale);

			newSprite.setSize(newSprite.getWidth() * xScale, newSprite.getHeight() * yScale);

			newSprite.setPosition(door.startX, door.startY);
			//newSprite.setRegion(door.startX, door.startY, (door.endX - door.startX)/2, door.endY - door.startY);
			doors.add(new RenderItem(newSprite, door));
		}

		titleScreen = new Texture(Gdx.files.internal("assets/Detective_sprite.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1366, 768);

		batch = new SpriteBatch();
		
		//initialise the player sprite
		player = new Sprite(titleScreen);
		player.setPosition(250, 250);
		
		//initialise journal stages
		journalStage = new Stage();
		journalCluesStage = new Stage();
		journalQuestionsStage = new Stage();
		
		//++INITIALISE GUI TEXTURES++++
		controlStage = new Stage(); //initialise a new stage to hold control buttons
		
		questioningStage = new Stage();

		uiSkin = new Skin(Gdx.files.internal("assets/skins/uiskin.json")); //load ui skin from assets
		
		//create a sprite for the journal background
		Texture journalBackground = new Texture(Gdx.files.internal("assets/journal.png"));
		journalSprite = new Sprite(journalBackground);
		journalSprite.setPosition(220, 90);
				
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
				gameSnapshot.setState(GameState.journalClues);
			}
		});

		//add a listener for the show interview log button
		questionsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Questions button was pressed");
				gameSnapshot.setState(GameState.journalQuestions);
			}
		});
		
		//++++CREATE JOURNAL CLUES STAGE++++
		Label clueLabel = new Label("Clues", uiSkin);

		clueLabel.setColor(Color.BLACK);
		clueLabel.setFontScale(1.5f);

		clueLabel.setPosition(750, 600);

		cluesTable = new Table(uiSkin);		
		
	    Table container = new Table(uiSkin);
	    ScrollPane scroll = new ScrollPane(cluesTable, uiSkin);

	    
	    scroll.layout();
	    container.add(scroll).width(300f).height(400f);
	    container.row();
	    container.setPosition(800, 360);
	    

		journalCluesStage.addActor(clueLabel);
		journalCluesStage.addActor(container);
		
		
		//++++CREATE JOURNAL INTERVIEW STAGE++++
		
		//Create labels
		Label questionsLabel = new Label("Interview Log", uiSkin);

		questionsLabel.setColor(Color.BLACK);
		questionsLabel.setFontScale(1.5f);

		questionsLabel.setPosition(720, 600);

		questionsTable = new Table(uiSkin);

		Table qcontainer = new Table(uiSkin);
		ScrollPane qscroll = new ScrollPane(questionsTable, uiSkin);
		//scroll.setStyle("-fx-background: transparent;"); //the numpteys depreciated this very useful command to make the background transparent


		qscroll.layout();
		qcontainer.add(qscroll).width(300f).height(400f);
		qcontainer.row();
		qcontainer.setPosition(800, 360);

		journalQuestionsStage.addActor(questionsLabel);
		journalQuestionsStage.addActor(qcontainer);
		
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
		
		//++++CREATE QUESTIONING STAGE++++
		
		
		//Create an input multiplexer to take input from every stage
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(journalStage);
		multiplexer.addProcessor(journalQuestionsStage);
		multiplexer.addProcessor(journalCluesStage);
		multiplexer.addProcessor(controlStage);
		multiplexer.addProcessor(questioningStage);
		Gdx.input.setInputProcessor(multiplexer);
		
		System.out.println(rooms.size());
		//drawCharacterSelection();
	      
	}
	
	@Override
	public void render() {
	      Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      
	      batch.setProjectionMatrix(camera.combined);
	      	      
	      
	      //Draw the map here
	      if (gameSnapshot.getState() == GameState.map){
	    	  RenderItem currentRoom = getCurrentRoom(rooms, player); //find the current room that the player is in
	    	  Float currentX = player.getX();
	    	  Float currentY = player.getY();
	    	  
	    	  //System.out.println(player.getX());
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
	    	  
	    	  RenderItem newRoom = getCurrentRoom(rooms, player);
	    	  
	    	  if (!currentRoom.equals(newRoom) && !inDoor(gameSnapshot.getDoors(), player)){
	    		  player.setX(currentX);
	    		  player.setY(currentY); 
	    	  }
	    	  
	    	  java.util.Random random = new java.util.Random();
	    	  System.out.println(random.nextInt(10));
	    	  
	    	  //loop through each character, moving them randomly
	    	  for (RenderItem character: characters){
	    		  if ((step % moveStep) == 0){
	    			  System.out.println("Updating move step");
	    			  if (random.nextInt(2) >= 1){
		    			  float randX =  (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
			    		  float randY = (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
			    		  System.out.println(randX);
		    			  Suspect suspect = (Suspect) character.object;
		    			  suspect.moveStep = new Vector2(randX, randY);
		    			  character.object = suspect;
	    			  } else {
	    				  Suspect suspect = (Suspect) character.object;
		    			  suspect.moveStep = new Vector2(0f, 0f);
		    			  character.object = suspect;
	    			  }
	    		  }
	    		  
	    		  //Check to ensure character is still in room
    			  Suspect suspect = (Suspect) character.object;

	    		  
	    		  float thisX = character.sprite.getX();
	    		  float thisY = character.sprite.getY();
	    		  RenderItem thisRoom = getCurrentRoom(rooms, character.sprite);
					
	    		  character.sprite.translate(suspect.moveStep.x, suspect.moveStep.y);
	    		  
	    		  RenderItem thisNextRoom = getCurrentRoom(rooms, character.sprite);
	    		  
	    		  if (!thisRoom.equals(thisNextRoom) && !inDoor(gameSnapshot.getDoors(), character.sprite)){
		    		  character.sprite.setX(thisX);
		    		  character.sprite.setY(thisY); 
		    	  }
		    	  
	    	  } 
	    	  

	    	  // process user touch input
	    	  if (Gdx.input.isTouched()) {
	    		  Vector3 touchPos = new Vector3();
	    		  touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    		  camera.unproject(touchPos);

	    		  boolean clicked = false;
	    		  int length = objects.size();
	    		  int i = 0;
	    		  System.out.println(length);
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(objects.get(i).sprite, touchPos);

	    			  if (clicked){
	    				  //handle touch input for objects
	    				  System.out.println("Object clicked!");
	    				  drawItemDialogue((Prop) objects.get(i).object);
	    				  //add the prop to the journal
	    				  gameSnapshot.journal.addProp((Prop) objects.get(i).object); 
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
	    	  
	    	  //Draw the map to the display
	    	  camera.position.set (new Vector3(player.getX(), player.getY(), 1)); //move the camera to follow the player
		      camera.update();
	    	  drawMap(rooms, doors, objects, characters, batch);
	    	  batch.begin();
	    	  player.draw(batch);
	    	  batch.end();
	    	  
	    	  controlStage.draw();
	      
	    	  //Draw the journal here
	      } else if (gameSnapshot.getState() == GameState.journalHome){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalClues){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  genJournalCluesStage(cluesTable, gameSnapshot.journal);
	    	  journalCluesStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	    	  journalCluesStage.draw();
	    	  
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalQuestions){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  genJournalQuestionsStage(questionsTable, gameSnapshot.journal);
	    	  journalStage.draw();
	    	  journalQuestionsStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	    	  journalQuestionsStage.draw();
	    	  
	    	  
	    	  
	      } else if (state == GameState.dialogue){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	      
	      
	      }
	      
	      step++; //increment the step counter
		
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}