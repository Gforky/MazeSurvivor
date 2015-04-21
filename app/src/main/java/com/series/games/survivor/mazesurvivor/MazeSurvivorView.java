package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorView extends GLSurfaceView {

    public final MazeSurvivorRenderer myRenderer;
    //maze's row and column
    private int row;
    private int col;

    public MazeSurvivorView(Context context, int row, int col) {
        super(context);

        this.row = row;
        this.col = col;
        //get the screen's width and height ratio
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size= new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y;
        float ratio = width / height;

        myRenderer = new MazeSurvivorRenderer(ratio, row, col);

        //set myRenderer as the renderer for the GLSurfaceView
        setRenderer(myRenderer);
    }
}
