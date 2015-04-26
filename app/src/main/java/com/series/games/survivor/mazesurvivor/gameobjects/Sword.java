package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Luke on 4/25/2015.
 * Sword used by the player
 */
public class Sword {

    private int indexX;
    private int indexY;
    private boolean outForAttack;//Status of the sword

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
    public void attackIt(int newX, int newY) {
        outForAttack = true;
        indexX = newX;
        indexY = newY;
        inActive();

    }

    public int getX() {
        return indexX;
    }

    public int getY() {
        return indexY;
    }

    private void updateX(int newX) {
        indexX = newX;
    }

    private void updateY(int newY) {
        indexY = newY;
    }
}
