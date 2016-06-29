package com.example.joker.util.common;

public class RegularUtil
{
    public static String getImgSrc(String text)
    {
        return text.replaceAll("<img\\s+src=\"([^\"]*?)\".*>(.*?>)?", "$1")
                .trim().replace("\"", "");
    }

    // "<link path=\"C:\\io\\apn-box.png\" tag=\"png\"/>"
    // C:\\io\\apn-box.png
    public static String getLinkFilePath(String text)
    {
        return text.replaceAll("<link\\s+path=\"([^\"]*?)\".*>(.*?>)?", "$1")
                .trim().replace("\"", "");
    }

    public static String getLinkTipPath(String text)
    {
        return text.replaceAll("<link.*?tip=\"([^\"]*?)\".*>(.*?>)?", "$1")
                .trim().replace("\"", "");
    }

}
