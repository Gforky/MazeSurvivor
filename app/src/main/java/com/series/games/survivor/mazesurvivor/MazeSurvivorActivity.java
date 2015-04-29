package com.series.games.survivor.mazesurvivor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.series.games.survivor.SurvivorGamesMenu;
import com.series.survivor.survivorgames.R;


public class MazeSurvivorActivity extends Activity {

    private MazeSurvivorView myGLView;
    private float screenWidth;
    private float screenHeight;
    private float ratio;
    //borders of direction control buttons' touch areas
    private float buttonLeftBorder;
    private float buttonRightBorder;
    private float buttonUpBorder;
    private float buttonMidBorder;
    private float attackButtonLeftBorder;
    private float bonusTimeLeftBorder;
    private float bonusTimeBottomBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive the message from SurvivorGamesMenu Activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(SurvivorGamesMenu.EXTRA_MESSAGE);
        //convert string to int
        int level = 0;
        char[] array = message.toCharArray();
        //get the initial level from menu activity
        for (int index = 0; index < array.length; index++) {
            level += ((array[index] - '0') * Math.pow(10, array.length - 1 - index));
        }

        //get the screen's width and height ratio
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        ratio = screenWidth / screenHeight;

        buttonLeftBorder = screenWidth / 3;
        buttonRightBorder = screenWidth * 2 / 3;
        buttonUpBorder = (screenHeight + screenWidth) / 2;
        buttonMidBorder = (screenHeight + screenWidth) / 2 + (screenHeight - screenWidth) / 4;
        attackButtonLeftBorder = screenWidth * 3 / 4;
        bonusTimeLeftBorder = screenWidth / 2;
        bonusTimeBottomBorder = screenWidth + (screenHeight - screenWidth) / 4;

        //Create an instance of GLSurfaceView
        //and set it as the content view
        myGLView = new MazeSurvivorView(this, level, ratio);
        setContentView(myGLView);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://According to the touch down position
                //move survivor to the corresponding direction, by 1 step
                //Or attack the monster
                if(x < buttonLeftBorder && y > buttonUpBorder) {
                    myGLView.myRenderer.updateSurvivor("LEFT");
                } else if(x > buttonRightBorder && y > buttonUpBorder) {
                    myGLView.myRenderer.updateSurvivor("RIGHT");
                } else if(y < buttonMidBorder && y > buttonUpBorder && x > buttonLeftBorder && x < buttonRightBorder) {
                    myGLView.myRenderer.updateSurvivor("UP");
                } else if(y > buttonMidBorder && x > buttonLeftBorder && x < buttonRightBorder) {
                    myGLView.myRenderer.updateSurvivor("DOWN");
                } else if(x > attackButtonLeftBorder && y > screenWidth && y < buttonUpBorder) {//Attack Button touched
                    myGLView.myRenderer.updateSword();
                } else if(x > bonusTimeLeftBorder && x < attackButtonLeftBorder && y > screenWidth && y < bonusTimeBottomBorder) {//BonusTime Button touched
                    myGLView.myRenderer.updateChangeTime();
                } else if(y < screenWidth) {//Game is paused
                    myGLView.myRenderer.updatePausedStatus();
                }
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

        }
        return false;
    }
}
