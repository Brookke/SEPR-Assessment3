package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by brookehatton on 01/02/2017.
 */
public class Assets
{

    public static Texture doorwayTexture;

    public static void load()
    {
        doorwayTexture = new Texture(Gdx.files.internal("door.png"));
    }


}
