package com.series.games.survivor.mazesurvivor.gameobjects;

import android.os.SystemClock;

/**
 * Created by Malvin on 4/24/2015.
 * Monster move in the maze
 */
public class Monster {

    //Monster's position in the maze
    private int indexX;
    private int indexY;
    //Last system time when the monster created, moved, or checked
    private long prevTime;
    //Record the last relative position to the current position
    private int[] lastPosition;
    //Survival status of the monster
    public boolean isAlive;
    //Record the previous type of monster's current cell
    public char prevType;
    //The orientation of the monster
    public String orientation;
    //Record the previous texture change time
    private long prevTextureChangeTime;
    //Record the Texture ID
    private int textureId;

    public Monster(int indexX, int indexY, long createTime) {

        //Initializations
        this.indexX = indexX;
        this.indexY = indexY;
        prevTime = createTime;
        prevTextureChangeTime = createTime;
        isAlive = true;
        prevType = 'p';
        orientation = "DOWN";
        textureId = 0;
    }

    /**Function to move the monster, move it to a direction by 1 step every second
     * each time the monster will check whether it's killed by the player
     * or find the player and kill the player
     *
     * @param maze
     */
    public boolean move(MazeWorld.Cell[][] maze, long systemTime, Survivor survivor) {
        //return value is to the survival status of the monster
        checkIfAlive(survivor.fireAttack);
        //Get a random order array of directions, in order to move to a random direction each time
        Dir[] dirs = Dir.values();
        shuffle(dirs);

        if(isAlive && systemTime - prevTime >= 500L){//0.5 or more second past, and the maze is not in change

            //Set the prev time to current system time
            prevTime = systemTime;

            for(Dir dir : dirs) {
                int nextX = dir.moveX(indexX);
                int nextY = dir.moveY(indexY);
                //Try to move the monster to a direction by 1 step, AVOID moving back to the position where it from
                if(maze[indexX][indexY].Type == 'a') {//Monster is killed by the player
                    isAlive = false;
                    maze[indexX][indexY].Type = 'p';
                    return false;
                } else if(valid(maze, nextX, nextY, survivor)) {
                    switch (dir) {//update the orientation of the monster

                        case UP:
                            orientation = "UP";
                            break;
                        case DOWN:
                            orientation = "DOWN";
                            break;
                        case  LEFT:
                            orientation = "LEFT";
                            break;
                        case RIGHT:
                            orientation = "RIGHT";
                            break;
                    }
                    return isAlive;
                }
            }
            //Come into a corner, need to reset the last position parameter to get a direction to go out
            if (lastPosition != null) {
                lastPosition = null;
            }
        }
        if(isAlive) {
            return true;
        } else {
            return false;
        }
    }

    //shuffle the direction array to get a random order directions
    private void shuffle(Dir[] dirs) {
        for(int index = 0; index < dirs.length; index++) {
            int random = (int)(Math.random() * index);
            //swap the current direction with the randomly chose direction
            Dir temp = dirs[random];
            dirs[random] = dirs[index];
            dirs[index] = temp;
        }
    }

    //check whether the monster can move to the new cell
    private boolean valid(MazeWorld.Cell[][] maze, int x, int y, Survivor survivor) {

        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type != 'w' && maze[x][y].Type != 'e' &&
                maze[x][y].Type != 't' && maze[x][y].Type != 'm' && (lastPosition == null || lastPosition[0] != x || lastPosition[1] != y)) {
            if (lastPosition == null) {
                lastPosition = new int[]{indexX, indexY};
            } else {
                lastPosition[0] = indexX;
                lastPosition[1] = indexY;
            }
            //Move to the new cell
            maze[indexX][indexY].Type = prevType;
            //Avoid creating duplicate survivors, and fire balls
            if(maze[x][y].Type == 's') {
                prevType = survivor.getPrevType();
            } else if(maze[x][y].Type == 'a') {
                prevType = survivor.fireAttack.prevType;
            } else {
                prevType = maze[x][y].Type;
            }
            updateX(x);
            updateY(y);
            //check whether the monster is killed
            if(checkIfAlive(survivor.fireAttack)) {
                maze[indexX][indexY].Type = 'm';
            }
            return true;//no matter be killed or not, moved to the new cell
        }
        return false;
    }

    /**Function to check the survival status of the monster
     *
     */
    public boolean checkIfAlive(FireAttack fireAttack) {

        if(isAlive && fireAttack.outForAttack && indexX == fireAttack.getX() && indexY == fireAttack.getY()) {
            isAlive = false;
            return false;
        }
        return true;
    }

    public int getX() {

        return indexX;
    }

    public int getY() {

        return indexY;
    }

    /**Function to update the texture to display, in order to make animation
     *
     * @return
     */
    public int getTextureId() {

        if(SystemClock.uptimeMillis() - prevTextureChangeTime >= 150L) {
            prevTextureChangeTime = SystemClock.uptimeMillis();
            textureId = textureId < 2 ? textureId + 1 : 0;
        }
        return textureId;
    }

    public void updateX(int newX) {

        indexX = newX;
    }

    public void updateY(int newY) {

        indexY = newY;
    }
}
