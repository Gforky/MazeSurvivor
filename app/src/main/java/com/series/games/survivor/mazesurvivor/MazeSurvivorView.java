package com.series.games.survivor.mazesurvivor;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorView extends GLSurfaceView {

    public final MazeSurvivorRenderer myRenderer;

    public MazeSurvivorView(Context context, int level, float ratio, char mode) {
        super(context);

        myRenderer = new MazeSurvivorRenderer(ratio, level, context, mode);

        //set myRenderer as the renderer for the GLSurfaceView
        setRenderer(myRenderer);
    }
}
