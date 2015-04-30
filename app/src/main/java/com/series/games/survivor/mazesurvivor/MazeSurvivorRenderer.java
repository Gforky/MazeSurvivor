package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.series.games.survivor.mazesurvivor.gameobjects.AlertBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.AttackButton;
import com.series.games.survivor.mazesurvivor.gameobjects.BonusTimeBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.CountDownTimer;
import com.series.games.survivor.mazesurvivor.gameobjects.DirButtons;
import com.series.games.survivor.mazesurvivor.gameobjects.GameTextures;
import com.series.games.survivor.mazesurvivor.gameobjects.LevelSelector;
import com.series.games.survivor.mazesurvivor.gameobjects.MazeWorld;
import com.series.games.survivor.mazesurvivor.gameobjects.AliveMonsterBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.ScoreBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.Survivor;

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

    //Direction buttons
    private DirButtons dirButtons;

    //ScoreBoard
    private ScoreBoard scoreBoard;

    //Bonus Time Delayer board
    private BonusTimeBoard bonusTimeBoard;

    //ScoreBoard to record the number of killed monsters
    private AliveMonsterBoard aliveMonsterBoard;

    //Count Down Timer
    private CountDownTimer countDownTimer;

    //Attack Button
    private AttackButton attackButton;

    //Alert Board
    private AlertBoard alertBoard;

    //record the status of the player, if the player find the exit, set it to true
    private boolean findExit;

    //boolean flag to record whether the maze is in change or not
    private boolean inChange;

    //boolean flag to check whether the game is paused
    private boolean isPaused;
    //Record the paused time
    private long pausedTime;

    //Context get from the MazeSurvivorActivity
    private Context context;

    //record the game levels
    private int gameLevel;

    //Game Level Selector
    private LevelSelector levelSelector;

    //All the textures used for game rendering
    private GameTextures gameTextures;

    //Constructor
    public MazeSurvivorRenderer(float ratio, int level, Context context) {
        //Initializations
        this.context = context;

        //Initialization of Game Level Selector
        levelSelector = new LevelSelector();
        //Get the initial level
        levelSelector.updateSelector(level);

        //control the corner case as the player enter 0 as initial size
        row = levelSelector.getMazeSize();
        col = levelSelector.getMazeSize();
        findExit = false;
        inChange = false;

        //set the initial game level
        gameLevel = level;

        //Initialize the maze
        mazeWorld = new MazeWorld(row, col, ratio);

        //Get the maze from generator
        maze = mazeWorld.generateMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps());//generate a M * N maze

        //Initialize direction buttons
        dirButtons = mazeWorld.getDirButtons();

        //Initialize ScoreBoard
        scoreBoard = new ScoreBoard(ratio);

        //Initialize BonusTimeDelayer board
        bonusTimeBoard = new BonusTimeBoard(ratio);

        //Initialize MonsterKilledBoard
        aliveMonsterBoard = new AliveMonsterBoard(ratio);

        //Initialize CountDownTimer
        countDownTimer = new CountDownTimer(ratio);

        //Initialize AttackButton
        attackButton = new AttackButton(ratio);

        //Initialize Alert Board
        alertBoard = new AlertBoard(ratio);

        //set the game start time for current round
        startTime = SystemClock.uptimeMillis();

        isPaused = false;
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
        gameTextures = new GameTextures(gl, context);
    }

    public void onDrawFrame(GL10 gl) {
        //Redraw background color
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        if(mazeWorld.survivor.isAlive && gameLevel <= 99 && !isPaused) {
            //Check the game status
            mazeWorld.checkIfGameOver();//Player is killed by the monster

            //Check the survival status of monsters
            mazeWorld.checkMonsterIsAlive();
        }

        //draw all the elements on the game interface
        mazeWorld.drawMaze(gl, gameTextures,
                inChange,
                isPaused
        );
        //Game is paused, draw Pause Alert, avoid displaying under the maze, draw it after draw the maze
        if(mazeWorld.survivor.isAlive && isPaused) {
            alertBoard.drawAlertBoard(gl, gameTextures.pauseAlertTexture);
        }

        scoreBoard.drawScoreBoard(gl,
                gameTextures.lvSymbolTexture,
                gameTextures.numTextures,
                gameLevel > 99 ? 99 : gameLevel//Avoid exceeding the display bound
        );

        bonusTimeBoard.drawBonusTimeBoard(gl,
                gameTextures.bonusTimeLogoTexture,
                gameTextures.numTextures,
                mazeWorld.survivor.getNumOfTimeDelayer()
        );

        aliveMonsterBoard.drawAliveMonsterBoard(gl,
                gameTextures.monsterLogoTexture,
                gameTextures.numTextures,
                mazeWorld.getNumOfAliveMonsters()
        );

        dirButtons.drawButtons(gl,
                gameTextures.leftButtonTexture,
                gameTextures.rightButtonTexture,
                gameTextures.upButtonTexture,
                gameTextures.downButtonTexture
        );

        attackButton.drawAttackButton(gl, gameTextures.attackButtonTexture);

        //If game is paused, update the startTime until the game is continue
        countDownTimer.drawCountDownTime(gl, gameTextures.numTextures, mazeWorld.survivor.isAlive,
                    gameLevel > 99 ? 0 : (mazeWorld.getChangeTime() - (SystemClock.uptimeMillis() - (isPaused ? (startTime + (SystemClock.uptimeMillis() - pausedTime)) : startTime))) / 1000L);

        //player run out of current maze, create a new maze for the player
        if(mazeWorld.survivor.isAlive && findExit) {
            updateGame();
        } else if(gameLevel < 100 && mazeWorld.survivor.isAlive &&
                (SystemClock.uptimeMillis() - (isPaused ? (startTime + (SystemClock.uptimeMillis() - pausedTime)) : startTime)) >= mazeWorld.getChangeTime()) {
            //time to change the maze
            changeMaze();
        } else if(!mazeWorld.survivor.isAlive) {//Game Over, display game over alert
            alertBoard.drawAlertBoard(gl, gameTextures.gameOverAlertTexture);
        } else if(gameLevel > 99) {//Clear all the levels, win the game
            alertBoard.drawAlertBoard(gl, gameTextures.gameClearAlertTexture);
        }
    }

    /**Function to update game level
     *
     */
    private void updateLevel() {

        gameLevel += 1;
    }

    /**Function to update the player's position
     *
     * @param move
     */
    public void updateSurvivor(String move) {

        if(gameLevel < 100 && mazeWorld.survivor.isAlive && !isPaused &&
                mazeWorld.survivor.updateSurvivor(move, maze, inChange)) {//player reach the exit
            updateFindExit();
        }
    }
    private void updateFindExit() {

        findExit = true;
    }

    /**Update the maze change time when bonus time is used
     *
     */
    public void updateChangeTime() {

        if(gameLevel < 100 && !isPaused) {
            mazeWorld.updateChangeTime();
        }
    }

    /**Function to use the sword to attack the monster
     *
     */
    public void updateSword() {

        if(gameLevel < 100 && !isPaused) {
            mazeWorld.survivorAttack(inChange);
        }
    }

    /**Change the game to the next level
     *
     */
    private void updateGame() {

        findExit = false;//set the boolean flag to false
        inChange = true;//freeze the action of the player, avoid sending wrong player's position to the maze generator

        updateLevel();//increase the game level by 1
        if(gameLevel < 100) {
            levelSelector.updateSelector(gameLevel);
            row = levelSelector.getMazeSize();
            col = levelSelector.getMazeSize();

            //reset the player and re-generate the maze
            mazeWorld.survivor = new Survivor(row, col);

            //re-generate the maze
            mazeWorld.updateMaze(row, col);//update the row and column of the maze
            mazeWorld.updatePlayer(mazeWorld.survivor.getX(), mazeWorld.survivor.getY());//set the player at a new start position
            maze = mazeWorld.generateMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps());

            startTime = SystemClock.uptimeMillis();//reset the start time

            inChange = false;//free the player
        }
    }

    /**Update the game pause status
     *
     */
    public void updatePausedStatus() {

        isPaused = !isPaused;
        if(isPaused) {
            //If game is paused, record the paused time
            pausedTime = SystemClock.uptimeMillis();
        } else {//update the startTime to the new time
            startTime += (SystemClock.uptimeMillis() - pausedTime);
        }
    }

    /**Change the maze
     *
     */
    private void changeMaze() {

        //player still in current maze, change the maze around every (maze row) seconds
        inChange = true;//freeze the action of the player, avoid sending wrong player's position to the maze generator
        startTime = SystemClock.uptimeMillis();

        //update the player's position, avoid changing the maze with the previous player's position
        mazeWorld.updatePlayer(mazeWorld.survivor.getX(), mazeWorld.survivor.getY());//set the player at a new start position
        maze = mazeWorld.changeMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps());//change the maze
        inChange = false;//free the player
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}