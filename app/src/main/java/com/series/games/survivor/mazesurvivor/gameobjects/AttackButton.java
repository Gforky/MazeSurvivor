package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/25/2015.
 * Attack Button
 */
public class AttackButton {

    private MazeCell attackButton;

    public AttackButton(float ratio) {

        attackButton = new MazeCell(new float[]{
                0.5f, -2.0f * ratio + 1.0f,//top left
                0.5f, -ratio,//bottom left
                1.0f, -ratio,//bottom right
                1.0f, -2.0f * ratio + 1.0f//top right
        });
    }

    public void drawAttackButton(GL10 gl, int attackTexture) {

        attackButton.draw(gl, attackTexture);
    }
}
