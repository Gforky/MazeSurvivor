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
    char[][] maze;

    //All 3 parameters needed to draw the maze
    private MazeCell MazeWall;
    private final float[] wallColor =  new float[]{1.0f, 0.0f, 0.0f, 1.0f };

    private MazeCell MazePath;
    private final float[] pathColor = new float[]{0.0f, 1.0f, 0.0f, 1.0f};

    private MazeCell MazeSurvivor;
    private final float[] survivorColor = new float[]{0.0f, 0.0f, 1.0f, 1.0f};

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //Initialize the maze generator
        mazeGenerator = new GenerateRandomMaze();

        //Get the maze from generator
        maze = mazeGenerator.generateMaze(8, 8);//generate a M * N maze

        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //Set the vertices coordinates for all the cells in the maze
        MazeWall = new MazeCell(new float[]{
                -0.05f, 0.05f, 0.0f,//top left
                -0.05f, -0.05f, 0.0f,//bottom left
                0.05f, -0.05f, 0.0f,//bottom right
                0.05f, 0.05f, 0.0f}//top right
                );
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //draw the maze
        MazeWall.draw(gl, wallColor);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}