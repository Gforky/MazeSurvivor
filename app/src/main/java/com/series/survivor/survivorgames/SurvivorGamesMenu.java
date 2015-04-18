package com.series.survivor.survivorgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SurvivorGamesMenu extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.series.survivor.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survivor_games_menu);
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

    //Calls when the user clicks the SEND button
    public void sendMessage(View view) {
        //Do something here in response to the button
        Intent intent = new Intent(this, MazeSurvivorActivity.class);
        String message = "Hello World!";//message to send to MazeSurvivorActivity
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
