package com.series.games.survivor.mazesurvivor.gameobjects;

import android.os.SystemClock;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/23/2015.
 * Game World contains MazeCells
 */
public class MazeWorld {

    //Inner class to store the cell type and the vertices coordinates of the cell
    public static class Cell {
        public char Type;
        public MazeCell mazeCell;

        public Cell(char Type, MazeCell mazeCell) {
            this.Type = Type;
            this.mazeCell = mazeCell;
        }
    }

    //cell array contains all the maze cells used in current maze
    private Cell[][] maze;
    private int row;
    private int col;
    private float ratio;

    //four control buttons
    private DirButtons dirButtons;

    //Maze generator to create the random mazes
    private GenerateRandomMaze mazeGenerator;
    private Monster[] monsters;
    private Trap[] traps;

    //Game player
    public Survivor survivor;

    public MazeWorld(int row, int col, float ratio) {

        //Initialize the maze cells
        this.survivor = new Survivor(row, col);;
        this.row = row;
        this.col = col;
        this.ratio = ratio;

        //Initialize the maze generator
        mazeGenerator = new GenerateRandomMaze();

        //Initialize the direction buttons
        dirButtons = new DirButtons(ratio);
    }

    public Cell[][] generateMaze() {

        maze = new Cell[row][col];//store the cells in maze

        //get the upper left corner vertex's X and Y coordinates
        //set the screen's top left corner as the start point
        float leftMost = -1;
        float upMost = 1;
        float sideLengthX = 2 / (float)row;//length of side of one cell at X coordinate
        float sideLengthY = sideLengthX * ratio;//length of side of one cell at Y coordinate, length is as same as sideLengthX

        //using char matrix to save space
        if(row < 1 || col < 1) {//Sanity check
            return null;
        }

        //Initialize the maze
        float temp = leftMost;//temporally store the left most X coordinate
        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                //initialize the corresponding cells
                if(r == survivor.getX() && c == survivor.getY()) {//initially only set the start point as available path
                    maze[r][c] = new Cell('s', null);//p as "path"
                } else {//set all the other cells as walls
                    maze[r][c] = new Cell('w', null);//w as "wall"
                }
                //set the four vertices of the cell
                maze[r][c].mazeCell = new MazeCell(new float[]{
                        leftMost, upMost,//top left
                        leftMost, upMost - sideLengthY,//bottom left
                        leftMost + sideLengthX, upMost - sideLengthY,//bottom right
                        leftMost + sideLengthX, upMost//top right
                });
                leftMost += sideLengthX;//move to next column of the maze
            }
            upMost -= sideLengthY;//move to next row of the maze
            leftMost = temp;//reset column to the first column
        }

        mazeGenerator.generateMaze(maze, survivor.getX(), survivor.getY(), 16, 16);
        monsters = mazeGenerator.getMonsters();
        traps = mazeGenerator.getTraps();
        return maze;
    }

    public Cell[][] changeMaze() {

        resetMaze();
        generateMaze();
        return maze;
    }

    public void updatePlayer(int newX, int newY) {

        survivor.updateX(newX);
        survivor.updateY(newY);
    }

    public void updateMaze(int newRow, int newCol) {

        row = newRow;
        col = newCol;
    }

    public void resetMaze() {

        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                if(maze[r][c].Type == 'p' || maze[r][c].Type == 'e') {
                    maze[r][c].Type = 'w';
                }
            }
        }
    }

    /**Function to draw the maze
     *
     * @param gl
     */
    public void drawMaze(GL10 gl, int wallTexture, int pathTexture, int survivorTexture, int activeTrapTexture,
                         int bonusTimeTexture, int exitTexture, int monsterTexture, int swordTexture,
                         boolean inChange, boolean survivorIsAlive) {
        //Update the monsters' positions, if game is still continuing
        if(survivorIsAlive) {
            updateMonsters(inChange);
        }
        //update the traps' status
        updateTraps();

        //draw maze function
        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                if(maze[r][c].Type == 'w') {//draw the wall cell
                    maze[r][c].mazeCell.draw(gl, wallTexture);
                } else if(maze[r][c].Type == 'p') {//draw the path cell
                    maze[r][c].mazeCell.draw(gl, pathTexture);
                } else if(maze[r][c].Type == 's') {//draw the survivor
                    maze[r][c].mazeCell.draw(gl, survivorTexture);
                } else if(maze[r][c].Type == 'e') {//draw the exit
                    maze[r][c].mazeCell.draw(gl, exitTexture);
                } else if(maze[r][c].Type == 'm') {//draw monsters
                    maze[r][c].mazeCell.draw(gl, monsterTexture);
                } else if(maze[r][c].Type == 'a') {//draw the sword in attack
                    maze[r][c].mazeCell.draw(gl, swordTexture);
                } else if(maze[r][c].Type == 't') {//draw the trap
                    if(mazeGenerator.getTrap(r, c).getActiveStatus()) {//Trap is active
                        maze[r][c].mazeCell.draw(gl, activeTrapTexture);
                    } else {//Trap is not active
                        maze[r][c].mazeCell.draw(gl, pathTexture);
                    }
                } else if(maze[r][c].Type == 'b') {//draw the bonus time
                    maze[r][c].mazeCell.draw(gl, bonusTimeTexture);
                }
            }
        }
    }

    /**Update the monsters' positions
     *
     * @param inChange
     */
    private void updateMonsters(boolean inChange) {

        checkMonsterIsAlive();
        for(Monster monster : mazeGenerator.getMonsters()) {
            if(monster != null && monster.isAlive) {//move the monster if it is alive
                monster.move(maze, SystemClock.uptimeMillis(), inChange, survivor);
            }
        }
    }

    /**Function to check the traps' status
     *
     */
    private void updateTraps() {

        for(Trap trap : traps) {
            if(trap != null) {
                trap.updateTrap(SystemClock.uptimeMillis());
            }
        }
    }

    /**Function to check whether the player is killed by the monster
     *
     * @return
     */
    public boolean checkIfGameOver() {

        for(Monster monster : mazeGenerator.getMonsters()) {
            if(monster != null && monster.isAlive && monster.getX() == survivor.getX() && monster.getY() == survivor.getY()) {
            //Player is killed by the monster
                survivor.isAlive = false;
                return true;
            }
        }
        for(Trap trap : traps) {
            if(trap != null && trap.getActiveStatus() && trap.getX() == survivor.getX() && trap.getY() == survivor.getY()) {
                survivor.isAlive = false;
                return true;
            }
        }
        return false;
    }


    /**Function to check the survival status of monsters
     *
     * @return
     */
    public void checkMonsterIsAlive() {

        for(Monster monster : monsters) {
            if(monster != null) {
                monster.checkIfAlive(survivor.sword);
            }
        }
    }

    public Cell[][] getMaze() {

        return maze;
    }

    public DirButtons getDirButtons() {

        return dirButtons;
    }

    public int getMaxCost() {

        return mazeGenerator.getMaxCost();
    }

    public Monster[] getMonsters() {

        return monsters;
    }
}
