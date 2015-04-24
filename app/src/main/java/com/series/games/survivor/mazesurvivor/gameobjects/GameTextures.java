package com.series.games.survivor.mazesurvivor.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.series.survivor.survivorgames.R;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/24/2015.
 * Class to store all the textures used for MazeSurvivor game
 */
public class GameTextures {

    //texture Ids used for drawing maze
    public int wallTexture;
    public int pathTexture;
    public int survivorTexture;
    public int exitTexture;
    public int leftButtonTexture;
    public int rightButtonTexture;
    public int upButtonTexture;
    public int downButtonTexture;
    public int lvSymbolTexture;
    public int numTextures[];
    public int monsterTexture;

    public GameTextures(GL10 gl, Context context) {

        //load the textures used for drawing maze, load the texture once, and bind it to GL_TEXTURE_2D when render
        wallTexture = loadTexture(gl, context, R.drawable.brick_wall);
        pathTexture = loadTexture(gl, context, R.drawable.grass);
        survivorTexture = loadTexture(gl, context, R.drawable.survivor);
        exitTexture = loadTexture(gl, context, R.drawable.exit);
        leftButtonTexture = loadTexture(gl, context, R.drawable.leftbutton);
        rightButtonTexture = loadTexture(gl, context, R.drawable.rightbutton);
        upButtonTexture = loadTexture(gl, context, R.drawable.upbutton);
        downButtonTexture = loadTexture(gl, context, R.drawable.downbutton);
        lvSymbolTexture = loadTexture(gl, context, R.drawable.lvsymbol);
        numTextures = new int[]{
                loadTexture(gl, context, R.drawable.num0),
                loadTexture(gl, context, R.drawable.num1),
                loadTexture(gl, context, R.drawable.num2),
                loadTexture(gl, context, R.drawable.num3),
                loadTexture(gl, context, R.drawable.num4),
                loadTexture(gl, context, R.drawable.num5),
                loadTexture(gl, context, R.drawable.num6),
                loadTexture(gl, context, R.drawable.num7),
                loadTexture(gl, context, R.drawable.num8),
                loadTexture(gl, context, R.drawable.num9)
        };
        monsterTexture = loadTexture(gl, context, R.drawable.monster);
    }

    /**Function to load the textures
     *
     * @param gl
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(GL10 gl, final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        gl.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0 )
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
