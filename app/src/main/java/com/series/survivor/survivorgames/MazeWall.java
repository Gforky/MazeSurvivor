package com.series.survivor.survivorgames;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/18/2015.
 * Wall Cell in the MazeSurvivor game
 * size of 2 * 2 pixels
 */
public class MazeWall {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;

    //number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float cellCoords[] = {
            -0.5f, 0.5f, 0.0f,//top left
            -0.5f, -0.5f, 0.0f,//bottom left
            0.5f, -0.5f, 0.0f,//bottom right
            0.5f, 0.5f, 0.0f};//top right
    //order to draw the vertices
    private final short drawOrder[] = {0, 1, 2, 0, 2, 3};

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    //Constructor
    public MazeWall() {

        //initialize vertex byte buffer for cell coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                //number of coordinate values * 4 bytes per float
                cellCoords.length * 4
        );
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cellCoords);
        vertexBuffer.position(0);

        //initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                //number of coordinate values * 2 bytes per short
                drawOrder.length * 2
        );
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    //wall cell's drawing logic
    public void draw(GL10 gl) {

        //This cell uses vertex array, enable them
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //Draw maze cell:
        gl.glColor4f(color[0], color[1], color[2], color[3]);//set the color

        gl.glVertexPointer(COORDS_PER_VERTEX,
                GL10.GL_FLOAT, 0,
                vertexBuffer);//point to the vertex data

        gl.glDrawElements(
                GL10.GL_TRIANGLES,
                drawOrder.length, GL10.GL_UNSIGNED_SHORT,
                drawListBuffer);

        //Disable vertex array drawing to avoid conflicts with shapes that don't use this
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
