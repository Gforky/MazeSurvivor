package com.series.survivor.survivorgames;

/**
 * Created by Malvin on 4/18/2015.
 * Generate a random maze with size M * N (M and N are both even numbers)
 * for any cell in the maze, there is one and only one path between them
 * 'p' means available path, 'w' means walls
 */
public class GenerateRandomMaze {

    //Inner class to store the cell type and the vertices coordinates of the cell
    static class Cell {
        public char Type;
        public float[] coords;

        public Cell(char Type, float[] coords) {
            this.Type = Type;
            this.coords = coords;
        }
    }

    public Cell[][] generateMaze(int row, int col) {

        //Initialize the maze cells
        Cell[][] maze = new Cell[row][col];//store the cells in maze
        //get the upper left corner vertex's X and Y coordinates
        float leftMost = -((row / 2) * 0.01f);
        float upMost = (col / 2) * 0.01f;

        //using char matrix to save space
        if(row < 1 || col < 1) {//Sanity check
            return null;
        }
        //Randomly choose a start point in maze
        int startX = (int)(Math.random() * (row - 1));
        int startY = (int) (Math.random() * (col - 1));

        //initialize the maze
        float temp = leftMost;//temporally store the left most X coordinate
        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                //initialize the corresponding cells
                if(r == startX && c == startY) {//initially only set the start point as available path
                    maze[r][c] = new Cell('p', null);//p as "path"
                } else {//set all the other cells as walls
                    maze[r][c] = new Cell('w', null);//w as "wall"
                }
                //set the four vertices of the cell
                maze[r][c].coords = new float[]{
                        leftMost, upMost, 0.0f,//top left
                        leftMost, upMost - 0.02f, 0.0f,//bottom left
                        leftMost + 0.02f, upMost - 0.02f, 0.0f,//bottom right
                        leftMost + 0.02f, upMost, 0.0f//top right
                };
                leftMost += 0.02f;//move to next column of the maze
            }
            upMost -= 0.02f;//move to next row of the maze
            leftMost = temp;
        }
        generate(maze, startX, startY);//generate a maze with the matrix and start point
        return maze;
    }

    //Recursion: at each level, move the path two steps to a random direction, if valid
    private void generate(Cell[][] maze, int X, int Y) {
        Dir[] dirs = Dir.values();//get the array of four directions
        shuffle(dirs);//shuffle the order of the directions, in order to move to random direction at each level
        for(Dir dir : dirs) {//try every direction, check whether can move to
            //move 2 steps
            int nextX = dir.moveX(dir.moveX(X));
            int nextY = dir.moveY(dir.moveY(Y));
            if(valid(maze, nextX, nextY)) {
                maze[dir.moveX(X)][dir.moveY(Y)].Type = 'p';
                maze[nextX][nextY].Type = 'p';
                generate(maze, nextX, nextY);//continue generate the path from the new cell
            }//end-if
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
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y].Type != 'p') {//cell is with the maze, and is not be dig yet
            return true;
        }
        return false;
    }
}
