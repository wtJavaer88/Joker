package com.example.joker.util.common;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtil
{
    @SuppressLint("NewApi")
    public static String getClipBoardContent(Context context)
    {
        @SuppressWarnings("deprecation")
        ClipboardManager clipboardManager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasText())
        {
            return clipboardManager.getText().toString();
        }
        return "";
    }

    @SuppressLint("NewApi")
    public static void setNormalContent(Context context, String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData textCd = ClipData.newPlainText("key", text);
        clipboard.setPrimaryClip(textCd);
        System.out.println("text: " + text);
    }
}
