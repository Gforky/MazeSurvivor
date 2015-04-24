package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/23/2015.
 * Scoreboard that record and display the current level of the game
 */
public class ScoreBoard {

    //Three display areas for the scoreboard
    public MazeCell lvSymbol;//'LV' symbol
    public MazeCell firstDigit;//display the higher digit
    public MazeCell secondDigit;//display the lower digit

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
}
