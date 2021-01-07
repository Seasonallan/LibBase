package com.library.base;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;


/**
 * 需要在在application的onCreate中初始化: s.init(this);
 */
public final class GlobalContext {

    private static boolean sDebug;
    private static Application sApplication;


    public static void init(Application app) {
        sApplication = app;
    }

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    private GlobalContext() {
    }

    public static boolean isDebug() {
        return sDebug;
    }

    public static Application app() {
        return sApplication;
    }

    public static boolean isInit() {
        return sApplication != null;
    }

}
