package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/18/2015.
 * Store the constants of four move directions:Up, Down, Left, Right
 */
public enum Dir {
    //move up, down, left, right, one step
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
    //number of cells for one step's move
    int deltaX;
    int deltaY;

    Dir(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int moveX(int x) {//return the destination X after one step's move
        return x + deltaX;
    }

    public int moveY(int y) {//return the destination Y after one step's move
        return y + deltaY;
    }
}
