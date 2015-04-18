package com.series.survivor.survivorgames;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Malvin on 4/18/2015.
 */
public class MazeSurvivorView extends GLSurfaceView {

    private final MazeSurvivorRenderer myRenderer;

    public MazeSurvivorView(Context context) {
        super(context);

        myRenderer = new MazeSurvivorRenderer();

        //set myRenderer as the renderer for the GLSurfaceView
        setRenderer(myRenderer);
    }
}
