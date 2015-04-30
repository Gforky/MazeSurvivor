package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Luke on 4/27/2015.
 * ScoreBoard to record the number of monsters be killed
 */
public class AliveMonsterBoard {

    private MazeCell monsterLogo;
    private MazeCell firstDigit;
    private MazeCell secondDigit;

    public AliveMonsterBoard(float ratio) {

        monsterLogo = new MazeCell(new float[] {
                -0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                -0.25f, -ratio,//bottom left
                0.0f, -ratio,//bottom right
                0.0f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });

        firstDigit = new MazeCell(new float[] {
                0.0f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                0.0f, -ratio,//bottom left
                0.25f, -ratio,//bottom right
                0.25f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });

        secondDigit = new MazeCell(new float[] {
                0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                0.25f, -ratio,//bottom left
                0.5f, -ratio,//bottom right
                0.5f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });
    }

    public void drawAliveMonsterBoard(GL10 gl, int monsterTexture, int[] numTextures, int aliveMonsters) {

        monsterLogo.draw(gl, monsterTexture);
        firstDigit.draw(gl, numTextures[aliveMonsters / 10]);
        secondDigit.draw(gl, numTextures[aliveMonsters % 10]);
    }
}
