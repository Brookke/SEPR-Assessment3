package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Screens.MapScreen;


/**
 * This class allows the player to be moved and controlled.
 */
public class PlayerController extends InputAdapter
{
    /**
     * This timer is used to measure how long input has been read for
     */
    public float timer = 0;

    /**
     * This timer is used to measure how long input has been read for
     */
    public float movementTime = 0.1f;
    /**
     * Booleans storing what keys have been pressed and not released
     */
    private boolean north, south, west, east;

    /**
     * This stores the player that the controller controls
     */
    private Player player;

    /**
     * This stores the game that is running
     */
    private MIRCH game;

    /**
     * Constructor to create the PlayerController to control the provided Player
     *
     * @param player - The player that we want this controller to control
     */
    public PlayerController(MIRCH game, Player player)
    {
        this.player = player;
        this.game = game;
    }

    /**
     * This method is called when a key press is read
     *
     * @param keycode - The code of the key pressed
     * @return (boolean) Whether this method acted upon the keypress or not. Used for InputMultiplexers
     */
    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
            //player.interact();
            return true;
        }

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            this.west = true;
            return true;
        }

        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            this.east = true;
            return true;
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            this.north = true;
            return true;
        }


        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            this.south = true;
            return true;
        }

        //TODO: The following 3 key reads could do with being placed in another controller

        return false;
    }

    /**
     * This method is called when a key release is read
     *
     * @param keycode - The keycode of the released key
     * @return (boolean) Whether this method processed the key release or not. Used for input multiplexers.
     */
    @Override
    public boolean keyUp(int keycode)
    {

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            this.west = false;
            return true;
        }

        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            this.east = false;
            return true;
        }

        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            this.north = false;
            return true;
        }

        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            this.south = false;
            return true;
        }

        return false;
    }

    /**
     * This method is called once a game tick to transfer the key reads to the live game data in the logic Thread.
     */
    public void update(float delta)
    {
        if (!south && !north && !east && !west) {
            timer = 0;
        }

        Direction goTo = null;

        if (north) {
            goTo = Direction.NORTH;
        } else if (south) {
            goTo = Direction.SOUTH;
        } else if (east) {
            goTo = Direction.EAST;
        } else if (west) {
            goTo = Direction.WEST;
        }

        if (goTo == null) return;

        timer += delta;

        if (timer > movementTime && !((MapScreen) game.guiController.mapScreen).isTransitioning()) {
            player.move(goTo);
            return;
        }

        if (player.getState() != AbstractPerson.PersonState.WALKING) {
            //player.setDirection(goTo);
        }
    }
}