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

    //Inner class to store the cell type and the vertices coordinates of the cell
    public static class Cell {
        public char Type;
        public MazeCell mazeCell;

        public Cell(char Type, MazeCell mazeCell) {
            this.Type = Type;
            this.mazeCell = mazeCell;
        }
    }

    private int maxCost;//record the global max cost from a path cell to the survivor
    private int[] exitCell = new int[2];//record the cell indices of exit in the maze
    private float ratio;//The screen's height and width ratio

    public Cell[][] generateMaze(int row, int col, float ratio, int startX, int startY) {//main function to generate the maze

        //Initialize the max cost as minimum int
        maxCost = Integer.MIN_VALUE;

        this.ratio = ratio;

        //Initialize the maze cells
        Cell[][] maze = new Cell[row][col];//store the cells in maze
        //get the upper left corner vertex's X and Y coordinates
        float leftMost = -1;
        float upMost = ratio;//set the height of the maze at the same size as width
        float sideLengthX = 2 / (float)row;//length of side of one cell at X coordinate
        float sideLengthY = sideLengthX * ratio;//length of side of one cell at Y coordinate

        //using char matrix to save space
        if(row < 1 || col < 1) {//Sanity check
            return null;
        }

        //initialize the maze
        float temp = leftMost;//temporally store the left most X coordinate
        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                //initialize the corresponding cells
                if(r == startX && c == startY) {//initially only set the start point as available path
                    maze[r][c] = new Cell('s', null);//p as "path"
                } else {//set all the other cells as walls
                    maze[r][c] = new Cell('w', null);//w as "wall"
                }
                //set the four vertices of the cell
                maze[r][c].mazeCell = new MazeCell(new float[]{
                        leftMost, upMost, 0.0f,//top left
                        leftMost, upMost - sideLengthY, 0.0f,//bottom left
                        leftMost + sideLengthX, upMost - sideLengthY, 0.0f,//bottom right
                        leftMost + sideLengthX, upMost, 0.0f//top right
                });
                leftMost += sideLengthX;//move to next column of the maze
            }
            upMost -= sideLengthY;//move to next row of the maze
            leftMost = temp;//reset column to the first column
        }
        generate(maze, startX, startY, 0);//generate a maze with the matrix and start point
        maze[exitCell[0]][exitCell[1]].Type = 'e';//set the exit with the largest cost to survivor
        return maze;
    }

    //Recursion: at each level, move the path two steps to a random direction, if valid
    private void generate(Cell[][] maze, int X, int Y, int localMax) {
        Dir[] dirs = Dir.values();//get the array of four directions
        shuffle(dirs);//shuffle the order of the directions, in order to move to random direction at each level
        for(Dir dir : dirs) {//try every direction, check whether can move to
            //move 2 steps
            int nextX = dir.moveX(dir.moveX(X));
            int nextY = dir.moveY(dir.moveY(Y));
            if(valid(maze, nextX, nextY)) {
                maze[dir.moveX(X)][dir.moveY(Y)].Type = 'p';
                maze[nextX][nextY].Type = 'p';
                generate(maze, nextX, nextY, localMax + 2);//continue generate the path from the new cell
            }else {//check whether the cell can be set as the exit, and temporarily record the indices of the cell
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
    private boolean valid(Cell[][] maze, int x, int y) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type == 'w') {//cell is with the maze, and is not be dig yet
            return true;
        }
        return false;
    }
}
