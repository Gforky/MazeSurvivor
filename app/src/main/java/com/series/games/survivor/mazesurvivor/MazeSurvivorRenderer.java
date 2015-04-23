package com.series.games.survivor.mazesurvivor;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.series.games.survivor.mazesurvivor.gameobjects.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorRenderer implements GLSurfaceView.Renderer {

    //Storage to store the cell information in the maze, 'w' mean wall, 'p' means path, 's' means survivor
    MazeWorld mazeWorld;
    MazeWorld.Cell[][] maze;

    //The screen's height and width ratio
    private float ratio;

    //Set the startTime of a maze
    private long startTime;

    //numbers of row and column of the maze
    int row;
    int col;

    //Player object
    private Survivor survivor;

    //Direction buttons
    private DirButtons dirButtons;

    //record the status of the player, if the player find the exit, set it to true
    private boolean findExit;

    //boolean flag to record whether the maze is in change or not
    private boolean inChange;

    //Constructor
    public MazeSurvivorRenderer(float ratio, int row, int col) {
        //Initializations
        this.ratio = ratio;

        //control the corner case as the player enter 0 as initial size
        this.row = row == 0 ? 4 : row;
        this.col = col == 0 ? 4 : col;
        survivor = new Survivor(row, col);
        findExit = false;
        inChange = false;

        //Initialize the maze
        mazeWorld = new MazeWorld(row, col, ratio, survivor.getX(), survivor.getY());

        //Get the maze from generator
        maze = mazeWorld.generateMaze();//generate a M * N maze

        //Initialize direction buttons
        dirButtons = mazeWorld.getDirButtons();

        //set the game start time for current round
        startTime = SystemClock.uptimeMillis();
    }

    //All 3 colors needed to draw the maze
    private final float[] wallColor =  new float[]{1.0f, 0.0f, 0.0f, 1.0f };

    private final float[] pathColor = new float[]{0.0f, 1.0f, 0.0f, 1.0f};

    private final float[] survivorColor = new float[]{0.0f, 0.0f, 1.0f, 1.0f};

    private final float[] exitColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //draw the maze and direction buttons
        drawMaze(gl);
        drawButtons(gl);

        if(findExit) {//player run out of current maze, create a new maze for the player
            findExit = false;//set the boolean flag to false
            inChange = true;//freeze the action of the player, avoid sending wrong player's position to the maze generator

            //enlarge the maze's size to increase the difficulty of the game
            row += 2;
            col += 2;

            //reset the player and re-generate the maze
            survivor = new Survivor(row, col);

            //re-generate the maze
            mazeWorld.updateMaze(row, col);//update the row and column of the maze
            mazeWorld.updatePlayer(survivor.getX(), survivor.getY());//set the player at a new start position
            maze = mazeWorld.generateMaze();

            startTime = SystemClock.uptimeMillis();//reset the start time

            inChange = false;//free the player
        } else if((SystemClock.uptimeMillis() - startTime) / (1000L * mazeWorld.getMaxCost() / 5) > 0) {

            //player still in current maze, change the maze around every (maze row) seconds
            inChange = true;//freeze the action of the player, avoid sending wrong player's position to the maze generator
            startTime = SystemClock.uptimeMillis();

            //update the player's position, avoid changing the maze with the previous player's position
            mazeWorld.updatePlayer(survivor.getX(), survivor.getY());//set the player at a new start position
            maze = mazeWorld.changeMaze();//change the maze
            inChange = false;//free the player
        }
    }

    private void drawMaze(GL10 gl) {//draw maze function

        for(int r = 0; r < row; r++) {
            for(int c = 0; c < col; c++) {
                if(maze[r][c].Type == 'w') {//draw the wall cell
                    maze[r][c].mazeCell.draw(gl, wallColor);
                } else if(maze[r][c].Type == 'p') {//draw the path cell
                    maze[r][c].mazeCell.draw(gl, pathColor);
                } else if(maze[r][c].Type == 's') {//draw the survivor
                    maze[r][c].mazeCell.draw(gl, survivorColor);
                } else if(maze[r][c].Type == 'e') {//draw the exit
                    maze[r][c].mazeCell.draw(gl, exitColor);
                }
            }
        }
    }

    private void drawButtons(GL10 gl) {//draw operation buttons
        dirButtons.leftButton.draw(gl, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        dirButtons.rightButton.draw(gl, new float[]{1.0f, 1.0f, 0.0f, 1.0f});
        dirButtons.upButton.draw(gl, new float[]{1.0f, 0.0f, 1.0f, 1.0f});
        dirButtons.downButton.draw(gl, new float[]{0.0f, 1.0f, 1.0f, 1.0f});
    }

    //update the survivor position according to the touch event
    public void updateSurvivor(String move) {
        switch(move) {
            case "LEFT":
                int moveLeft = Dir.LEFT.moveY(survivor.getY());
                if(!inChange && moveLeft >=0 && maze[survivor.getX()][moveLeft].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    if(maze[survivor.getX()][moveLeft].Type == 'e') {
                        findExit = true;
                    }
                    maze[survivor.getX()][survivor.getY()].Type = 'p';
                    survivor.updateY(moveLeft);
                    maze[survivor.getX()][survivor.getY()].Type = 's';
                }
                break;
            case "RIGHT":
                int moveRight = Dir.RIGHT.moveY(survivor.getY());
                if(!inChange && moveRight < row && maze[survivor.getX()][moveRight].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    if(maze[survivor.getX()][moveRight].Type == 'e') {
                        findExit = true;
                    }
                    maze[survivor.getX()][survivor.getY()].Type = 'p';
                    survivor.updateY(moveRight);
                    maze[survivor.getX()][survivor.getY()].Type = 's';
                }
                break;
            case "UP":
                int moveUp = Dir.UP.moveX(survivor.getX());
                if(!inChange && moveUp >= 0 && maze[moveUp][survivor.getY()].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    if(maze[moveUp][survivor.getY()].Type == 'e') {
                        findExit = true;
                    }
                    maze[survivor.getX()][survivor.getY()].Type = 'p';
                    survivor.updateX(moveUp);
                    maze[survivor.getX()][survivor.getY()].Type = 's';
                }
                break;
            case "DOWN":
                int moveDown = Dir.DOWN.moveX(survivor.getX());
                if(!inChange && moveDown < col && maze[moveDown][survivor.getY()].Type != 'w') {
                    //maze is not in change, and can move to the new cell
                    if(maze[moveDown][survivor.getY()].Type == 'e') {
                        findExit = true;
                    }
                    maze[survivor.getX()][survivor.getY()].Type = 'p';
                    survivor.updateX(moveDown);
                    maze[survivor.getX()][survivor.getY()].Type = 's';
                }
                break;
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}