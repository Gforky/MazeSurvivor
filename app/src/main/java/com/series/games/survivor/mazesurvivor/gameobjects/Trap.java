package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/27/2015.
 * Trap in the maze, active every 3 seconds
 */
public class Trap {

    //Trap's coordinates
    private int indexX;
    private int indexY;
    //Record the last check time, when check the trap's status
    private long lastChangeTime;
    //The active status of the trap
    private boolean inActive;

    public Trap(int indexX, int indexY, long startTime) {

        this.indexX = indexX;
        this.indexY = indexY;
        lastChangeTime = startTime;
        inActive = false;
    }

    /**Function to check whether the trap is in active or not
     *
     * @param currentTime
     */
    public void updateTrap(long currentTime) {

        if(!inActive && currentTime - lastChangeTime >= 3000L) {
            inActive = true;
            lastChangeTime = currentTime;//Set the end time as last active time
        } else if(inActive && currentTime - lastChangeTime >= 1000L) {
            inActive = false;
            lastChangeTime = currentTime;//Set the end time as last active time
        }
    }

    public boolean getActiveStatus() {

        return inActive;
    }

    public int getX() {

        return indexX;
    }

    public int getY() {

        return indexY;
    }
}
