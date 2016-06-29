package com.example.joker.util.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper
{

    public static Bitmap getLocalBitmap(String filePath)
    {
        return BitmapFactory.decodeFile(filePath, null);
    }

    public static Bitmap getAppBitmap(String picname)
    {
        return BitmapFactory.decodeResource(MyAppParams.getInstance()
                .getResources(), AppRescouceReflect.getAppRrawbleID(picname));
    }

    public static Bitmap getAppBitmap(int picId)
    {
        return BitmapFactory.decodeResource(MyAppParams.getInstance()
                .getResources(), picId);
    }

}
