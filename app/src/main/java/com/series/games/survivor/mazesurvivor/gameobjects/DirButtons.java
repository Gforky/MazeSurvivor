package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/22/2015.
 * direction control buttons, in order to manipulate the player
 */
public class DirButtons {

    //four control buttons
    private MazeCell upButton;
    private MazeCell downButton;
    private MazeCell leftButton;
    private MazeCell rightButton;

    public DirButtons(float ratio) {

        leftButton = new MazeCell(new float[] {
                -1.0f, -ratio,//top left
                -1.0f, -1.0f,//bottom left
                -1.0f / 3.0f, -1.0f,//bottom right
                -1.0f / 3.0f, -ratio//top right
        });
        rightButton = new MazeCell(new float[] {
                1.0f / 3.0f, -ratio,//top left
                1.0f / 3.0f, -1.0f,//bottom left
                1.0f, -1.0f,//bottom right
                1.0f, -ratio//top right
        });
        upButton = new MazeCell(new float[] {
                -1.0f / 3.0f, -ratio,//top left
                -1.0f / 3.0f, -(1.0f + ratio) / 2.0f,//bottom left
                1.0f / 3.0f, -(1.0f + ratio) / 2.0f,//bottom right
                1.0f / 3.0f, -ratio//top right
        });
        downButton = new MazeCell(new float[] {
                -1.0f / 3.0f, -(1.0f + ratio) / 2.0f,//top left
                -1.0f / 3.0f, -1.0f,//bottom left
                1.0f / 3.0f, -1.0f,//bottom right
                1.0f / 3.0f, -(1.0f + ratio) / 2.0f//top right
        });
    }

    /**Function to draw the direction control buttons
     *
     * @param gl
     */
    public void drawButtons(GL10 gl, int leftButtonTexture, int rightButtonTexture, int upButtonTexture, int downButtonTexture) {
        //draw operation buttons
        leftButton.draw(gl, leftButtonTexture);
        rightButton.draw(gl, rightButtonTexture);
        upButton.draw(gl, upButtonTexture);
        downButton.draw(gl, downButtonTexture);
    }
}
