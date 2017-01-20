package org.teamfarce.mirch;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.teamfarce.mirch.DialogueTree.Personality;
import org.teamfarce.mirch.Question.Style;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * MIRCH is used to generate all graphics in the program. It initialises the scenario generator and game state
 * and provides all interactions with the back end of the program.
 * @author jacobwunwin
 *
 */
public class MIRCH extends ApplicationAdapter{
	private static final boolean playAnnoyingMusic = false; //set to true to play incredibly annoying background music that ruins your songs
	private Texture titleScreen;
	private Texture doorwayTexture;
	private SpriteBatch batch;
	private GameSnapshot gameSnapshot;
	
	//generate different stages for the Journal
	private Stage journalStage;
	private Stage journalCluesStage;
	private Stage journalQuestionsStage;
	private Stage journalNotepadStage;
	
	private Sprite journalSprite;
	private Sprite dialogueSprite;
	
	private Stage questionIntentionStage;
	private Stage questionStyleStage;
	private Stage questionResponseStage;
	private Stage controlStage;
	
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
	
	private OrthographicCamera camera;
	
	private Music music_background;
	
	/**
	 * Detects whether a Sprite has been clicked by the mouse. Returns true if this is the case.
	 * @param theSprite
	 * @param mouse
	 * @return
	 */
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
	
	/**
	 * Draws an array list of characters (suspects) onto the screen in their correctl locations
	 * @param characters
	 * @param batch
	 */
	private void drawCharacters(ArrayList<RenderItem> characters, SpriteBatch batch){
		batch.begin();
		for (RenderItem character : characters){
			character.sprite.draw(batch);
		}
		batch.end();
	}
	
	/**
	 * Draws an array list of props onto the screen in their correct locations
	 * @param objects
	 * @param batch
	 */
	private void drawObjects(ArrayList<RenderItem> objects, SpriteBatch batch){
		batch.begin();
		for (RenderItem object : objects){
			object.sprite.draw(batch);
		}
		batch.end();
	}
	
	/**
	 * Draws an array list of rooms onto the screen in their correct locations
	 * @param rooms
	 * @param batch
	 */
	private void drawRooms(ArrayList<RenderItem> rooms, SpriteBatch batch){
		batch.begin();
		for (RenderItem room : rooms){
			room.sprite.draw(batch);
		}
		batch.end();
	}
	
	/**
	 * Draws an array list of doors onto the screen in their correct location
	 * @param doors
	 * @param batch
	 */
	private void drawDoors(ArrayList<RenderItem> doors, SpriteBatch batch){
		batch.begin();
		for (RenderItem door : doors){
			door.sprite.draw(batch);
		}
		batch.end();
	}
	
	/**
	 * Returns a RenderItem referencing the current room that the player sprite is in.
	 * @param rooms
	 * @param player
	 * @return
	 */
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
	
	/**
	 * Returns true if the sprite is in a doorway.
	 * @param doors
	 * @param player
	 * @return
	 */
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
	 * Draws the map onto the screen.
	 * @param rooms
	 * @param doors
	 * @param objects
	 * @param characters
	 * @param batch
	 */
	private void drawMap(ArrayList<RenderItem> rooms, ArrayList<RenderItem> doors, ArrayList<RenderItem> objects, ArrayList<RenderItem> characters, SpriteBatch batch){
		drawRooms(rooms, batch);
		drawDoors(doors, batch);
		drawObjects(objects, batch);
		drawCharacters(characters, batch);
	}
	
	/**
	 * Adds the scrollable list of clues to the cluesTable UI item
	 * @param cluesTable
	 * @param journal
	 */
	private void genJournalCluesStage(Table cluesTable, Journal journal){
		cluesTable.reset(); //reset the table

		for (Prop prop : journal.getProps()){
			Label label = new Label (prop.name + " : " + prop.description, uiSkin);
			cluesTable.add(label).width(280f); //set a maximum width on the row of 300 pixels
			cluesTable.row(); //end the row
		}
	}
	
	/**
	 * Adds the scrollable conversation text collected from conversations with characters
	 * to the questionTable UI item
	 * @param qTable
	 * @param journal
	 */
	private void genJournalQuestionsStage(Table qTable, Journal journal){
		qTable.reset(); //reset the table

		Label tlabel = new Label (journal.conversations, uiSkin);
		qTable.add(tlabel).width(300f); //set a maximum width on the row of 300 pixels
		qTable.row(); //end the row
	}
	
	/**
	 * Generates the base stage for the questions screen. This includes character images and names.
	 * @param theStage
	 * @param suspect
	 * @param player
	 */
	private void genQuestionBase(Stage theStage, Suspect suspect, Sprite player){
		//Create buttons and labels
		Label characterName = new Label (suspect.name, uiSkin);
		Image theSuspect = new Image(new Texture(Gdx.files.internal("assets/characters/" + suspect.filename)));
		Image thePlayer = new Image (player.getTexture());
		Label playerName = new Label ("You", uiSkin);


		//textButton.setPosition(200, 200);
		characterName.setPosition(830, 630);
		characterName.setColor(Color.WHITE);
		characterName.setFontScale(1.5f);
		
	
		theSuspect.setPosition(770, 360);
		theSuspect.scaleBy(2f);
				
		thePlayer.setPosition(210, 160);
		thePlayer.scaleBy(2f);
		
		playerName.setPosition(280,  120);
		playerName.setFontScale(1.5f);
		
		
		theStage.addActor(characterName);
		theStage.addActor(thePlayer);
		theStage.addActor(theSuspect);
		theStage.addActor(playerName);
	}
	
	
	/**
	 * Generates the question response screen
	 * @param theStage
	 * @param suspect
	 * @param player
	 * @param intent
	 * @param style
	 */
	private void genQResponseScreen(Stage theStage, Suspect suspect, Sprite player, int intent, int style){
		theStage.clear();
		
		genQuestionBase(theStage, suspect, player);
		
		String response = suspect.dialogueTree.selectStyledQuestion(intent, style, gameSnapshot.journal);
		
		Label comment = new Label (response, uiSkin);
		comment.setPosition(300, 600);
		theStage.addActor(comment);
		
		TextButton exitButton = new TextButton("End Conversation", uiSkin);
		exitButton.setPosition(300, 100);

		exitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Exit was pressed");
				gameSnapshot.setState(GameState.map);
			}
		});
		
		theStage.addActor(exitButton);
		
		TextButton continueButton = new TextButton("Ask Another Question", uiSkin);
		continueButton.setPosition(600, 100);

		continueButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Ask another question was pressed");
				genIntentionScreen(theStage, suspect, player); //move back to the intention screen
				gameSnapshot.incrementTime(); //increment the pseudotime counter as an action has been made
			}
		});
		
		theStage.addActor(continueButton);
		
		
		
		
		
	}
	
	/**
	 * Generates the question style selection screen UI stage
	 * @param theStage
	 * @param suspect
	 * @param player
	 */
	private void genStyleScreen(Stage theStage, Suspect suspect, Sprite player, int intent){
		theStage.clear();
		
		Label comment = new Label ("Be careful of your phrasing...", uiSkin);
		comment.setPosition(300, 600);
		theStage.addActor(comment);
		
		genQuestionBase(theStage, suspect, player);
		
		float buttonX = 300;
		float buttonY = 200;
		float buttonSpace = 50;
		ArrayList<String> styles = suspect.dialogueTree.getAvailableStyles(intent);
		for (int i = 0; i < styles.size(); i++){
			TextButton button = new TextButton(styles.get(i), uiSkin);
			button.setPosition(buttonX, buttonY);

			final int k = i;
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Style was pressed");
					genQResponseScreen(theStage, suspect, player, intent, k);
				}
			});

			theStage.addActor(button);

			buttonX += buttonSpace;
		}
	}
	
	/**
	 * Generates the question intention selection screen
	 * @param theStage
	 * @param suspect
	 * @param player
	 */
	private void genIntentionScreen(Stage theStage, Suspect suspect, Sprite player){
		theStage.clear(); //clear the stage
		
		genQuestionBase(theStage, suspect, player); //generate the base stage that we can the build off of	
		
		Label comment = new Label ("Go on then, ask your question.", uiSkin);
		comment.setPosition(300, 600);
		theStage.addActor(comment);
		
		float buttonX = 300;
		float buttonY = 200;
		float buttonSpace = 50;
		for (int i = 0; i < suspect.dialogueTree.getAvailableIntents().size(); i++){
			TextButton button = new TextButton(suspect.dialogueTree.getAvailableIntentsAsString().get(i), uiSkin);
			button.setPosition(buttonX, buttonY);

			final int k = i;
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Button was pressed");
					gameSnapshot.setState(GameState.dialogueStyle);
					genStyleScreen(theStage, suspect, player, k);
				}
			});

			theStage.addActor(button);

			buttonX += buttonSpace;
		}
	}

	
	/**
	 * Generates the question response screen UI stage
	 * @param theStage
	 * @param suspect
	 * @param player
	 */
	private void genResponseScreen(Stage theStage, Suspect suspect, Sprite player){

	}
	
	/**
	 * Creates a pop up window with text announcing that the user has found the referenced prop
	 * @param prop
	 */
	private void drawItemDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. It has been added to your Journal.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Creates a pop up window with text announcing that the user has already found the referenced prop
	 * @param prop
	 */
	private void drawItemAlreadyFoundDialogue(Prop prop){
		String output;
		output = "You find: '" + prop.description + "'. You've already found this item.";
		JOptionPane.showMessageDialog(null,output, prop.name,JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * Plays music in the background
	 */
	private void playMusic(){
		music_background = Gdx.audio.newMusic(Gdx.files.internal("assets/music/Minima.mp3"));
		music_background.setLooping(true);
		music_background.play();
	}

	/**
	 * Initialises all variables in the game and sets up the game for play.
	 */
	@Override
	public void create() {
		//++++INITIALISE THE GAME++++
		
		doorwayTexture = new Texture(Gdx.files.internal("assets/door.png")); //create the doorway texture

		
		step = 0; //initialise the step variable
		//create temporary required items, eventually ScenarioBuilder will generate these
		ArrayList<Suspect> tempSuspects = new ArrayList<Suspect>();
		
		Suspect tempSuspect = new Suspect("Devil_sprite.png", new Vector2(400, 400));

		/**
		 * please do not be mad.
		 * the tree begins to be constructed now.
		 */
		
		//constructing concrete questions:
		ArrayList<Question> questions1 = new ArrayList<Question>();
		ArrayList<Question> questions2 = new ArrayList<Question>();
		
		questions1.add(new Question(Style.AGGRESSIVE, "You look suspicious. What are you hiding?"));
		questions1.add(new Question(Style.PLACATING, "Don't worry, you're not in trouble or anything. I just need to know what you were doing earlier"));
		questions1.add(new Question(Style.CONVERSATIONAL, "Hi, I'm the detective. What have you been up to lately?"));
		questions1.add(new Question(Style.DIRECT, "What were you doing when the crime took place?"));
		questions1.add(new Question(Style.GRUNTSANDPOINTS, "*grunts mildly*, *points at suspect*"));
		
		questions2.add(new Question(Style.AGGRESSIVE, "Maybe one of your accompalices did the foul deed. Give me a name, now"));
		questions2.add(new Question(Style.PLACATING, "Everything will be fine, but I need your help; have you seen anyone suspicious?"));
		questions2.add(new Question(Style.CONVERSATIONAL, "The costumes here are pretty odd, don't you think? What's the weirdest you've seen?"));
		questions2.add(new Question(Style.DIRECT, "Have you seen anyone suspicious?"));
		questions2.add(new Question(Style.GRUNTSANDPOINTS, "*grunts harshly*, *beats chest*"));
		
		//creating concrete responses
		
		ArrayList<String> responses1 = new ArrayList<String>();
		
		responses1.add("Uhh I'm not really sure you should be asking me");
		responses1.add("Go away.");
		responses1.add("Sorry, I cant help you with that!");
		responses1.add("...");
		responses1.add("*beats chest*");
		
		String correctResp1 = "uhh, I was at the library earlier";
		String correctResp2 = "The guy in the vampire costume looked like he put a lot of effort into the fake blood";
		
		//creating Clues
		
		Clue library = new Clue(2, 4, "Library");
		Clue vampire = new Clue(2, 2, "Vampire_Costume");
		
		//creating responseIntents and QuestionIntents
		ResponseIntent respInt1 = new ResponseIntent(responses1, correctResp2, vampire);
		QuestionIntent questInt1 = new QuestionIntent(questions2, respInt1, "did you see anyone suspicious");
		ResponseIntent respInt2 = new ResponseIntent(responses1, correctResp1, library, questInt1);
		QuestionIntent questInt2 = new QuestionIntent(questions1, respInt2, "what were you doing earlier");
		
		ArrayList<QuestionIntent> questionIntentions = new ArrayList<QuestionIntent>();
		questionIntentions.add(questInt2);
		
		//Creating a dialogue tree of one question intent
		DialogueTree dTree = new DialogueTree(questionIntentions, Personality.ANXIOUS);
		
		tempSuspect.dialogueTree = dTree;
		tempSuspect.name = "The Devil";
		tempSuspect.description = "He is the devil";
		
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
		
		//Generate an ArrayList of RenderItems to store every door in the gameSnapshot
		doors = new ArrayList<RenderItem>();
		for (Door door : gameSnapshot.getDoors()){			
			Sprite newSprite = new Sprite(doorwayTexture);
			float xScale = (door.endX - door.startX)/(newSprite.getWidth());
			float yScale = (door.endY - door.startY)/(newSprite.getHeight());		
			//newSprite.setScale(xScale, yScale);

			newSprite.setSize(newSprite.getWidth() * xScale, newSprite.getHeight() * yScale);

			newSprite.setPosition(door.startX, door.startY);
			//newSprite.setRegion(door.startX, door.startY, (door.endX - door.startX)/2, door.endY - door.startY);
			doors.add(new RenderItem(newSprite, door));
		}
		
		//render the title screen texture
		titleScreen = new Texture(Gdx.files.internal("assets/Detective_sprite.png"));
		camera = new OrthographicCamera(); //set up the camera as an Orthographic camera
		camera.setToOrtho(false, 1366, 768); //set the size of the window

		batch = new SpriteBatch(); //create a new sprite batch - used to display sprites onto the screen
		
		//initialise the player sprite
		player = new Sprite(titleScreen);
		player.setPosition(250, 250);
		
		//initialise journal stages
		journalStage = new Stage();
		journalCluesStage = new Stage();
		journalQuestionsStage = new Stage();
		journalNotepadStage = new Stage();
		
		//starts music "Minima.mp3" - Kevin Macleod
		if (playAnnoyingMusic){ 
			playMusic();
		}
		
		//++INITIALISE GUI TEXTURES++++
		controlStage = new Stage(); //initialise a new stage to hold control buttons
		
		//Creates stages to store question related UI
		questionIntentionStage = new Stage();
		questionStyleStage = new Stage();
		questionResponseStage = new Stage();
		
		uiSkin = new Skin(Gdx.files.internal("assets/skins/skin_pretty/skin.json")); //load ui skin from assets
		//uiSkin = new Skin(Gdx.files.internal("assets/skins/skin_default/uiskin.json")); //load ui skin from assets

		//create a sprite for the journal background
		Texture journalBackground = new Texture(Gdx.files.internal("assets/Open_journal.png"));
		journalSprite = new Sprite(journalBackground);
		journalSprite.setPosition(220, 90);
		
		Texture dialogueBackground = new Texture(Gdx.files.internal("assets/dialogue_b.png"));
		dialogueSprite = new Sprite(dialogueBackground);
		dialogueSprite.setPosition(220, 90);
				
		//++++CREATE JOURNAL HOME STAGE++++
		
		//Create buttons and labels
		final TextButton cluesButton = new TextButton("Clues", uiSkin);
		final TextButton questionsButton = new TextButton("Interview Log", uiSkin);
		final TextButton notepadButton = new TextButton("Notepad", uiSkin);
		Label journalLabel = new Label ("Journal", uiSkin);
		
		//textButton.setPosition(200, 200);
		journalLabel.setPosition(360, 600);
		journalLabel.setColor(Color.BLACK);
		journalLabel.setFontScale(1.5f);
		
		cluesButton.setPosition(380, 400);
		questionsButton.setPosition(350, 350);
		notepadButton.setPosition(370, 300);
		//journalStage.addActor(textButton);
		journalStage.addActor(cluesButton);
		journalStage.addActor(journalLabel);
		journalStage.addActor(notepadButton);
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
		
		//add a listener for the show interview log button
		notepadButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Notepad button was pressed");
				gameSnapshot.setState(GameState.journalNotepad);
			}
		});
		
		//++++CREATE JOURNAL CLUES STAGE++++
		Label clueLabel = new Label("Clues", uiSkin);

		clueLabel.setColor(Color.BLACK);
		clueLabel.setFontScale(1.5f);

		clueLabel.setPosition(750, 600);
		
		//create a new table to store clues
		cluesTable = new Table(uiSkin);		
		
		//place the clues table in a scroll pane
	    Table container = new Table(uiSkin);
	    ScrollPane scroll = new ScrollPane(cluesTable, uiSkin);

	    
	    scroll.layout();
	    //add the scroll pane to an external container
	    container.add(scroll).width(300f).height(400f);
	    container.row();
	    container.setPosition(800, 360); //set the position of the extenal container
	    
	    //add actors to the clues stage
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
		
		//++++CREATE JOURNAL NOTEPAD STAGE++++
		
		//Create labels
		Label notepadLabel = new Label("Notepad", uiSkin);

		notepadLabel.setColor(Color.BLACK);
		notepadLabel.setFontScale(1.5f);

		notepadLabel.setPosition(720, 600);
		TextArea notepad = new TextArea("Here are my notes about a particularly develish crime...", uiSkin);
		notepad.setX(650);
		notepad.setY(150);
		notepad.setWidth(290);
		notepad.setHeight(400);
		
		journalNotepadStage.addActor(notepadLabel);
		journalNotepadStage.addActor(notepad);
		
		//Create an input multiplexer to take input from every stage
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(journalStage);
		multiplexer.addProcessor(journalQuestionsStage);
		multiplexer.addProcessor(journalCluesStage);
		multiplexer.addProcessor(journalNotepadStage);
		multiplexer.addProcessor(controlStage);
		multiplexer.addProcessor(questionIntentionStage);
		multiplexer.addProcessor(questionResponseStage);
		multiplexer.addProcessor(questionStyleStage);
		Gdx.input.setInputProcessor(multiplexer);
		
		System.out.println(rooms.size());
		//drawCharacterSelection(); 	      
	}
	
	/**
	 * The render function renders all items to the screen.
	 */
	@Override
	public void render() {
	      Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      
	      batch.setProjectionMatrix(camera.combined);
	      
	      //Draw the map here
	      if (gameSnapshot.getState() == GameState.map){
	    	  //store the players current room and position, so that we can later check that the player has not stepped over the room bounds
	    	  RenderItem currentRoom = getCurrentRoom(rooms, player); //find the current room that the player is in
	    	  Float currentX = player.getX();
	    	  Float currentY = player.getY();
	    	  
	    	  //System.out.println(player.getX());
	    	  // process keyboard touch and translate the player  	  
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
	    	  
	    	  RenderItem newRoom = getCurrentRoom(rooms, player); //find the new current room of the player
	    	  
	    	  //if we are no longer in the previous room and haven't entered a door, we move the player back
	    	  //to the old position
	    	  if (!currentRoom.equals(newRoom) && !inDoor(gameSnapshot.getDoors(), player)){
	    		  player.setX(currentX);
	    		  player.setY(currentY); 
	    	  }
	    	  
	    	  java.util.Random random = new java.util.Random();
	    	  System.out.println(random.nextInt(10));
	    	  
	    	  //loop through each suspect character, moving them randomly
	    	  for (RenderItem character: characters){
	    		  if ((step % moveStep) == 0){
	    			  System.out.println("Updating move step");
	    			  //Carries out a probability check to determine whether the character should move or stay stationary
	    			  //This gives the characters a 'meandering' look
	    			  if (random.nextInt(2) >= 1){ 
	    				  //calculate the new move vector for the character
		    			  float randX =  (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
			    		  float randY = (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
			    		  System.out.println(randX); 
		    			  Suspect suspect = (Suspect) character.object; //store the characters current room
		    			  suspect.moveStep = new Vector2(randX, randY);
		    			  character.object = suspect;
	    			  } else {
	    				  Suspect suspect = (Suspect) character.object;
		    			  suspect.moveStep = new Vector2(0f, 0f);
		    			  character.object = suspect;
	    			  }
	    		  }
	    		  
	    		  //Check to ensure character is still in room
    			  Suspect suspect = (Suspect) character.object; //retrieve the SUspect object from the renderItem

	    		  
	    		  float thisX = character.sprite.getX();
	    		  float thisY = character.sprite.getY();
	    		  //find the objects current room
	    		  RenderItem thisRoom = getCurrentRoom(rooms, character.sprite);
					
	    		  character.sprite.translate(suspect.moveStep.x, suspect.moveStep.y); //translate the character
	    		  
	    		  //find the characters new room
	    		  RenderItem thisNextRoom = getCurrentRoom(rooms, character.sprite);
	    		  
	    		  //check if the character has illegally left the rooms bounds, if it has move it back to its previous location
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

	    		  boolean clicked = false; //stores whether an object has been clicked
	    		  int length = objects.size();
	    		  int i = 0;
	    		  System.out.println(length);
	    		  //loop while no object has been clicked - this prevents the clicking of multiple items that may be overlaying eachother
	    		  length = characters.size();
	    		  i = 0;
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(characters.get(i).sprite, touchPos); //characters are drawn on top so check them first
	    			  
	    			  //if a character is clicked, generate the question intention screen for the character and change state so that the
	    			  //dialogue intention screen can be displayed
	    			  if (clicked){
	    				  genIntentionScreen(questionIntentionStage, (Suspect) characters.get(i).object, player);
	    				  gameSnapshot.setState(GameState.dialogueIntention); 
	    				  gameSnapshot.incrementTime(); //increment the pseudotime counter as an action has been made
	    			  }
	    			  i++;
	    		  }
	    		  
	    		  while (!clicked && (i < length)){
	    			  clicked = isObjectPressed(objects.get(i).sprite, touchPos);
	    			  if (clicked){
	    				  //handle touch input for objects, they are drawn underneath characters so check them next
	    				  System.out.println("Object clicked!");
	    				  //if the object has been clicked and isn't already in the journal, add it to the journal
	    				  if (gameSnapshot.journal.getProps().indexOf((Prop) objects.get(i).object) == -1){
		    				  drawItemDialogue((Prop) objects.get(i).object);
	    					  gameSnapshot.journal.addProp((Prop) objects.get(i).object); 
	    					  gameSnapshot.incrementTime(); //increment the pseudotime counter as an action has been made
	    				  } else {
	    					  //otherwise we report to the user that the object is already in the journal
		    				  drawItemAlreadyFoundDialogue((Prop) objects.get(i).object);
		    				  gameSnapshot.incrementTime(); //increment the pseudotime counter as an action has been made
	    				  }
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
	    	  controlStage.draw(); //draw the global control buttons
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalClues){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch); //draw the journal background
	    	  batch.end();
	    	  journalStage.draw();
	    	  genJournalCluesStage(cluesTable, gameSnapshot.journal); //generate the clues table
	    	  journalCluesStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //draw the clues table to the screen
	    	  journalCluesStage.draw();
	    	  
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalQuestions){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  genJournalQuestionsStage(questionsTable, gameSnapshot.journal); //generate the interview questions table
	    	  journalStage.draw();
	    	  journalQuestionsStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); //draw the questions table to the screen
	    	  journalQuestionsStage.draw();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalNotepad){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  controlStage.draw();
	    	  batch.begin();
	    	  journalSprite.draw(batch);
	    	  batch.end();
	    	  journalStage.draw();
	    	  journalNotepadStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	    	  journalNotepadStage.draw();
	    	  
	    	  
	      } else if (gameSnapshot.getState() == GameState.dialogueIntention){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      batch.begin();
		      dialogueSprite.draw(batch);
		      batch.end();
		      questionIntentionStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		      questionIntentionStage.draw();
		      
	      } 
	      
	      step++; //increment the step counter
		
	}
	
	@Override
	public void dispose() {
		  titleScreen.dispose();
	}
}