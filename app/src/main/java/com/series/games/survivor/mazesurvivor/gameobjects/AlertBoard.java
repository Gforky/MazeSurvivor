package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/29/2015.
 * Display Alerts according to different events
 */
public class AlertBoard {
    private MazeCell alertBoard;

    public AlertBoard(float ratio) {

        alertBoard = new MazeCell(new float[] {
                -0.25f, ratio,//top left
                -0.25f, ratio - 0.2f,//bottom left
                0.25f, ratio - 0.2f,//bottom right
                0.25f, ratio//top right
        });
    }

    public void drawAlertBoard(GL10 gl, int alertTexture) {

        alertBoard.draw(gl, alertTexture);
    }
}
