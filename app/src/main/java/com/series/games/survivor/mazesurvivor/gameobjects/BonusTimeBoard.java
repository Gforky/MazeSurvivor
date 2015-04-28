package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Luke on 4/27/2015.
 * Operation Board to manipulate the bonus time delayers
 */
public class BonusTimeBoard {

    private MazeCell timeLogo;
    private MazeCell numOfDelayers;

    public BonusTimeBoard(float ratio) {

        timeLogo = new MazeCell(new float[] {
                0.0f, -2.0f * ratio + 1.0f,//top left
                0.0f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom left
                0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom right
                0.25f, -2.0f * ratio + 1.0f//top right
        });

        numOfDelayers = new MazeCell(new float[] {
                0.25f, -2.0f * ratio + 1.0f,//top left
                0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom left
                0.5f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom right
                0.5f, -2.0f * ratio + 1.0f//top right
        });
    }

    public void drawBonusTimeBoard(GL10 gl, int bonusTime, int[] numTextures, int delayerNum) {

        timeLogo.draw(gl, bonusTime);
        numOfDelayers.draw(gl, numTextures[delayerNum]);
    }
}
