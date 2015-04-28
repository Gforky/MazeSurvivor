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
    private float attackButtonleftBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive the message from SurvivorGamesMenu Activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(SurvivorGamesMenu.EXTRA_MESSAGE);
        //convert string to int
        int size = 0;
        char[] array = message.toCharArray();
        if(array == null || array.length == 0) {//if no initial size has been set, set it to 4
            size = 4;
        } else {//get the initial size from menu activity
            for (int index = 0; index < array.length; index++) {
                size += ((array[index] - '0') * Math.pow(10, array.length - 1 - index));
            }
        }
        System.out.println("Size is " + size);

        //get the screen's width and height ratio
        WindowManager manager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point= new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        ratio = screenWidth / screenHeight;

        buttonLeftBorder = screenWidth / 3;
        buttonRightBorder = screenWidth * 2 / 3;
        buttonUpBorder = (screenHeight + screenWidth) / 2;
        buttonMidBorder = (screenHeight + screenWidth) / 2 + (screenHeight - screenWidth) / 4;
        attackButtonleftBorder = screenWidth * 3 / 4;

        //Create an instance of GLSurfaceView
        //and set it as the content view
        myGLView = new MazeSurvivorView(this, size, size, ratio);
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
                } else if(x > attackButtonleftBorder && y > screenWidth && y < buttonUpBorder) {
                    myGLView.myRenderer.updateSword();
                }
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

        }
        return false;
    }
}
