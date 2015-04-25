package com.series.games.survivor.mazesurvivor.gameobjects;

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
    //Current maze's row and col
    private int row;
    private int col;

    public Monster(int indexX, int indexY, long createTime, int row, int col) {

        //Initializations
        this.indexX = indexX;
        this.indexY = indexY;
        prevTime = createTime;
        this.row = row;
        this.col = col;
    }

    /**Function to move the monster, move it to a direction by 1 step every second
     *
     * @param maze
     */
    public void move(MazeWorld.Cell[][] maze, long systemTime, boolean inChange) {

        int moveLeft = Dir.LEFT.moveY(indexY);
        int moveRight = Dir.RIGHT.moveY(indexY);
        int moveUp = Dir.UP.moveX(indexX);
        int moveDown = Dir.DOWN.moveX(indexX);

        if(!inChange && systemTime - prevTime >= 500L){//1 or more second past, and the maze is not in change

            //Set the prev time to current system time
            prevTime = systemTime;

            //Try to move the monster to a direction by 1 step, AVOID moving back to the position where it from
            if(moveLeft >= 0 && maze[indexX][moveLeft].Type == 'p' &&
                    (lastPosition == null || lastPosition[0] != indexX || lastPosition[1] != moveLeft)) {
                if(lastPosition == null) {
                    lastPosition = new int[]{indexX, indexY};
                } else {
                    lastPosition[0] = indexX;
                    lastPosition[1] = indexY;
                }
                //Move to left
                maze[indexX][indexY].Type = 'p';
                updateY(moveLeft);
                maze[indexX][indexY].Type = 'm';
                return;//avoid marching on the spot
            }
            if(moveUp >= 0 && maze[moveUp][indexY].Type == 'p' &&
                    (lastPosition == null || lastPosition[1] != indexY || lastPosition[0] != moveUp)) {
                if(lastPosition == null) {
                    lastPosition = new int[]{indexX, indexY};
                } else {
                    lastPosition[0] = indexX;
                    lastPosition[1] = indexY;
                }
                //Move to up side
                maze[indexX][indexY].Type = 'p';
                updateX(moveUp);
                maze[indexX][indexY].Type = 'm';
                return;//avoid marching on the spot
            }
            if(moveRight < row && maze[indexX][moveRight].Type == 'p' &&
                    (lastPosition == null || lastPosition[0] != indexX || lastPosition[1] != moveRight)) {
                if(lastPosition == null) {
                    lastPosition = new int[]{indexX, indexY};
                } else {
                    lastPosition[0] = indexX;
                    lastPosition[1] = indexY;
                }
                //Move to right
                maze[indexX][indexY].Type = 'p';
                updateY(moveRight);
                maze[indexX][indexY].Type = 'm';
                return;//avoid marching on the spot
            }
            if(moveDown < col && maze[moveDown][indexY].Type == 'p' &&
                    (lastPosition == null || lastPosition[1] != indexY || lastPosition[0] != moveDown)) {
                if(lastPosition == null) {
                    lastPosition = new int[]{indexX, indexY};
                } else {
                    lastPosition[0] = indexX;
                    lastPosition[1] = indexY;
                }
                //Move to down side
                maze[indexX][indexY].Type = 'p';
                updateX(moveDown);
                maze[indexX][indexY].Type = 'm';
                return;//avoid marching on the spot
            }
            //Come into a corner, need to reset the last position parameter to get a direction to go out
            if(lastPosition != null) {
                lastPosition = null;
            }
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
