package com.example.joker.util.app;

import android.text.Html;
import android.text.Spanned;

public class TextHelper
{
    public static Spanned getHtmlFormatText(String content)
    {
        return Html.fromHtml(content);
    }

}
