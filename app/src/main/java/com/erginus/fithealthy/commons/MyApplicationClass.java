package com.erginus.fithealthy.commons;

import android.app.Application;

/**
 * Created by paramjeet on 23/6/15.
 */
public class MyApplicationClass extends Application  {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible=true;
}