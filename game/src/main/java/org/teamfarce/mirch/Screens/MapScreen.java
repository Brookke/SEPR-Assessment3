package org.teamfarce.mirch.Screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.teamfarce.mirch.*;
import org.teamfarce.mirch.Entities.Prop;
import org.teamfarce.mirch.Entities.Suspect;

import java.util.Random;

import static sun.audio.AudioPlayer.player;

/**
 * Created by brookehatton on 31/01/2017.
 */
public class MapScreen extends AbstractScreen
{

    private InputController inputController;
    private float characterMove = 1f;
    private int moveStep = 50;


    public MapScreen(MIRCH game)
    {
        super(game);
        this.inputController = new InputController();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        RenderItem currentRoom = game.getCurrentRoom(game.rooms, game.player); //find the current room that the player is in
        Float currentX = game.player.getX();
        Float currentY = game.player.getY();

        game.player.move(inputController.fetchPlayerPositionUpdate());

        //TODO: Fix this mess of collision handling and move it to the player...
        RenderItem newRoom = game.getCurrentRoom(game.rooms, game.player); //find the new current room of the player

        //if we are no longer in the previous room and haven't entered a door, we move the player back
        //to the old position
        if (!currentRoom.equals(newRoom) && !game.inDoor(game.doors, game.player)){
            game.player.setX(currentX);
            game.player.setY(currentY);
        }

        Random random = new Random();
        //System.out.println(random.nextInt(10));

        //loop through each suspect character, moving them randomly
        for (RenderItem character: game.characters){
            if ((game.step % moveStep) == 0){
                //System.out.println("Updating move step");
                //Carries out a probability check to determine whether the character should move or stay stationary
                //This gives the characters a 'meandering' look
                if (random.nextInt(2) >= 1){
                    //calculate the new move vector for the character
                    float randX =  (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
                    float randY = (float) random.nextInt((int) characterMove * 2 + 1) - characterMove;
                    //System.out.println(randX);
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
            Suspect suspect = (Suspect) character.object; //retrieve the Suspect object from the renderItem


            float thisX = character.sprite.getX();
            float thisY = character.sprite.getY();
            //find the objects current room
            RenderItem thisRoom = game.getCurrentRoom(game.rooms, character.sprite);

            character.sprite.translate(suspect.moveStep.x, suspect.moveStep.y); //translate the character

            //find the characters new room
            RenderItem thisNextRoom = game.getCurrentRoom(game.rooms, character.sprite);

            //check if the character has illegally left the rooms bounds, if it has move it back to its previous location
            if (!thisRoom.equals(thisNextRoom) && !game.inDoor(game.doors, character.sprite)){
                character.sprite.setX(thisX);
                character.sprite.setY(thisY);
            }

            suspect.setRoom((Room) thisNextRoom.object); //update the current room the suspect is in in the back end

        }

        if (inputController.isObjectClicked(game.characters, game.camera)){
            RenderItem character = inputController.getClickedObject(game.characters, game.camera);
            game.displayController.drawGUI().initialiseInterviewGUI((Suspect) character.object, game.player);

            //TODO: swap to the dialogue screen here
            game.gameSnapshot.setState(GameState.dialogueIntention);

        } else if (inputController.isObjectClicked(game.objects, game.camera)){
            RenderItem object = inputController.getClickedObject(game.objects, game.camera);
            if (game.gameSnapshot.journal.getProps().indexOf((Prop) object.object) == -1){
                game.displayController.drawItemDialogue((Prop) object.object);
                game.gameSnapshot.journalAddProp((Prop) object.object);
            } else {
                //otherwise we report to the user that the object is already in the journal
                game.displayController.drawItemAlreadyFoundDialogue((Prop) object.object);
            }
        }

        //Draw the map to the display
        game.camera.position.set (new Vector3(game.player.getX(), game.player.getY(), 1)); //move the camera to follow the player
        game.camera.update();
        game.displayController.drawMap(game.rooms, game.doors, game.objects, game.characters);

        game.batch.begin();
        game.player.draw(game.batch);
        game.batch.end();


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
