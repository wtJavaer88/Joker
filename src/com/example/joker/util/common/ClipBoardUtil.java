package com.example.joker.util.common;

import android.content.Context;
import android.text.ClipboardManager;

public class ClipBoardUtil
{
    @SuppressWarnings("deprecation")
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
}
