package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Malvin on 4/18/2015.
 * Generate a random maze with size M * N (M and N are both even numbers)
 * the start point is the survivor's current position in the maze
 * for any cell in the maze, there is one and only one path between them
 * the exit is the cell with the max cost to the survivor
 * 'p' means available path, 'w' means walls, 's' means the survivor, 'e' means exit
 */
public class GenerateRandomMaze {

    private int maxCost;//record the global max cost from a path cell to the survivor
    private int[] exitCell = new int[2];//record the cell indices of exit in the maze
    private MazeWorld.Cell[][] maze;//Currently used maze

    public void generateMaze(MazeWorld.Cell[][] maze, int startX, int startY) {//main function to generate the maze

        //Initialize the maze
        this.maze = maze;

        //Initialize the max cost as minimum int
        maxCost = Integer.MIN_VALUE;

        generatePath(maze, startX, startY, 0);//generate a maze with the matrix and start point
        maze[exitCell[0]][exitCell[1]].Type = 'e';//set the exit with the largest cost to survivor
    }

    /**Recursion: at each level, move the path two steps to a random direction, if valid
     *
     * @param maze
     * @param X
     * @param Y
     * @param localMax
     */
    private void generatePath(MazeWorld.Cell[][] maze, int X, int Y, int localMax) {
        Dir[] dirs = Dir.values();//get the array of four directions
        shuffle(dirs);//shuffle the order of the directions, in order to move to random direction at each level
        for(Dir dir : dirs) {//try every direction, check whether can move to
            //move 2 steps
            int nextX = dir.moveX(dir.moveX(X));
            int nextY = dir.moveY(dir.moveY(Y));
            if(valid(maze, nextX, nextY)) {
                maze[dir.moveX(X)][dir.moveY(Y)].Type = 'p';
                maze[nextX][nextY].Type = 'p';
                generatePath(maze, nextX, nextY, localMax + 2);//continue generate the path from the new cell
            } else {//check whether the cell can be set as the exit, and temporarily record the indices of the cell
                if(localMax > maxCost) {//find a larger cost path to the survivor, set the cell as exit
                    //update the exit indices and global max cost
                    exitCell[0] = X;
                    exitCell[1] = Y;
                    maxCost = localMax;
                }
            }
        }//end-for
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

    //check if the destination cell is valid to move to
    private boolean valid(MazeWorld.Cell[][] maze, int x, int y) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type == 'w') {//cell is with the maze, and is not be dig yet
            return true;
        }
        return false;
    }

    //get the cost from exit to the survivor
    public int getMaxCost() {

        return maxCost;
    }
}
