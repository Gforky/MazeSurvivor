package com.series.survivor.survivorgames;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

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

    public MazeSurvivorRenderer(float ratio) {
        this.ratio = ratio;
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
        maze = mazeGenerator.generateMaze(40, 40, ratio);//generate a M * N maze

        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //draw the maze
        for(int r = 0; r < maze.length; r++) {
           for(int c = 0; c < maze[0].length; c++) {
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