package com.series.games.survivor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.series.games.survivor.mazesurvivor.MazeSurvivorActivity;
import com.series.survivor.survivorgames.R;


public class SurvivorGamesMenu extends ActionBarActivity implements OnClickListener {

    public final static String EXTRA_MESSAGE = "com.series.survivor.MESSAGE";
    private Button mazeMarathonButton;
    private Button mazeExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survivor_games_menu);
        mazeMarathonButton = (Button) findViewById(R.id.maze_marathon_button);
        mazeExerciseButton = (Button) findViewById(R.id.maze_exercise_button);
        mazeMarathonButton.setOnClickListener(this);
        mazeExerciseButton.setOnClickListener(this);
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
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
                break;

            //maze marathon button be clicked
            case R.id.maze_marathon_button:
                //Do something here in response to the button
                intent.putExtra(EXTRA_MESSAGE, "1");
                startActivity(intent);
                break;
        }
    }
}
