package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorView extends GLSurfaceView {

    public final MazeSurvivorRenderer myRenderer;
    //maze's row and column
    private int row;
    private int col;

    //screen width and height ratio
    private float ratio;

    public MazeSurvivorView(Context context, int row, int col, float ratio) {
        super(context);

        this.row = row;
        this.col = col;

        this.ratio = ratio;

        myRenderer = new MazeSurvivorRenderer(ratio, row, col, context);

        //set myRenderer as the renderer for the GLSurfaceView
        setRenderer(myRenderer);
    }
}
