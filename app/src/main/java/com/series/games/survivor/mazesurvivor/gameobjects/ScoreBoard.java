package com.series.games.survivor.mazesurvivor.gameobjects;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/23/2015.
 * Scoreboard that record and display the current level of the game
 */
public class ScoreBoard {

    //Three display areas for the scoreboard
    private MazeCell lvSymbol;//'LV' symbol
    private MazeCell firstDigit;//display the higher digit
    private MazeCell secondDigit;//display the lower digit

    public ScoreBoard(float ratio) {

        lvSymbol = new MazeCell(new float[]{
                -1.0f, -2.0f * ratio + 1.0f,//top left
                -1.0f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom left
                -0.5f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom right
                -0.5f, -2.0f * ratio + 1.0f//top right
        });

        firstDigit = new MazeCell(new float[]{
                -0.5f, -2.0f * ratio + 1.0f,//top left
                -0.5f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom left
                -0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom right
                -0.25f, -2.0f * ratio + 1.0f//top right
        });

        secondDigit = new MazeCell(new float[]{
                -0.25f, -2.0f * ratio + 1.0f,//top left
                -0.25f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom left
                0.0f, (-3.0f * ratio + 1.0f) / 2.0f,//bottom right
                0.0f, -2.0f * ratio + 1.0f//top right
        });
    }

    /**Function to draw the ScoreBoard
     *
     * @param gl
     */
    public void drawScoreBoard(GL10 gl, int lvSymbolTexture, int[] numTextures, int gameLevel) {

        lvSymbol.draw(gl, lvSymbolTexture);
        firstDigit.draw(gl, numTextures[gameLevel / 10]);
        secondDigit.draw(gl, numTextures[gameLevel % 10]);
    }
}
