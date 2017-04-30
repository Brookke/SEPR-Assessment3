package org.teamfarce.mirch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
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
    final int playerTurnTimeInFrames = 300; // in frames

    public static MIRCH me;

    public int step; //stores the current loop number

    public boolean hasNewGameStarted = false; // initially in main menu - see GUIController
    public Player currentPlayer;
    private int numberOfPlayers; // either 1 or 2
    private int currentPlayerNumber; // either 1 or 2 to represent which player is playing

    private Player player1;
    private Player player2;

    private GameSnapshot gameSnapshotPlayer1;
    private GameSnapshot gameSnapshotPlayer2;
    private GUIController guiControllerPlayer1;
    private GUIController guiControllerPlayer2;

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
                gameSnapshotPlayer1 = ScenarioBuilder.generateGame(this, database, new Random(42));
                gameSnapshotPlayer2 = ScenarioBuilder.generateGame(this, database, new Random(43));
            } catch (ScenarioBuilderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        gameSnapshotPlayer1.map.placeNPCsInRooms(gameSnapshotPlayer1.getSuspects());
        gameSnapshotPlayer2.map.placeNPCsInRooms(gameSnapshotPlayer2.getSuspects());

        //initialise the sprites for each player
        Dialogue playerDialogue = null;
        try {
            playerDialogue = new Dialogue("Player.JSON", true);
        } catch (Dialogue.InvalidDialogueException e) {
            System.out.print(e.getMessage());
            System.exit(0);
        }

        player1 = new Player(this, "Bob", "Bob the pro detective.", "Detective_sprite.png", playerDialogue);
        player2 = new Player(this, "Dave", "Dave the super cool detective.", "Detective_sprite.png", playerDialogue);
        player1.setTileCoordinates(7, 10);
        player2.setTileCoordinates(7, 10);
        player1.setRoom(gameSnapshotPlayer1.getRooms().get(0));
        player2.setRoom(gameSnapshotPlayer2.getRooms().get(0));

        currentPlayer = player1;
        currentPlayerNumber = 1;

        //Setup screens
        guiControllerPlayer1 = new GUIController(this);
        guiControllerPlayer1.initScreens();
        // Small hack: before creating GUIController 2 set currentPlayer to 2, since MapScreen and perhaps other things reference currentPlayer
        currentPlayer = player2;
        guiControllerPlayer2 = new GUIController(this);
        guiControllerPlayer2.initScreens();
        currentPlayer = player1;
    }

    /**
     * The render function deals with all game logic. It receives inputs from the input controller,
     * carries out logic and pushes outputs to the screen through the GUIController
     */
    @Override
    public void render() {
        getGUIController().update();
        step++;

        if (step % 10 == 0)
            System.out.println("step: " + step);

        if (numberOfPlayers > 1 && step % playerTurnTimeInFrames == 0) {
            // Swap the current player
            if (currentPlayerNumber == 1) {
                System.out.println("Swapping current player from 1 to 2.");
                currentPlayer = player2;
                currentPlayerNumber = 2;
            }
            else if (currentPlayerNumber == 2) {
                System.out.println("Swapping current player from 2 to 1.");
                currentPlayer = player1;
                currentPlayerNumber = 1;
            }
            else {
                System.out.println("Invalid currentPlayer!");
            }

            getGUIController().onPlayerSwitch();
        }

        super.render();
    }

    @Override
    public void dispose() {

    }

    public void startNewGame(int numberOfPlayers) {
        assert(numberOfPlayers == 1 || numberOfPlayers == 2);
        System.out.println("Starting a new game with " + numberOfPlayers + " players.");
        this.numberOfPlayers = numberOfPlayers;
        hasNewGameStarted = true;
    }

    public GameSnapshot getGameSnapshot() {
        if (currentPlayer == player1) {
            return gameSnapshotPlayer1;
        }
        else if (currentPlayer == player2) {
            return gameSnapshotPlayer2;
        }
        else {
            System.out.println("Invalid currentPlayer! Oh dear!");
            return null;
        }
    }

    public GUIController getGUIController() {
        if (currentPlayer == player1) {
            return guiControllerPlayer1;
        }
        else if (currentPlayer == player2) {
            return guiControllerPlayer2;
        }
        else {
            System.out.println("Invalid currentPlayer! Oh dear!");
            return null;
        }
    }

    public void setGameSnapshotForTestingPurposes(GameSnapshot snapshot) {
        gameSnapshotPlayer1 = snapshot;
    }

    public void setGUIControllerForTestingPurposes(GUIController controller) {
        guiControllerPlayer1 = controller;
    }
}
