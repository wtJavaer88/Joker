package com.example.joker.util.data;

import java.io.File;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

import com.example.joker.util.app.MyAppParams;
import com.example.joker.util.common.ToastUtil;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;

public class BackUpDataUtil
{
    static final String subjectDb = "joke.db";
    static final int maxBackupCount = 20;

    public static void backup(Context context)
    {
        File dbFile = context.getDatabasePath(subjectDb);
        String newFilePath = BasicFileUtil.getMakeFilePath(MyAppParams
                .getInstance().getBackupDbPath(),
        // "/data/data/db",
                BasicDateUtil.getCurrentDateTimeString() + "_" + subjectDb);
        File[] files = new File(MyAppParams.getInstance().getBackupDbPath())
                .listFiles();
        Arrays.sort(files);

        final int len = files.length;
        if (len > 0 && files[len - 1].length() == dbFile.length())
        {
            Log.i("BackUpDataUtil", "根据最近的备份文件判断,该文件已经备份!");
            return;
        }
        if (BasicFileUtil.CopyFile(dbFile, new File(newFilePath)))
        {
            int i = len;
            while (i + 1 > maxBackupCount)
            {
                String filePath = files[len - i].getAbsolutePath();
                Log.i("BackUpDataUtil", "删除-->" + filePath);
                if (!BasicFileUtil.deleteFile(filePath))
                {
                    ToastUtil
                            .showShortToast(context, "删除" + filePath + "文件失败!");
                }
                i--;
            }
        }
        else
        {
            ToastUtil.showShortToast(context, "复制" + subjectDb + "文件到<"
                    + newFilePath + ">失败!");
        }
    }
}
