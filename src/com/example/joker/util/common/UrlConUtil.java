package com.example.joker.util.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class UrlConUtil
{
    public static BufferedReader getReader(String urlStr)
    {
        URL url;
        BufferedReader in = null;

        try
        {
            url = new URL(urlStr);

            HttpURLConnection httpCon = (HttpURLConnection) url
                    .openConnection();

            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(5000);
            httpCon.connect();
            if (httpCon.getResponseCode() == 200)
            {
                InputStream is = httpCon.getInputStream();
                in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                is.close();
            }
            else
            {
                Log.e("spider", "Error not link");
            }
        }
        catch (Exception e)
        {
            Log.e("spider", "网络错误!" + urlStr);
            e.printStackTrace();
        }
        return in;
    }
}
