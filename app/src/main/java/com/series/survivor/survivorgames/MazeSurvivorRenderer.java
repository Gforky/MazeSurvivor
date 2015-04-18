package com.series.survivor.survivorgames;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorRenderer implements GLSurfaceView.Renderer {

    private MazeWall mMazeWall;

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mMazeWall = new MazeWall();
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //draw the maze
        mMazeWall.draw(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}