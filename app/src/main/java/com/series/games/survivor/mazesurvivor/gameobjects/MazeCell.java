package com.series.games.survivor.mazesurvivor.gameobjects;

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
public class MazeCell {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final FloatBuffer textureBuffer;

    //number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    //number of coordinates per texture
    static final int COORDS_PER_TEXTURE = 2;
    static float cellCoords[];//stores the four vertices of the cell
    //order to draw the vertices
    private final short drawOrder[] = new short[]{0, 1, 2, 0, 2, 3};
    //UV coordinates
    private final float[] uvCoords = new float[]{
            // Mapping coordinates for the vertices
            0.0f, 0.0f,     // top left
            0.0f, 1.0f,     // bottom left
            1.0f, 1.0f,     // top right
            1.0f, 0.0f      // bottom right

    };

    //Constructor
    public MazeCell(float[] cellCoords) {

        this.cellCoords = cellCoords;

        //initialize vertex byte buffer for cell coordinates
        ByteBuffer vb = ByteBuffer.allocateDirect(
                //number of coordinate values * 4 bytes per float
                cellCoords.length * 4
        );
        vb.order(ByteOrder.nativeOrder());
        vertexBuffer = vb.asFloatBuffer();
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

        // The texture buffer
        ByteBuffer tb = ByteBuffer.allocateDirect(uvCoords.length * 4);
        tb.order(ByteOrder.nativeOrder());
        textureBuffer = tb.asFloatBuffer();
        textureBuffer.put(uvCoords);
        textureBuffer.position(0);
    }

    //wall cell's drawing logic
    public void draw(GL10 gl, int textureId) {

        //Bind the texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        //Point to buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //point to vertex buffer
        gl.glVertexPointer(COORDS_PER_VERTEX,
                GL10.GL_FLOAT, 0,
                vertexBuffer);//point to the vertex data

        //point to texture buffer
        gl.glTexCoordPointer(COORDS_PER_TEXTURE,
                GL10.GL_FLOAT, 0,
                textureBuffer);

        gl.glDrawElements(
                GL10.GL_TRIANGLES,
                drawOrder.length, GL10.GL_UNSIGNED_SHORT,
                drawListBuffer);

        //Disable client state to avoid conflicts with shapes that don't use this
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

    }
}
