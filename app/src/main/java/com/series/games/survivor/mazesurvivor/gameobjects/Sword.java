package com.series.games.survivor.mazesurvivor.gameobjects;

import android.os.SystemClock;

/**
 * Created by Luke on 4/25/2015.
 * Sword used by the player
 */
public class Sword {

    private int indexX;
    private int indexY;

    public Sword(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    /**Function to use the sword to attack
     *
     * @param newX
     * @param newY
     */
    public void attackMonster(int newX, int newY, MazeWorld.Cell[][] maze) {
        updateX(newX);
        updateY(newY);
        maze[indexX][indexY].Type = 'a';
        inActive(SystemClock.uptimeMillis());
        maze[indexX][indexY].Type = 'p';
    }

    private void inActive(long startTime) {//Sword in active for 0.5 seconds
        long endTime = startTime;
        while(endTime - startTime < 100L) {
            endTime = SystemClock.uptimeMillis();
        }
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
