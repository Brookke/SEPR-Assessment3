package org.teamfarce.mirch;

import org.teamfarce.mirch.entities.AbstractPerson;
import org.teamfarce.mirch.entities.Player;
import org.teamfarce.mirch.map.Room;
import org.teamfarce.mirch.screens.MapScreen;
import org.teamfarce.mirch.screens.PuzzleScreen;

import java.util.List;

/**
 * Created by jacks on 17/03/2017.
 */
public class PuzzleGame {
    /*
    *
    * Public boolean for "game completed"
    * method for the game logic? linked to 8 buttons on screen
    * method updates bool to true or false
    * depending on if the game was won
    *
    */

    private boolean puzzleWon;
    public String codeEntered = "CODE";
    public String correctCode = "CODE42";
    private MIRCH game;

    public PuzzleGame(MIRCH game) {
        this.game = game;
    }
    public void PuzzleGame(){
        puzzleWon = false;
    }

    public boolean getPuzzleWonState(){
        return this.puzzleWon;
    }

    public void setPuzzleWon()
    {
        /*
        Player player = game.currentPlayer;
        this.puzzleWon = true;
        Room.Transition newRoomData = player.getRoom().getTransitionData(player.getTileCoordinates().x, player.getTileCoordinates().y);
        System.out.println(newRoomData.getNewRoom().getName());
        player.setRoom(newRoomData.getNewRoom());
        if (newRoomData.newDirection != null) {
            player.setDirection(newRoomData.newDirection);
            player.updateTextureRegion();
        }

        player.setTileCoordinates(newRoomData.newTileCoordinates.x, newRoomData.newTileCoordinates.y);
        */

        game.currentPlayer.setRoom(game.getGameSnapshot().map.rooms.get(10));
        MapScreen mapScreen = (MapScreen)game.getGUIController().mapScreen;
        OrthogonalTiledMapRendererWithPeople tileRenderer = mapScreen.getTileRenderer();
        tileRenderer.setMap(game.currentPlayer.getRoom().getTiledMap());
        tileRenderer.clearPeople();
        tileRenderer.addPerson((List<AbstractPerson>) ((List<? extends AbstractPerson>) mapScreen.currentNPCs));
        tileRenderer.addPerson(game.currentPlayer);
        game.getGameSnapshot().setState(GameState.map);
        game.currentPlayer.setTileCoordinates(9, 5);
        System.out.println("PUZZLE COMPLETE so changing to map state");
        this.puzzleWon = true;
    }

    public void addToCode(String digit){
        codeEntered += digit;
        System.out.println(codeEntered);
        if(codeEntered.contentEquals(correctCode)){
            setPuzzleWon();

        }
    }
}
