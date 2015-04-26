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

        //Get a random order array of directions, in order to move to a random direction each time
        Dir[] dirs = Dir.values();
        shuffle(dirs);

        if(!inChange && systemTime - prevTime >= 300L){//1 or more second past, and the maze is not in change

            //Set the prev time to current system time
            prevTime = systemTime;

            for(Dir dir : dirs) {

                int nextX = dir.moveX(indexX);
                int nextY = dir.moveY(indexY);
                //Try to move the monster to a direction by 1 step, AVOID moving back to the position where it from
                if(valid(maze, nextX, nextY)) {
                    return;
                }
            }
            //Come into a corner, need to reset the last position parameter to get a direction to go out
            if (lastPosition != null) {
                lastPosition = null;
            }
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
    private boolean valid(MazeWorld.Cell[][] maze, int x, int y) {

        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type == 'p' &&
                (lastPosition == null || lastPosition[0] != x || lastPosition[1] != y)) {
            if (lastPosition == null) {
                lastPosition = new int[]{indexX, indexY};
            } else {
                lastPosition[0] = indexX;
                lastPosition[1] = indexY;
            }
            //Move to up side
            maze[indexX][indexY].Type = 'p';
            updateX(x);
            updateY(y);
            maze[indexX][indexY].Type = 'm';
            return true;
        }
        return false;
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
