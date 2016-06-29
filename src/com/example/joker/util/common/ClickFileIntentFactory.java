package com.example.joker.util.common;

import android.content.Intent;

import com.example.joker.util.app.MyIntentUtil;
import com.wnc.basic.BasicStringUtil;

public class ClickFileIntentFactory
{
    public static Intent getIntentByFile(String filePath)
    {
        if (BasicStringUtil.isNullString(filePath))
        {
            throw new IllegalArgumentException("参数不能为空!");
        }

        if (FileTypeUtil.isTextFile(filePath))
        {
            return MyIntentUtil.getLocalTextFileIntent(filePath);
        }
        else if (FileTypeUtil.isPicFile(filePath))
        {
            return MyIntentUtil.getImageFileIntent(filePath);
        }
        else if (filePath.endsWith(".doc") || filePath.endsWith(".docx"))
        {
            return MyIntentUtil.getWordFileIntent(filePath);
        }
        else if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx"))
        {
            return MyIntentUtil.getExcelFileIntent(filePath);
        }
        else if (filePath.endsWith(".pdf"))
        {
            return MyIntentUtil.getPdfFileIntent(filePath);
        }

        throw new IllegalArgumentException(filePath + "该文件类型找不到对应的Intent");
    }
}
