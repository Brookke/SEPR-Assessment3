package org.teamfarce.mirch.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Settings;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.screens.MapScreen;

/**
 * This class allows the player to be moved and controlled.
 */
public class PlayerController extends InputAdapter {
    private final OrthographicCamera camera;
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
    public PlayerController(Player player, MIRCH game, OrthographicCamera camera) {
        this.player = player;
        this.camera = camera;
        this.game = game;
    }

    /**
     * This method is called when a key press is read
     *
     * @param keycode - The code of the key pressed
     * @return (boolean) Whether this method acted upon the keypress or not. Used for InputMultiplexers
     */
    @Override
    public boolean keyDown(int keycode) {
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

        return false;
    }

    /**
     * This method is called when a key release is read
     *
     * @param keycode - The keycode of the released key
     * @return (boolean) Whether this method processed the key release or not. Used for input multiplexers.
     */
    @Override
    public boolean keyUp(int keycode) {

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        player.interact(screenPosToTile(screenX, screenY));
        return true;
    }

    /**
     * This method converts an x and y coordinate on the screen to a tile location on the map
     *
     * @param screenX The screen x coordinate
     * @param screenY The screen y coordinate
     * @return - Vector2Int the tilePosition that was clicked on
     */
    private Vector2Int screenPosToTile(int screenX, int screenY) {
        Vector3 screenPosition = new Vector3(0, 0, 0);
        camera.unproject(screenPosition.set(screenX, screenY, 0));
        Vector2Int tileLocation = new Vector2Int((int) screenPosition.x / Settings.TILE_SIZE, (int) screenPosition.y / Settings.TILE_SIZE);
        return tileLocation;
    }

    /**
     * This method is called once a game tick to transfer the key reads to the live game data in the logic Thread.
     */
    public void update(float delta) {
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

        if (timer > movementTime && !((MapScreen) game.guiController.mapScreen).isTransitioning() && player.toMoveTo.isEmpty()) {
            player.move(goTo);
            return;
        }

        if (player.getState() != AbstractPerson.PersonState.WALKING) {
            player.direction = goTo;
        }
    }
}