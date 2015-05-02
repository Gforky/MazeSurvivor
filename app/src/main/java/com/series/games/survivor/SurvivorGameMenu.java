package com.series.games.survivor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.series.games.survivor.mazesurvivor.MazeSurvivorActivity;
import com.series.survivor.survivorgames.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SurvivorGameMenu extends ActionBarActivity implements OnClickListener {

    public final static String EXTRA_MESSAGE = "com.series.survivor.MESSAGE";
    private Button mazeMarathonButton;
    private Button mazeExerciseButton;
    //Display the highest score
    private TextView highestScoreLevelTextView;
    private TextView highestScoreTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survivor_games_menu);
        mazeMarathonButton = (Button) findViewById(R.id.marathon_mode_button);
        mazeExerciseButton = (Button) findViewById(R.id.maze_exercise_button);
        mazeMarathonButton.setOnClickListener(this);
        mazeExerciseButton.setOnClickListener(this);
        highestScoreLevelTextView = (TextView) findViewById(R.id.displayed_highest_score_level);
        highestScoreTimeTextView = (TextView) findViewById(R.id.displayed_highest_score_time);

        //Check if first time run the application
        SharedPreferences prefs = getSharedPreferences("com.series.games.survivor", MODE_PRIVATE);
        if(prefs.getBoolean("firstrun", true)) {
            writeIntoFile("HighestScore.txt");//create the highest score file to record the highest score
            prefs.edit().putBoolean("firstrun", false).commit();
        }

        String highestScore = readFromFile();
        //Split the highest score into two part: highest level, and the corresponding least game time
        int index = 0;
        while(highestScore.charAt(index) != ' ') {
            index++;
        }
        String highestLevel = highestScore.substring(0, index);
        String leastGameTime = highestScore.substring(index + 1);
        highestScoreLevelTextView.setText("Level " + highestLevel);
        highestScoreTimeTextView.setText("Within " + leastGameTime + "ms");

        //Display the game menu animation
        final ImageView animation = (ImageView) findViewById(R.id.game_menu_animation);
        animation.setBackgroundResource(R.drawable.game_menu_animation);
        animation.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable animationDrawable = (AnimationDrawable)animation.getBackground();
                animationDrawable.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Update the highest score and update the highestScore TextView
        String highestScore = readFromFile();
        //Split the highest score into two part: highest level, and the corresponding least game time
        int index = 0;
        while(highestScore.charAt(index) != ' ') {
            index++;
        }
        String highestLevel = highestScore.substring(0, index);
        String leastGameTime = highestScore.substring(index + 1);
        highestScoreLevelTextView.setText("Level " + highestLevel);
        highestScoreTimeTextView.setText("Within " + leastGameTime + "ms");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survivor_games_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, GameIntroduction.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**Function to do corresponding actions according to the button clicked events
     *
     * @param view
     */
    public void onClick(View view) {
        Intent intent = new Intent(this, MazeSurvivorActivity.class);

        switch(view.getId()) {

            //maze exercise button be clicked
            case R.id.maze_exercise_button:
            //Do something here in response to the button
                EditText editText = (EditText) findViewById(R.id.set_level_of_game);
                String message = editText.getText().toString();//message to send to MazeSurvivorActivity
                //Create an alert dialog to warn the empty or wrong input
                AlertDialog.Builder InputAlertBuilder = new AlertDialog.Builder(this);
                InputAlertBuilder.setMessage("Please enter 1 - 99");
                InputAlertBuilder.setCancelable(true);
                InputAlertBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog InputAlert = InputAlertBuilder.create();
                //Sanity check
                if(message == null || message.length() == 0) {
                    InputAlert.show();
                } else {
                    int level = 0;
                    char[] array = message.toCharArray();
                    //get the initial level from menu activity
                    for (int index = 0; index < array.length; index++) {
                        level += ((array[index] - '0') * Math.pow(10, array.length - 1 - index));
                    } if(level == 0 || level > 99) {//Input exceed the highest level
                        InputAlert.show();
                    } else {
                        intent.putExtra(EXTRA_MESSAGE, message + " e");//Start with customized level, in exercise mode
                        startActivity(intent);
                    }
                }
                break;

            //maze marathon button be clicked
            case R.id.marathon_mode_button:
                //Do something here in response to the button
                intent.putExtra(EXTRA_MESSAGE, "1 m");//Start with level 1, in marathon mode
                startActivity(intent);
                break;
        }
    }

    /**Function to read file from highestScore File
     *
     * @return
     */
    private String readFromFile() {

        String set = "";
        try {
            InputStream inputStream = openFileInput("HighestScore.txt");
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
        } catch(FileNotFoundException e) {//File not create, create the file
            Log.e("login activity", "File not found: " + e.toString());
        } catch(IOException e) {
            Log.e("login activity", "Cannot read file: " + e.toString());
        }
        return set;
    }

    /**Function to write into file
     *
     * @param filename
     */
    private void writeIntoFile(String filename) {

        String string = "0 0";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_MULTI_PROCESS);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
