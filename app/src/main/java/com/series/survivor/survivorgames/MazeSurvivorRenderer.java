package com.series.survivor.survivorgames;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorRenderer implements GLSurfaceView.Renderer {

    //The maze generator
    GenerateRandomMaze mazeGenerator;

    //Storage to store the cell information in the maze, 'w' mean wall, 'p' means path, 's' means survivor
    GenerateRandomMaze.Cell[][] maze;

    //The screen's height and width ratio
    private float ratio;

    //The Calendar instance to get the system time
    private Calendar calendar;

    //Set the startTime of a maze
    long startTime;

    //start point in maze
    int startX;
    int startY;

    //numbers of row and column of the maze
    int row;
    int col;

    //Constructor
    public MazeSurvivorRenderer(float ratio, int row, int col) {
        //Initializations
        this.ratio = ratio;
        calendar = Calendar.getInstance();
        startTime = SystemClock.uptimeMillis();
        this.row = row;
        this.col = col;
        //Randomly set a start point in maze
        startX = (int)(Math.random() * (row - 1));
        startY = (int) (Math.random() * (col - 1));
    }

    //All 3 colors needed to draw the maze
    private final float[] wallColor =  new float[]{1.0f, 0.0f, 0.0f, 1.0f };

    private final float[] pathColor = new float[]{0.0f, 1.0f, 0.0f, 1.0f};

    private final float[] survivorColor = new float[]{0.0f, 0.0f, 1.0f, 1.0f};

    private final float[] exitColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //Initialize the maze generator
        mazeGenerator = new GenerateRandomMaze();

        //Get the maze from generator
        maze = mazeGenerator.generateMaze(row, col, ratio, startX, startY);//generate a M * N maze

        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        drawMaze(gl);

        if((SystemClock.uptimeMillis() - startTime) / 5000L > 0) {//change the maze around every 10 seconds
            startTime = SystemClock.uptimeMillis();
            maze = mazeGenerator.generateMaze(row, col, ratio, startX, startY);
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

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}