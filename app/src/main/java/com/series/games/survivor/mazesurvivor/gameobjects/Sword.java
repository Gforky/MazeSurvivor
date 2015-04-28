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
    public int attackMonster(int newX, int newY, MazeWorld.Cell[][] maze, Monster[] monsters) {
        //Return the number of monsters be kill in this time's attack
        int beKilledMonsters = 0;
        if(!outForAttack) {//Sword is not out for attack
            outForAttack = true;
            updateX(newX);
            updateY(newY);
            maze[indexX][indexY].Type = 'a';
            beKilledMonsters = inActive(SystemClock.uptimeMillis(), monsters);
            maze[indexX][indexY].Type = 'p';
            outForAttack = false;
            return beKilledMonsters;
        }
        return beKilledMonsters;
    }

    private int inActive(long startTime, Monster[] monsters) {//Sword in active for 0.1 seconds, return the number of monsters be killed
        long endTime = startTime;
        int beKilledMonsters = 0;
        while(endTime - startTime < 100L) {
            for(Monster monster : monsters) {
                if(monster != null && monster.isAlive && outForAttack && indexX == monster.getX() && indexY == monster.getY()) {
                    //Monster is killed by the player
                    monster.isAlive = false;
                    beKilledMonsters += 1;
                }
            }
            endTime = SystemClock.uptimeMillis();
        }
        return beKilledMonsters;
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
