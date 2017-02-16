package org.teamfarce.mirch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Player;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.ScenarioBuilder.ScenarioBuilderException;
import org.teamfarce.mirch.map.Room;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;

/**
 * MIRCH is used to generate all graphics in the program. It initialises the scenario generator and game state
 * and provides all interactions with the back end of the program.
 *
 * @author jacobwunwin
 */
public class MIRCH extends Game
{
    private static final boolean playAnnoyingMusic = false; //set to true to play incredibly annoying background music that ruins your songs
    public static MIRCH me;
    public GameSnapshot gameSnapshot;
    public GUIController guiController;

    public ArrayList<Room> rooms;
    public ArrayList<Clue> objects;
    public ArrayList<Suspect> characters;


    public int step; //stores the current loop number

    public Player player;

    private Music music_background;

    private boolean testGame = false;


    /**
     * Controls the initial character traits selection at the start of the game.
     * <p>
     * Selection is made through a series of pop up windows.
     * </p>
     *
     * @return An array of integers containing the trait selections.
     */
    public int[] drawCharacterSelection()
    {
        int[] values = new int[3];
        String backstory = "Crash! \nA Murder has been committed in the Ron Cooke Hub \nYou are a detective, sent to solve the mystery.";
        JOptionPane.showMessageDialog(null, backstory, "Character Creation", JOptionPane.PLAIN_MESSAGE);
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
     * Plays music in the background
     */
    private void playMusic()
    {
        music_background = Gdx.audio.newMusic(Gdx.files.internal("music/Minima.mp3"));
        music_background.setLooping(true);
        music_background.play();
    }


    /**
     * Initialises all variables in the game and sets up the game for play.
     */
    @Override
    public void create()
    {

        me = this;
        Assets.load();

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

		} else*/
        {
            ScenarioBuilderDatabase database;
            try {
                database = new ScenarioBuilderDatabase("db.db");


                try {
                    Set<ScenarioBuilderDatabase.DataQuestioningStyle> newSet = new HashSet<>();

                    int[] output = drawCharacterSelection();

                    for (int i : output) {
                        newSet.add(database.questioningStyles.get(i));
                    }

                    gameSnapshot = ScenarioBuilder.generateGame(this,
                            database, 10, newSet, new Random()
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
        for (Room room : gameSnapshot.getRooms()) {
            rooms.add(room); //create a new renderItem for the room
        }

        //generate RenderItems for each prop


        //generate RenderItems for each suspect
        characters = new ArrayList<>();
        for (Suspect suspect : gameSnapshot.getSuspects()) {
            characters.add(suspect);
        }


        //initialise the player sprite
        player = new Player("Bob", "The player to beat all players", "Detective_sprite.png");
        player.setTileCoordinates(7, 10);
        this.player.setRoom(rooms.get(0));


        //starts music "Minima.mp3" - Kevin Macleod
        if (playAnnoyingMusic) {
            playMusic();
        }

        //Setup screens
        this.guiController = new GUIController(this);
        this.guiController.initScreens();
    }

    /**
     * The render function deals with all game logic. It receives inputs from the input controller,
     * carries out logic and pushes outputs to the screen through the GUIController
     */
    @Override
    public void render()
    {
        this.guiController.update();
        super.render();

        step++; //increment the step counter
    }

    @Override
    public void dispose()
    {

    }
}
