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
public class MIRCH extends Game {
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


    /**
     * Plays music in the background
     */
    private void playMusic() {
        music_background = Gdx.audio.newMusic(Gdx.files.internal("music/Minima.mp3"));
        music_background.setLooping(true);
        music_background.play();
    }


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
                gameSnapshot = ScenarioBuilder.generateGame(this, database, new Random());
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
        for (Room room : gameSnapshot.getRooms()) {
            rooms.add(room); //create a new renderItem for the room
        }

        //generate RenderItems for each prop


        //generate RenderItems for each suspect
        characters = new ArrayList<>();
        for (Suspect suspect : gameSnapshot.getSuspects()) {
            characters.add(suspect);
        }

        gameSnapshot.map.placeNPCsInRooms(characters);

        //initialise the player sprite
        player = new Player(this, "Bob", "The player to beat all players", "Detective_sprite.png");
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
    public void render() {
        this.guiController.update();
        super.render();

        step++; //increment the step counter
    }

    @Override
    public void dispose() {

    }
}
