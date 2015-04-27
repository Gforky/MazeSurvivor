package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/26/2015.
 * Timer to record the count down time left before next maze change
 */
public class CountDownTimer {

    private MazeCell firstDigit;
    private MazeCell secondDigit;
    private MazeCell thirdDigit;
    private MazeCell fourthDigit;

    public CountDownTimer(float ratio) {

        firstDigit = new MazeCell(new float[] {
                -1.0f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                -1.0f, -ratio,//bottom left
                -0.75f, -ratio,//bottom right
                -0.75f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });

        secondDigit = new MazeCell(new float[] {
                -0.75f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                -0.75f, -ratio,//bottom left
                -0.5f, -ratio,//bottom right
                -0.5f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });

        thirdDigit = new MazeCell(new float[] {
                -0.5f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                -0.5f, -ratio,//bottom left
                -0.25f, -ratio,//bottom right
                -0.25f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });

        fourthDigit = new MazeCell(new float[] {
                -0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//top left
                -0.25f, -ratio,//bottom left
                0.0f, -ratio,//bottom right
                0.0f, (-3.0f * ratio + 1.0f) / 2.0f//top right
        });
    }


    public void drawCountDownTime(GL10 gl, int[] numTextures, boolean gameNotOver, long timeLeft) {

        if(gameNotOver) {//Game is continuing, count down the time
            int first = (int) timeLeft / 1000;
            timeLeft = timeLeft % 1000;
            int second = (int) timeLeft / 100;
            timeLeft = timeLeft % 100;
            int third = (int) timeLeft / 10;
            int fourth = (int) timeLeft % 10;
            firstDigit.draw(gl, numTextures[first]);
            secondDigit.draw(gl, numTextures[second]);
            thirdDigit.draw(gl, numTextures[third]);
            fourthDigit.draw(gl, numTextures[fourth]);
        } else {//Game is over, set all four digits to 0
            firstDigit.draw(gl, numTextures[0]);
            secondDigit.draw(gl, numTextures[0]);
            thirdDigit.draw(gl, numTextures[0]);
            fourthDigit.draw(gl, numTextures[0]);
        }
    }
}
