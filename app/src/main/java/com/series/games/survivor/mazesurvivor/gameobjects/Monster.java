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
    //Survival status of the monster
    public boolean isAlive;

    public Monster(int indexX, int indexY, long createTime) {

        //Initializations
        this.indexX = indexX;
        this.indexY = indexY;
        prevTime = createTime;
        isAlive = true;
    }

    /**Function to move the monster, move it to a direction by 1 step every second
     * each time the monster will check whether it's killed by the player
     * or find the player and kill the player
     *
     * @param maze
     */
    public void move(MazeWorld.Cell[][] maze, long systemTime, boolean inChange, Survivor survivor) {

        checkIfAlive(survivor.sword);
        //Get a random order array of directions, in order to move to a random direction each time
        Dir[] dirs = Dir.values();
        shuffle(dirs);

        if(isAlive && !inChange && systemTime - prevTime >= 500L){//0.5 or more second past, and the maze is not in change

            //Set the prev time to current system time
            prevTime = systemTime;

            for(Dir dir : dirs) {

                int nextX = dir.moveX(indexX);
                int nextY = dir.moveY(indexY);
                //Try to move the monster to a direction by 1 step, AVOID moving back to the position where it from
                if(maze[indexX][indexY].Type == 'a') {//Monster is killed by the player
                    isAlive = false;
                    maze[indexX][indexY].Type = 'p';
                    return;
                } else if(valid(maze, nextX, nextY, survivor)) {
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
    private boolean valid(MazeWorld.Cell[][] maze, int x, int y, Survivor survivor) {

        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type != 'w' && maze[x][y].Type != 'e' &&
                (lastPosition == null || lastPosition[0] != x || lastPosition[1] != y)) {
            if (lastPosition == null) {
                lastPosition = new int[]{indexX, indexY};
            } else {
                lastPosition[0] = indexX;
                lastPosition[1] = indexY;
            }
            //Move to the new cell
            maze[indexX][indexY].Type = 'p';
            updateX(x);
            updateY(y);
            //check whether the monster is killed
            if(checkIfAlive(survivor.sword)) {
                maze[indexX][indexY].Type = 'm';
                return true;
            }
        }
        return false;
    }

    /**Function to check the survival status of the monster
     *
     */
    public boolean checkIfAlive(Sword sword) {

        if(isAlive && sword.outForAttack && indexX == sword.getX() && indexY == sword.getY()) {
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

    public void updateX(int newX) {

        indexX = newX;
    }

    public void updateY(int newY) {

        indexY = newY;
    }
}
