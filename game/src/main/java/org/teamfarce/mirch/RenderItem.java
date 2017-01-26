package org.teamfarce.mirch;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * The RenderItem class is used by the front end to link a front-end sprite with its back end
 * Object.
 */
public class RenderItem
{
    /**
     * The sprite of this item.
     */
    public Sprite sprite;

    /**
     * The object associated with this sprite.
     */
    public Object object;

    /**
     * Initialise the renderItem
     *
     * @param sprite The sprite to store.
     * @param object The object to associate.
     */
    public RenderItem(Sprite sprite, Object object)
    {
        this.sprite = sprite;
        this.object = object;
    }
}
