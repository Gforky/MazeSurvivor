package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import com.series.games.survivor.mazesurvivor.gameobjects.AlertBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.AliveMonsterBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.AttackButton;
import com.series.games.survivor.mazesurvivor.gameobjects.BonusTimeBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.CountDownTimer;
import com.series.games.survivor.mazesurvivor.gameobjects.DirButtons;
import com.series.games.survivor.mazesurvivor.gameobjects.GameTextures;
import com.series.games.survivor.mazesurvivor.gameobjects.LevelSelector;
import com.series.games.survivor.mazesurvivor.gameobjects.MazeWorld;
import com.series.games.survivor.mazesurvivor.gameobjects.ScoreBoard;
import com.series.games.survivor.mazesurvivor.gameobjects.Survivor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorRenderer implements GLSurfaceView.Renderer {

    //Storage to store the cell information in the maze, 'w' mean wall, 'p' means path, 's' means survivor
    MazeWorld mazeWorld;
    MazeWorld.Cell[][] maze;
    //Record the current game mode
    private char mode;

    //Record the time when maze change
    private long prevMazeChangeTime;
    //Record the game start time and the total time used in game
    private long gameStartTime;
    private long timeUsed;

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

    //check whether can write into the file
    public boolean canWrite;

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
    public MazeSurvivorRenderer(float ratio, int level, Context context, char mode) {
        //Initializations
        this.context = context;

        //Set the game mode, when in exercise mode, don't record the score
        this.mode = mode;

        //Initialization of Game Level Selector
        levelSelector = new LevelSelector();
        //Get the initial level
        levelSelector.updateSelector(level);

        //control the corner case as the player enter 0 as initial size
        row = levelSelector.getMazeSize();
        col = levelSelector.getMazeSize();
        findExit = false;
        inChange = false;
        canWrite = true;

        //set the initial game level
        gameLevel = level;

        //Initialize the maze
        mazeWorld = new MazeWorld(row, col, ratio);

        //Get the maze from generator
        maze = mazeWorld.generateMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps(), gameLevel);//generate a M * N maze

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
        prevMazeChangeTime = SystemClock.uptimeMillis();
        gameStartTime = prevMazeChangeTime;
        timeUsed = 0;

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
                    gameLevel > 99 ? 0 : (mazeWorld.getChangeTime() - (SystemClock.uptimeMillis() - (isPaused ? (prevMazeChangeTime + (SystemClock.uptimeMillis() - pausedTime)) : prevMazeChangeTime))) / 1000L);

        if(mazeWorld.survivor.isAlive && findExit) {//player run out of current maze, create a new maze for the player, and update the time used in game
            timeUsed = SystemClock.uptimeMillis() - gameStartTime;
            updateGame();
        } else if(gameLevel < 100 && mazeWorld.survivor.isAlive &&
                (SystemClock.uptimeMillis() - (isPaused ? (prevMazeChangeTime + (SystemClock.uptimeMillis() - pausedTime)) : prevMazeChangeTime)) >= mazeWorld.getChangeTime()) {
            //time to change the maze
            changeMaze();
        } else if(!mazeWorld.survivor.isAlive) {//Game Over, display game over alert, update the highest score if exceed the record
            if(canWrite && mode == 'm') {//Avoid writing duplicate files, and only record the new highest score in marathon mode
                long[] highestScore = readFromFile();
                //Hit the new record, write into the file
                if ((int) highestScore[0] < gameLevel - 1 || ((int) highestScore[0] == gameLevel - 1 && highestScore[1] < timeUsed)) {
                    //Delete the old file
                    File highestScoreFile = new File(context.getFilesDir(), "HighestScore.txt");
                    highestScoreFile.delete();
                    writeIntoFile("" + (gameLevel - 1) + " " + timeUsed);
                }
            }
            canWrite = false;
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
            maze = mazeWorld.generateMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps(), gameLevel);

            prevMazeChangeTime = SystemClock.uptimeMillis();//reset the start time

            inChange = false;//free the player
        }
    }

    /**Update the game pause status
     *
     */
    public void updatePausedStatus() {

        if(mazeWorld.survivor.isAlive) {//Game in continue
            isPaused = !isPaused;
            if (isPaused) {
                //If game is paused, record the paused time
                pausedTime = SystemClock.uptimeMillis();
            } else {//update the startTime to the new time
                prevMazeChangeTime += (SystemClock.uptimeMillis() - pausedTime);
            }
        } else if(mode == 'm'){//Game over, restart the game when in marathon mode
            gameLevel = 0;
            //Get the initial level
            findExit = false;
            inChange = false;
            canWrite = true;
            mazeWorld.survivor.isAlive = true;
            updateGame();
            //set the game start time for current round
            gameStartTime = SystemClock.uptimeMillis();
            timeUsed = 0;

            isPaused = false;
        }
    }

    /**Change the maze
     *
     */
    private void changeMaze() {

        //player still in current maze, change the maze around every (maze row) seconds
        inChange = true;//freeze the action of the player, avoid sending wrong player's position to the maze generator
        prevMazeChangeTime = SystemClock.uptimeMillis();

        //update the player's position, avoid changing the maze with the previous player's position
        mazeWorld.updatePlayer(mazeWorld.survivor.getX(), mazeWorld.survivor.getY());//set the player at a new start position
        maze = mazeWorld.changeMaze(levelSelector.getNumOfMonsters(), levelSelector.getNumOfTraps(), gameLevel);//change the maze
        inChange = false;//free the player
    }

    /**Function to write the highest score into file
     *
     * @param highestScore
     */
    private void writeIntoFile(String highestScore) {

        String filename = "HighestScore.txt";
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_MULTI_PROCESS);
            outputStream.write(highestScore.getBytes());
            outputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**Function to read file from highestScore File
     *
     * @return
     */
    private long[] readFromFile() {
        String set = "";
        try {
            InputStream inputStream = context.openFileInput("HighestScore.txt");
            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                set = stringBuilder.toString();
            }
        } catch(FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch(IOException e) {
            Log.e("login activity", "Cannot read file: " + e.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(set == "") {
            return new long[]{0, 0};
        } else {
            return getHighestScore(set);
        }
    }

    /**Get the previous highest game level with the corresponding least game time\
     *
     * @param set
     * @return
     */
    private long[] getHighestScore(String set) {
        if(set == null || set.length() == 0) {//Sanity check
            return new long[]{0, 0};
        }
        ArrayList<Character> highestLevel = new ArrayList<Character>();
        ArrayList<Character> leastTimeUsed = new ArrayList<Character>();
        int index = 0;
        //get the highest game level in char array
        while(set.charAt(index) != ' ') {
            highestLevel.add(set.charAt(index++));
        }
        index++;
        //get the least time used in game for the highest game level
        while(index < set.length()) {
            leastTimeUsed.add(set.charAt(index++));
        }
        return new long[]{convertCharArrayToLong(highestLevel), convertCharArrayToLong(leastTimeUsed)};
    }

    /**Function to convert ArrayList of Character into long
     *
     * @param arrayList
     * @return
     */
    private long convertCharArrayToLong(ArrayList<Character> arrayList) {
        int index = 0;
        long result = 0;
        int size = arrayList.size();
        while(index < size) {
            result += (arrayList.get(index) - '0') * Math.pow(10, size - 1 - index);
            index++;
        }
        return result;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }
}