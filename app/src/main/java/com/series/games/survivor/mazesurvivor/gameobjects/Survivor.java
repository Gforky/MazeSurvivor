package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Luke on 4/20/2015.
 * Survivor class that implements all the player's actions
 */
public class Survivor {

    //player's X and Y indices in maze
    private int indexX;
    private int indexY;

    public Survivor(int row, int col) {

        //Randomly set a start point in maze
        this.indexX = (int)(Math.random() * (row - 1));
        this.indexY = (int) (Math.random() * (col - 1));
    }

    /**Update the survivor position according to the touch event
     *
     * @param move
     */
    public boolean updateSurvivor(String move, MazeWorld.Cell[][] maze, boolean inChange) {
        int row = maze.length;
        int col = maze[0].length;
        boolean findExit = false;
        switch(move) {
            case "LEFT":
                int moveLeft = Dir.LEFT.moveY(indexY);
                if(!inChange && moveLeft >=0 && maze[indexX][moveLeft].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = 'p';
                    updateY(moveLeft);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    }
                    maze[indexX][indexY].Type = 's';
                }
                break;
            case "RIGHT":
                int moveRight = Dir.RIGHT.moveY(indexY);
                if(!inChange && moveRight < row && maze[indexX][moveRight].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = 'p';
                    updateY(moveRight);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    }
                    maze[indexX][indexY].Type = 's';
                }
                break;
            case "UP":
                int moveUp = Dir.UP.moveX(indexX);
                if(!inChange && moveUp >= 0 && maze[moveUp][indexY].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = 'p';
                    updateX(moveUp);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    }
                    maze[indexX][indexY].Type = 's';
                }
                break;
            case "DOWN":
                int moveDown = Dir.DOWN.moveX(indexX);
                if(!inChange && moveDown < col && maze[moveDown][indexY].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = 'p';
                    updateX(moveDown);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    }
                    maze[indexX][indexY].Type = 's';
                }
                break;
        }
        return findExit;
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
