package com.example.joker.util.data;

import java.io.Serializable;

import com.example.joker.util.app.MyAppParams;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;

public class DbMonitor
{
    private static boolean dbMonitorFlag = true;

    public static void monitorWhenNeed(MONITOR_TYPE type,
            Serializable changeItem)
    {
        if (!dbMonitorFlag)
        {
            return;
        }

        switch (type)
        {
        case UPDATE:
            writeChangeLog("修改:" + changeItem.toString());
            break;
        case ADD:
            writeChangeLog("添加:" + changeItem.toString());
            break;
        case DEL:
            if (changeItem instanceof Integer)
            {
                writeChangeLog("删除:ID=" + changeItem.toString());
            }
            else
            {
                writeChangeLog("删除:" + changeItem.toString());
            }
            break;
        default:
            break;
        }
    }

    private static void writeChangeLog(String string)
    {
        BasicFileUtil.writeFileString(MyAppParams.getInstance().getWorkPath()
                + "monitor.txt", BasicDateUtil.getCurrentDateTimeString()
                + "  " + string + "\r\n", null, true);
    }
}
