package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.SystemClock;

import com.series.games.survivor.mazesurvivor.gameobjects.Dir;
import com.series.games.survivor.mazesurvivor.gameobjects.DirButtons;
import com.series.games.survivor.mazesurvivor.gameobjects.MazeWorld;
import com.series.games.survivor.mazesurvivor.gameobjects.ScoreBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.Survivor;
import com.series.survivor.survivorgames.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorRenderer implements GLSurfaceView.Renderer {

    //Storage to store the cell information in the maze, 'w' mean wall, 'p' means path, 's' means survivor
    MazeWorld mazeWorld;
    MazeWorld.Cell[][] maze;

    //Set the startTime of a maze
    private long startTime;

    //numbers of row and column of the maze
    int row;
    int col;

    //Player object
    private Survivor survivor;

    //Direction buttons
    private DirButtons dirButtons;

    //ScoreBoard
    private ScoreBoard scoreBoard;

    //record the status of the player, if the player find the exit, set it to true
    private boolean findExit;

    //boolean flag to record whether the maze is in change or not
    private boolean inChange;

    //Context get from the MazeSurvivorActivity
    private Context context;

    //record the game levels
    private int gameLevel;

    //texture Ids used for drawing maze
    private int wallTexture;
    private int pathTexture;
    private int survivorTexture;
    private int exitTexture;
    private int leftButtonTexture;
    private int rightButtonTexture;
    private int upButtonTexture;
    private int downButtonTexture;
    private int lvSymbolTexture;
    private int numTextures[];


    //Constructor
    public MazeSurvivorRenderer(float ratio, int row, int col, Context context) {
        //Initializations
        this.context = context;

        //control the corner case as the player enter 0 as initial size
        this.row = row == 0 ? 4 : row;
        this.col = col == 0 ? 4 : col;
        survivor = new Survivor(row, col);
        findExit = false;
        inChange = false;

        //set the initial game level
        gameLevel = (row - 2) / 2;

        //Initialize the maze
        mazeWorld = new MazeWorld(row, col, ratio, survivor.getX(), survivor.getY());

        //Get the maze from generator
        maze = mazeWorld.generateMaze();//generate a M * N maze

        //Initialize direction buttons
        dirButtons = mazeWorld.getDirButtons();

        //Initialize ScoreBoard
        scoreBoard = new ScoreBoard(ratio);

        //set the game start time for current round
        startTime = SystemClock.uptimeMillis();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //Enable texture
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //Enable Smooth Shading
        gl.glShadeModel(GL10.GL_SMOOTH);
        //Depth Buffer Setup
        gl.glClearDepthf(1.0f);
        //Enables Depth Testing
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //The Type Of Depth Testing To Do
        gl.glDepthFunc(GL10.GL_LEQUAL);
        //set the background frame color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //load the textures used for drawing maze, load the texture once, and bind it to GL_TEXTURE_2D when render
        wallTexture = loadTexture(gl, context, R.drawable.brick_wall);
        pathTexture = loadTexture(gl, context, R.drawable.grass);
        survivorTexture = loadTexture(gl, context, R.drawable.survivor);
        exitTexture = loadTexture(gl, context, R.drawable.exit);
        leftButtonTexture = loadTexture(gl, context, R.drawable.leftbutton);
        rightButtonTexture = loadTexture(gl, context, R.drawable.rightbutton);
        upButtonTexture = loadTexture(gl, context, R.drawable.upbutton);
        downButtonTexture = loadTexture(gl, context, R.drawable.downbutton);
        lvSymbolTexture = loadTexture(gl, context, R.drawable.lvsymbol);
        numTextures = new int[]{
                loadTexture(gl, context, R.drawable.num0),
                loadTexture(gl, context, R.drawable.num1),
                loadTexture(gl, context, R.drawable.num2),
                loadTexture(gl, context, R.drawable.num3),
                loadTexture(gl, context, R.drawable.num4),
                loadTexture(gl, context, R.drawable.num5),
                loadTexture(gl, context, R.drawable.num6),
                loadTexture(gl, context, R.drawable.num7),
                loadTexture(gl, context, R.drawable.num8),
                loadTexture(gl, context, R.drawable.num9)
        };
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        //draw all the elements on the game interface
        drawMaze(gl);
        drawScoreBoard(gl);
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

            updateLevel();//increase the game level by 1
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

    /**Function to draw the maze
     *
     * @param gl
     */
    private void drawMaze(GL10 gl) {//draw maze function

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
                }
            }
        }
    }

    /**Function to draw the direction control buttons
     *
     * @param gl
     */
    private void drawButtons(GL10 gl) {//draw operation buttons
        dirButtons.leftButton.draw(gl, leftButtonTexture);
        dirButtons.rightButton.draw(gl, rightButtonTexture);
        dirButtons.upButton.draw(gl, upButtonTexture);
        dirButtons.downButton.draw(gl, downButtonTexture);
    }

    /**Function to draw the ScoreBoard
     *
     * @param gl
     */
    private void drawScoreBoard(GL10 gl) {

        scoreBoard.lvSymbol.draw(gl, lvSymbolTexture);
        scoreBoard.firstDigit.draw(gl, numTextures[gameLevel / 10]);
        scoreBoard.secondDigit.draw(gl, numTextures[gameLevel % 10]);
    }

    /**Update the survivor position according to the touch event
     *
     * @param move
     */
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

    /**Function to load the textures
     *
     * @param gl
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(GL10 gl, final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        gl.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0 )
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**Function to update game level
     *
     */
    private void updateLevel() {

        gameLevel += 1;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}