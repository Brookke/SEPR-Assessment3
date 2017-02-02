/**
 *
 */
package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * The input controller deals with all mouse and keyboard inputs, converting them to
 * meaningful commands for use by the render function.
 *
 * @author jacobwunwin
 */
public class InputController
{

    /**
     * Empty initialiser for the input controller
     */
    public InputController()
    {

    }

    /**
     * Returns a vector of the players latest move
     *
     * @return
     */
    public Vector2 fetchPlayerPositionUpdate()
    {
        // process keyboard touch and translate the player

        Vector2 move = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            move.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            move.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move.x += 1;
        }


        return move;
    }


    /**
     * Detects whether a Sprite has been clicked by the mouse. Returns true if this is the case.
     *
     * @param theSprite
     * @param mouse
     * @return
     */
    private boolean isSpritePressed(Sprite theSprite, Vector3 mouse)
    {
        boolean toReturn = false;

        float x1 = theSprite.getX();
        float y1 = theSprite.getY();
        float x2 = x1 + theSprite.getWidth();
        float y2 = y1 + theSprite.getHeight();

        if ((mouse.x > x1) && (mouse.x < x2)) {
            if ((mouse.y > y1) && (mouse.y < y2)) {
                toReturn = true;
            }
        }

        return toReturn;
    }

    /**
     * Detects whether a RenderItem has been clicked by the mouse, and returns a boolean
     *
     * @param object
     * @param camera
     * @return
     */
    public boolean isObjectClicked(ArrayList<RenderItem> object, Camera camera)
    {
        // process user touch input
        if (Gdx.input.isTouched()) {

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            for (int i = 0; i < object.size(); i++) {
                if (this.isSpritePressed(object.get(i).sprite, touchPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the RenderItem clicked by the mouse from a given list of RenderItems
     *
     * @param object
     * @param camera
     * @return
     */
    public RenderItem getClickedObject(ArrayList<RenderItem> object, Camera camera)
    {
        // process user touch input
        if (Gdx.input.isTouched()) {

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            for (int i = 0; i < object.size(); i++) {
                if (this.isSpritePressed(object.get(i).sprite, touchPos)) {
                    return object.get(i);
                }
            }
        }

        return null;
    }
}
