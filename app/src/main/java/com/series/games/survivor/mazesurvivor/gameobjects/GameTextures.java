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
    public int survivorUpTexture[];
    public int survivorDownTexture[];
    public int survivorLeftTexture[];
    public int survivorRightTexture[];
    public int exitTexture;
    public int leftButtonTexture;
    public int rightButtonTexture;
    public int upButtonTexture;
    public int downButtonTexture;
    public int lvSymbolTexture;
    public int numTextures[];
    public int monsterLogoTexture;
    public int monsterUpTexture[];
    public int monsterDownTexture[];
    public int monsterLeftTexture[];
    public int monsterRightTexture[];
    public int attackTexture;
    public int attackButtonTexture;
    public int trapTexture;
    public int activeTrapTexture;
    public int bonusTimeTexture;
    public int bonusTimeLogoTexture;
    public int pauseAlertTexture;
    public int gameOverAlertTexture;
    public int gameClearAlertTexture;
    public int restartTexture;

    public GameTextures(GL10 gl, Context context) {

        //load the textures used for drawing maze, load the texture once, and bind it to GL_TEXTURE_2D when render
        wallTexture = loadTexture(gl, context, R.drawable.wall);
        pathTexture = loadTexture(gl, context, R.drawable.path);
        survivorDownTexture = new int[]{
                loadTexture(gl, context, R.drawable.survivordown1),
                loadTexture(gl, context, R.drawable.survivordown2),
                loadTexture(gl, context, R.drawable.survivordown3),
        };
        survivorUpTexture = new int[]{
                loadTexture(gl, context, R.drawable.survivorup1),
                loadTexture(gl, context, R.drawable.survivorup2),
                loadTexture(gl, context, R.drawable.survivorup3)
        };
        survivorLeftTexture = new int[]{
                loadTexture(gl, context, R.drawable.survivorleft1),
                loadTexture(gl, context, R.drawable.survivorleft2),
                loadTexture(gl, context, R.drawable.survivorleft3)
        };
        survivorRightTexture = new int[]{
                loadTexture(gl, context, R.drawable.survivorright1),
                loadTexture(gl, context, R.drawable.survivorright2),
                loadTexture(gl, context, R.drawable.survivorright3)
        };
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
        monsterLogoTexture = loadTexture(gl, context, R.drawable.monsterlogo);
        monsterUpTexture = new int[]{
                loadTexture(gl, context, R.drawable.monsterup1),
                loadTexture(gl, context, R.drawable.monsterup2),
                loadTexture(gl, context, R.drawable.monsterup3)
        };
        monsterDownTexture = new int[]{
                loadTexture(gl, context, R.drawable.monsterdown1),
                loadTexture(gl, context, R.drawable.monsterdown2),
                loadTexture(gl, context, R.drawable.monsterdown3)
        };
        monsterLeftTexture = new int[]{
                loadTexture(gl, context, R.drawable.monsterleft1),
                loadTexture(gl, context, R.drawable.monsterleft2),
                loadTexture(gl, context, R.drawable.monsterleft3)
        };
        monsterRightTexture = new int[]{
                loadTexture(gl, context, R.drawable.monsterright1),
                loadTexture(gl, context, R.drawable.monsterright2),
                loadTexture(gl, context, R.drawable.monsterright3)
        };
        attackTexture = loadTexture(gl, context, R.drawable.attack);
        attackButtonTexture = loadTexture(gl, context, R.drawable.attackbutton);
        trapTexture = loadTexture(gl, context, R.drawable.trap);
        activeTrapTexture = loadTexture(gl, context, R.drawable.activetrap);
        bonusTimeTexture = loadTexture(gl, context, R.drawable.bonustime);
        bonusTimeLogoTexture = loadTexture(gl, context, R.drawable.bonustimelogo);
        pauseAlertTexture = loadTexture(gl, context, R.drawable.pause);
        gameOverAlertTexture = loadTexture(gl, context, R.drawable.gameover);
        gameClearAlertTexture = loadTexture(gl, context, R.drawable.youwin);
        restartTexture = loadTexture(gl, context, R.drawable.restart);
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
