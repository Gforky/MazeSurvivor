package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 5/2/2015.
 * Display when game is over, notify to restart the game
 */
public class RestartAlertBoard {

    private MazeCell restartRlertBoard;

    public RestartAlertBoard(float ratio) {

        restartRlertBoard = new MazeCell(new float[] {
                -0.1f, ratio - 0.2f,//top left
                -0.1f, ratio - 0.3f,//bottom left
                0.1f, ratio - 0.3f,//bottom right
                0.1f, ratio - 0.2f//top right
        });
    }

    public void drawRestartAlertBoard(GL10 gl, int alertTexture) {

        restartRlertBoard.draw(gl, alertTexture);
    }
}
