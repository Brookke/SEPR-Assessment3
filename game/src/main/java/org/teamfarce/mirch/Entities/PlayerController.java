package org.teamfarce.mirch.Entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import org.teamfarce.mirch.Vector2Int;

/**
 * Created by brookehatton on 03/02/2017.
 */
public class PlayerController extends InputAdapter
{
    /**
     * This stores the player that the controller controls
     */
    private Player player;

    private boolean up, down, left, right;

    /**
     * Constructor to create the PlayerController to control the provided Player
     *
     * @param player - The player that we want this controller to control
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.W) {
            this.up = true;
            return true;
        }

        if (keycode == Input.Keys.A) {
            this.left = true;
            return true;
        }

        if (keycode == Input.Keys.D) {
            this.right = true;
            return true;
        }

        if (keycode == Input.Keys.S) {
            this.down = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Input.Keys.W) {
        this.up = false;
        return true;
    }

        if (keycode == Input.Keys.A) {
            this.left = false;
            return true;
        }

        if (keycode == Input.Keys.D) {
            this.right = false;
            return true;
        }

        if (keycode == Input.Keys.S) {
            this.down = false;
            return true;
        }

        return false;
    }

    public void update(int delta) {
        if (up) {
            player.move(Direction.NORTH);
        } else if (down) {
            player.move(Direction.SOUTH);
        } else if (left) {
            player.move(Direction.WEST);
        } else if (right) {
            player.move(Direction.EAST);
        }

    }
}
