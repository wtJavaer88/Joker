package com.example.joker.util.common;

public class FileTypeUtil
{
    public static boolean isPicFile(String filePath)
    {
        return filePath.endsWith(".jpeg") || filePath.endsWith(".jpg")
                || filePath.endsWith(".bmp") || filePath.endsWith(".gif")
                || filePath.endsWith(".png");
    }

    public static boolean isTextFile(String filePath)
    {
        return filePath.endsWith(".txt") || filePath.endsWith(".java")
                || filePath.endsWith(".xml") || filePath.endsWith(".css")
                || filePath.endsWith(".cs");
    }
}
