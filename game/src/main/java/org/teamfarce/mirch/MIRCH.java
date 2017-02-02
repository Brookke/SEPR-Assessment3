package org.teamfarce.mirch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.lang.*;


import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Player;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.ScenarioBuilder.ScenarioBuilderException;
import org.teamfarce.mirch.Screens.MapScreen;
import org.teamfarce.mirch.map.Room;

/**
 * MIRCH is used to generate all graphics in the program. It initialises the scenario generator and game state
 * and provides all interactions with the back end of the program.
 * @author jacobwunwin
 *
 */
public class MIRCH extends Game {
	private static final boolean playAnnoyingMusic = false; //set to true to play incredibly annoying background music that ruins your songs
	public SpriteBatch batch;
	public GameSnapshot gameSnapshot;
    private InputController inputController;
    public static MIRCH me;
	
	public DisplayController displayController;

	
	private Skin uiSkin;
	
	public ArrayList<Room> rooms;
	public ArrayList<Clue> objects;
	public ArrayList<Suspect> characters;
	public ArrayList<Door> doors;


	public int step; //stores the current loop number
	private int characterWidth = 60;

	public Player player;
	
	public OrthographicCamera camera;
	
	private Music music_background;
	
	private boolean testGame = false;
    public MapScreen mapScreen;

    /**
     * Controls the initial character traits selection at the start of the game.
     * <p>
     * Selection is made through a series of pop up windows.
     * </p>
     *
     * @return An array of integers containing the trait selections.
     */
    public int[] drawCharacterSelection() {
        int[] values  = new int[3];
        String backstory = "Crash! \nA Murder has been committed in the Ron Cooke Hub \nYou are a detective, sent to solve the mystery.";
        JOptionPane.showMessageDialog(null, backstory, "Character Creation",JOptionPane.PLAIN_MESSAGE);
        Object[] options1 = {
            "Aggresive",
            "Polite",
            "Conversation",
            "Direct",
            "Caveman"
        };

        values[0] = JOptionPane.showOptionDialog(
            null,
            "Choose a style for your character.",
            "Character Creation",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options1,
            options1[2]
        );    
        
        values[0]++;
        
        values[1] = JOptionPane.showOptionDialog(
            null,
            "Choose another style for your character.",
            "Character Creation",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options1,
            options1[2]
        );
        
        values[1]++;
        
        values[2] = JOptionPane.showOptionDialog(
            null,
            "Choose a final style for your character.",
            "Character Creation",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options1,
            options1[2]
        );
        
        values[2]++;
        
        return values;
    }

	/**
	 * Returns true if the sprite is in a doorway.
	 * @param doors
	 * @param player
	 * @return
	 */
	public boolean inDoor(ArrayList<Door> doors, Sprite player){
		boolean toReturn = false;
		//System.out.println("Checking door");
		for (Door door: doors){
			//System.out.println(door.startX);
			//System.out.println(player.getX());
			//System.out.println(door.endX);
			float allowedY = 50;
			float allowedX = 50;
			float maxX = (characterWidth / 2);
			float maxY = (characterWidth / 2);

			
			if (door.face == Door.Face.vertical){
				if ((player.getX() > door.startX - maxX) && (player.getX() < door.endX - maxX )){ //reduce by characterWidth/2 as sprites are located from bottom left corner
					if ((player.getY() > door.startY - allowedY) && (player.getY() < door.endY + allowedY)){
						toReturn = true;

					}
				}
			} else {
				if ((player.getY() > door.startY - maxY) && (player.getY() < door.endY - maxY )){ //reduce by characterWidth/2 as sprites are located from bottom left corner
					if ((player.getX() > door.startX - allowedX) && (player.getX() < door.endX + allowedX)){
						toReturn = true;

					}
				}

			}
			
			
		}
		
		return toReturn;
	}

	
	/**
	 * Plays music in the background
	 */
	private void playMusic(){
		music_background = Gdx.audio.newMusic(Gdx.files.internal("music/Minima.mp3"));
		music_background.setLooping(true);
		music_background.play();
	}


	private void initScreens() {
	    this.mapScreen = new MapScreen(this);
    }

	/**
	 * Initialises all variables in the game and sets up the game for play.
	 */
	@Override
	public void create() {

		this.me = this;
        Assets.load();
	    initScreens();
	    this.setScreen(mapScreen);

		//++++INITIALISE THE GAME++++
		
		step = 0; //initialise the step variable
		
	/*	if (testGame){
			//create temporary required items, eventually ScenarioBuilder will generate these
			ArrayList<Suspect> tempSuspects = new ArrayList<Suspect>();

			Suspect tempSuspect = new Suspect("Devil_sprite.png", new Vector2(400, 400));

			tempSuspect.dialogueTree = new DialogueTree();
			tempSuspect.name = "The Devil";
			tempSuspect.description = "He is the devil";

			tempSuspects.add(tempSuspect);

			QuestionIntent qi1 = new QuestionIntent(1, "Did you see anything suspicious?");
			tempSuspect.dialogueTree.addQuestionIntent(qi1);
w
			QuestionAndResponse qar11 = new QuestionAndResponse(
					"You look suspicious, what are you hiding?",
					"Aggressive",
					"What?! Nothing I swear",
					new ArrayList<>()
					);
			qi1.addQuestion(qar11);

			QuestionAndResponse qar12 = new QuestionAndResponse(
					"Have you seen anything unusual or suspicious?",
					"Direct",
					"The vampire has put on surprisingly good fake blood since the start of the party.",
					new ArrayList<>()
					);
			qi1.addQuestion(qar12);

			QuestionAndResponse qar13 = new QuestionAndResponse(
					"*Grunts mildly*, *points at suspect*",
					"Grunts and Points",
					"*Beats chest*",
					new ArrayList<>()

					);
			qi1.addQuestion(qar13);

			QuestionIntent qi2 = new QuestionIntent(2, "How long ago did you notice this");
			IDialogueTreeAdder qi2_add = new SingleDialogueTreeAdder(tempSuspect.dialogueTree, qi2);

			qar12.addDialogueTreeAdder(qi2_add);

			QuestionAndResponse qar21 = new QuestionAndResponse(
					"Why didn't you tell me earlier? Tell me now!",
					"Aggressive",
					"I don't know!",
					new ArrayList<>()
					);
			qi2.addQuestion(qar21);

			QuestionAndResponse qar22 = new QuestionAndResponse(
					"When do you think you noticed this?",
					"Direct",
					"Maybe an hour ago. Not too long before the body was dumped.",
					new ArrayList<>()
					);
			qi2.addQuestion(qar22);

			QuestionAndResponse qar23 = new QuestionAndResponse(
					"*Grunts harshly*, *Beat chest*",
					"Grunts and Points",
					"*Screams in the fifth dimension*",
					new ArrayList<>()
					);
			qi2.addQuestion(qar23);

			qar23.addDialogueTreeAdder(new SingleDialogueTreeAdder(tempSuspect.dialogueTree, qi2));

			ArrayList<Room> tempRooms = new ArrayList<Room>();

			Room temp2 = new Room("Classroom_2.png", new Vector2(200, 490));

			Room temp1 = new Room("Classroom_1.png", new Vector2(200, 200)); //generate a sample room for testign purposes


			Room temp3 = new Room("Classroom_2.png", new Vector2(500, -250));
			Room temp4 = new Room("Classroom_2.png", new Vector2(50, -250));
			Room temp5 = new Room("Classroom_2.png", new Vector2(-250, 200));

			tempRooms.add(temp3);
			tempRooms.add(temp1);
			tempRooms.add(temp2);
			tempRooms.add(temp4);
			tempRooms.add(temp5);



			ArrayList<Prop> tempProps = new ArrayList<Prop>();

			Prop prop = new Prop("Axe.png", temp1, new Vector2(50, 50)); //generate a sample prop for testing purposes
			prop.description = "A bloody axe...";
			prop.name = "Axe";
			tempProps.add(prop);			


			gameSnapshot = new GameSnapshot(tempSuspects, tempProps, tempRooms, 100, 100); //generate the GameSnapshot object

		} else*/ {
			ScenarioBuilderDatabase database;
			try {
				database = new ScenarioBuilderDatabase("db.db");
				

				try {
					Set<ScenarioBuilderDatabase.DataQuestioningStyle> newSet = new HashSet<>();
					
					int[] output = drawCharacterSelection();
					
					for (int i : output){
						newSet.add(database.questioningStyles.get(i));
					}
					
					gameSnapshot = ScenarioBuilder.generateGame(
						database, 10, 10, 5, 10, newSet, new Random()
					);
				} catch (ScenarioBuilderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		
		//generate RenderItems from each room
		rooms = new ArrayList<>();
		for (Room room : gameSnapshot.getRooms()){
			rooms.add(room); //create a new renderItem for the room
		}
		
		//generate RenderItems for each prop

		
		//generate RenderItems for each suspect
		characters = new ArrayList<>();
		for (Suspect suspect : gameSnapshot.getSuspects()){
			characters.add(suspect);
		}


		
		//render the title screen texture
		camera = new OrthographicCamera(); //set up the camera as an Orthographic camera
		camera.setToOrtho(false, 1366, 768); //set the size of the window

		batch = new SpriteBatch(); //create a new sprite batch - used to display sprites onto the screen
		//initialise the player sprite
		player = new Player("Bob", "The player to beat all players", "Detective_sprite.png");
		player.setPosition(210, 210);
        this.player.setRoom(rooms.get(0));

		
		//starts music "Minima.mp3" - Kevin Macleod
		if (playAnnoyingMusic){ 
			playMusic();
		}
		
		uiSkin = new Skin(Gdx.files.internal("skins/skin_pretty/skin.json")); //load ui skin from assets
		//uiSkin = new Skin(Gdx.files.internal("skins/skin_default/uiskin.json")); //load ui skin from assets
		
		this.displayController = new DisplayController(uiSkin, gameSnapshot, batch);
		this.inputController = new InputController();

	}
	
	/**
	 * The render function deals with all game logic. It recieves inputs from the input controller, 
	 * carries out logic and pushes outputs to the screen through the display controller
	 */
	@Override
	public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
	      
	      batch.setProjectionMatrix(camera.combined);
	      	      
	      //Draw the map here
	      if (gameSnapshot.getState() == GameState.journalHome){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().useJournalHomeView();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalClues){	    	  
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().useJournalCluesView();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalQuestions){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().useJournalInterviewView();
	    	  
	      } else if (gameSnapshot.getState() == GameState.journalNotepad){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
	    	  this.displayController.drawGUI().useJournalNotepadView();	    	  
	    	  
	      } else if (gameSnapshot.getState() == GameState.dialogueIntention){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().drawInterviewGUI();
		      
	      }  else if (gameSnapshot.getState() == GameState.accuse){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().drawAccuseGUI();
	      } else if (gameSnapshot.getState() == GameState.gameWon){
	    	  camera.position.set (new Vector3(camera.viewportWidth / 2, camera.viewportHeight / 2, 1)); //move the camera to follow the player
		      camera.update();
		      this.displayController.drawGUI().drawWinScreen();
	      }
	      
	      step++; //increment the step counter
		
	}
	
	@Override
	public void dispose() {
		
	}
}
