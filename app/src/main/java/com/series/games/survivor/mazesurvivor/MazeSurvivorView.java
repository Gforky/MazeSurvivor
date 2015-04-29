package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorView extends GLSurfaceView {

    public final MazeSurvivorRenderer myRenderer;
    //game's level
    private int level;

    //screen width and height ratio
    private float ratio;

    public MazeSurvivorView(Context context, int level, float ratio) {
        super(context);

        this.level = level;

        this.ratio = ratio;

        myRenderer = new MazeSurvivorRenderer(ratio, level, context);

        //set myRenderer as the renderer for the GLSurfaceView
        setRenderer(myRenderer);
    }
}
