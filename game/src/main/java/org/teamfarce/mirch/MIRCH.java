package org.teamfarce.mirch;

import com.badlogic.gdx.Game;
import org.teamfarce.mirch.ScenarioBuilder.ScenarioBuilderException;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.entities.Player;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.map.Room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * MIRCH is used to generate all graphics in the program. It initialises the scenario generator and game state
 * and provides all interactions with the back end of the program.
 *
 * Lorem Ipsum executable file: http://lihq.me/Downloads/Assessment3/Game.zip
 *
 * @author jacobwunwin
 */
public class MIRCH extends Game {
    public static MIRCH me;
    public GameSnapshot gameSnapshotPlayer1;
    public GameSnapshot gameSnapshotPlayer2;
    public GUIController guiController;

    public ArrayList<Room> rooms;
    public ArrayList<Suspect> characters;

    public int step; //stores the current loop number

    public Player currentPlayer;

    private Player player1;
    private Player player2;

    /**
     * Initialises all variables in the game and sets up the game for play.
     */
    @Override
    public void create() {

        me = this;
        Assets.load();

        step = 0; //initialise the step variable

        ScenarioBuilderDatabase database;
        try {
            database = new ScenarioBuilderDatabase("db.db");

            try {
                gameSnapshotPlayer1 = ScenarioBuilder.generateGame(this, database, new Random());
                gameSnapshotPlayer2 = gameSnapshotPlayer1.makeCopy();
            } catch (ScenarioBuilderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //generate RenderItems from each room
        rooms = new ArrayList<>();
        for (Room room : gameSnapshotPlayer1.getRooms()) {
            rooms.add(room); //create a new renderItem for the room
        }

        //generate RenderItems for each prop


        //generate RenderItems for each suspect
        characters = new ArrayList<>();
        for (Suspect suspect : gameSnapshotPlayer1.getSuspects()) {
            characters.add(suspect);
        }

        gameSnapshotPlayer1.map.placeNPCsInRooms(characters);

        //initialise the currentPlayer sprite
        Dialogue playerDialogue = null;
        try {
            playerDialogue = new Dialogue("Player.JSON", true);
        } catch (Dialogue.InvalidDialogueException e) {
            System.out.print(e.getMessage());
            System.exit(0);
        }
        currentPlayer = new Player(this, "Bob", "The currentPlayer to beat all players", "Detective_sprite.png", playerDialogue);
        currentPlayer.setTileCoordinates(7, 10);
        this.currentPlayer.setRoom(rooms.get(0));

        //Setup screens
        this.guiController = new GUIController(this);
        this.guiController.initScreens();
    }

    /**
     * The render function deals with all game logic. It receives inputs from the input controller,
     * carries out logic and pushes outputs to the screen through the GUIController
     */
    @Override
    public void render() {
        this.guiController.update();
        super.render();

        step++; //increment the step counter
    }

    @Override
    public void dispose() {

    }
}
