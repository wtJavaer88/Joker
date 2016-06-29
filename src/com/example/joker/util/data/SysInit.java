package com.example.joker.util.data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.joker.util.app.MyAppParams;

public class SysInit
{
    final static boolean EMPTY_DB_FLAG = true;
    final static boolean EXTERNAL_FLAG = false;
    static Activity context;

    public static void init(Activity context2)
    {
        context = context2;

        MyAppParams.getInstance().setPackageName(context.getPackageName());
        MyAppParams.getInstance().setResources(context.getResources());
        MyAppParams.getInstance().setAppPath(context.getFilesDir().getParent());
        MyAppParams.getInstance().setScreenWidth(
                context.getWindowManager().getDefaultDisplay().getWidth());
        MyAppParams.getInstance().setScreenHeight(
                context.getWindowManager().getDefaultDisplay().getHeight());

        if (isFirstRun())
        {
            createDbAndFullData(EXTERNAL_FLAG);
        }
    }

    private static void createDbAndFullData(boolean flag)
    {
        MoveDbUtil.moveDb("joke.db", context.getBaseContext());
    }

    private static boolean isFirstRun()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "share", context.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isJokerFirstRun",
                true);
        Editor editor = sharedPreferences.edit();
        if (isFirstRun)
        {
            Log.d("Sysinit", "第一次运行");
            editor.putBoolean("isJokerFirstRun", false);
            editor.commit();
            return true;
        }
        else
        {
            Log.d("Sysinit", "不是第一次运行");
        }
        return false;
    }
}
