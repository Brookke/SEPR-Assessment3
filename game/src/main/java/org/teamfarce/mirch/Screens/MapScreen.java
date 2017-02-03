package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.teamfarce.mirch.*;
import org.teamfarce.mirch.Entities.Clue;
import org.teamfarce.mirch.Entities.Direction;
import org.teamfarce.mirch.Entities.PlayerController;
import org.teamfarce.mirch.Entities.Suspect;
import org.teamfarce.mirch.map.Room;

import java.util.Random;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class MapScreen extends AbstractScreen
{

    private OrthogonalTiledMapRendererWithPeople tileRender;
    private OrthographicCamera camera;
    private PlayerController playerController;
    private int moveStep = 50;
    public MapScreen(MIRCH game)
    {
        super(game);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, w, h);
        this.camera.update();
        this.tileRender = new OrthogonalTiledMapRendererWithPeople(game.player.getRoom().getTiledMap());
        this.tileRender.addPerson(game.player);
        this.playerController = new PlayerController(game.player);

    }

    @Override
    public void show()
    {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta)
    {

        playerController.update(delta);
        game.player.update(delta);
        camera.position.x = game.player.getX();
        camera.position.y = game.player.getY();
        camera.update();
        System.out.println(game.player.getTileCoordinates());
        tileRender.setView(camera);

        tileRender.render();


        //TODO: Fix this mess of collision handling and move it to the player...
        //Room newRoom = game.getCurrentRoom(game.rooms, game.player); //find the new current room of the player

        //if we are no longer in the previous room and haven't entered a door, we move the player back
        //to the old position

        Random random = new Random();
        //System.out.println(random.nextInt(10));

        //loop through each suspect character, moving them randomly
        for (Suspect character: game.characters){
            if ((game.step % moveStep) == 0){
                //System.out.println("Updating move step");
                //Carries out a probability check to determine whether the character should move or stay stationary
                //This gives the characters a 'meandering' look
                if (random.nextInt(2) >= 1){
                    //calculate the new move vector for the character


                    //System.out.println(randX);
                    character.move(Direction.EAST); //store the characters current room
                }
            }




            //find the characters new room
            //Room thisNextRoom = game.getCurrentRoom(game.rooms, character);

//            if (thisNextRoom == null || thisRoom == null) {
//
//                //check if the character has illegally left the rooms bounds, if it has move it back to its previous location
//            } else if (!thisRoom.equals(thisNextRoom)) {
//            }
//
//            suspect.setRoom(thisNextRoom); //update the current room the suspect is in in the back end

        }



//        } else if (inputController.isObjectClicked(game.objects, game.camera)){
//            Clue clue = (Clue) inputController.getClickedObject(game.objects, game.camera);
//            if (game.gameSnapshot.journal.getClues().indexOf(clue) == -1){
//                game.displayController.drawItemDialogue(clue);
//                game.gameSnapshot.journalAddClue(clue);
//            } else {
//                //otherwise we report to the user that the object is already in the journal
//                game.displayController.drawItemAlreadyFoundDialogue(clue);
//            }
//        }

        //Draw the map to the dispay



    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
