package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/27/2015.
 */
public class BonusTime {

    private int indexX;
    private int indexY;
    private boolean pickedBySurvivor;//Record the status of picking up by the player

    public BonusTime(int indexX, int indexY) {

        this.indexX = indexX;
        this.indexY = indexY;
        pickedBySurvivor = false;
    }
}
