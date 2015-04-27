package com.series.games.survivor.mazesurvivor.gameobjects;

import android.os.SystemClock;

/**
 * Created by Luke on 4/25/2015.
 * Sword used by the player
 */
public class Sword {

    private int indexX;
    private int indexY;
    public boolean outForAttack;

    public Sword(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
        outForAttack = false;
    }

    /**Function to use the sword to attack
     *
     * @param newX
     * @param newY
     */
    public void attackMonster(int newX, int newY, MazeWorld.Cell[][] maze, Monster[] monsters) {
        if(!outForAttack) {//Sword is not out for attack
            outForAttack = true;
            updateX(newX);
            updateY(newY);
            maze[indexX][indexY].Type = 'a';
            inActive(SystemClock.uptimeMillis(), monsters);
            maze[indexX][indexY].Type = 'p';
            outForAttack = false;
        }
    }

    private void inActive(long startTime, Monster[] monsters) {//Sword in active for 0.1 seconds
        long endTime = startTime;
        while(endTime - startTime < 100L) {
            for(Monster monster : monsters) {
                if(monster != null && outForAttack && indexX == monster.getX() && indexY == monster.getY()) {
                    //Monster is killed by the player
                    monster.isAlive = false;
                }
            }
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
