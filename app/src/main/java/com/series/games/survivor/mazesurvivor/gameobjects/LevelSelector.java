package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/28/2015.
 * Set Maze size, number of monsters and traps, according to the game level
 */
public class LevelSelector {

    private int mazeSize;
    private int numOfMonsters;
    private int numOfTraps;

    public void updateSelector(int level) {

        if(level >= 1 && level <= 7) {
            //Level 1-10, maze size 4-10
            mazeSize = level + 3;
        } else if(level >= 8 && level <= 35) {
            //Level 8-35, maze size 11-24, increase by 1 every 2 levels,
            // numOfMonster and numOfTraps 2 - 14, increase by 2 every 2 levels
            mazeSize = 11 + (level - 8) / 2;
            numOfMonsters = 2 * ((level - 8) / 2 + 1);
            numOfTraps = numOfMonsters;
        } else if(level >= 36 && level <= 50) {
            //Level 36-50, maze size 25-29, increase by 1 every 3 levels,
            //numOfMonster and numOfTraps 16
            mazeSize = 25 + (level - 36) / 3;
            numOfMonsters = 16;
            numOfTraps = 16;
        } else if(level > 50) {
            //Level 51 - 99
            mazeSize = 30;
            numOfMonsters = 16;
            numOfTraps = 16;
        }
    }

    public int getMazeSize() {

        return mazeSize;
    }

    public int getNumOfMonsters() {

        return numOfMonsters;
    }

    public int getNumOfTraps() {

        return  numOfTraps;
    }
}
