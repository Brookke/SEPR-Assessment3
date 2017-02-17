package org.teamfarce.mirch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;

/**
 * Created by vishal on 17/02/2017.
 */
public class GameMusic {


    /**
     * Reference to main game, used to set current screen and access GameState
     */
    public MIRCH game;

    /**
     * State of the game last time update() was called
     */
    public GameState currentState;

    Music gameMusic;


    String song;


    GameMusic(MIRCH game){
        this.game = game;

        update();

        playMusic();
        System.out.println(getSong());
    }

    public void playMusic(){
        //Loading music and playing it on loop
        Music gameMusic= Gdx.audio.newMusic(Gdx.files.internal(song));
        //Loading music and playing it on loop
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    public void muteMusic(){
        gameMusic.stop();
    }

    public String getSong(){
        return(song);
    }

    public void setSong(String newSong){
        song= newSong;
    }

    public void update(){

        //Get latest game state
        GameState currentState = game.gameSnapshot.getState();

        //Set music depending on current game state

        switch (currentState) {
            case map:
                //gameMusic.dispose();
                song="music/Kings of Tara.mp3";
                System.out.println(getSong());
                //playMusic();
            case menu:
                //gameMusic.dispose();
                song= "music/Groundwork.mp3";
                System.out.println(getSong());
                //playMusic();
            case narrator:
                //gameMusic.dispose();
                song= "music/Village Consort.mp3";
                System.out.println(getSong());
                //playMusic();
    }

}
}

