package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Luke on 4/20/2015.
 * Survivor class that implements all the player's actions
 */
public class Survivor {

    //player's X and Y indices in maze
    private int indexX;
    private int indexY;

    public Survivor(int row, int col) {

        //Randomly set a start point in maze
        this.indexX = (int)(Math.random() * (row - 1));
        this.indexY = (int) (Math.random() * (col - 1));
    }

    public int getX() {

        return indexX;
    }

    public int getY() {

        return indexY;
    }

    public void updateX(int newX) {

        indexX = newX;
    }

    public void updateY(int newY) {
         indexY = newY;
    }
}
