package com.example.joker.util.common;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SoupUtil
{
    static Connection con = null;

    public static Document getDoc(String p)
    {
        Document doc = null;
        con = Jsoup.connect(p);
        con.timeout(300000);// 设置连接超时时间
        // 给服务器发消息头，告诉服务器，俺不是java程序。CSDN不允许java程序访问
        con.header("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
        try
        {
            // 获取返回的html的document对象
            doc = con.get();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
            throw new IllegalArgumentException("网址非法,无法连接: " + p);
        }
        finally
        {
            con = null;
        }
        return doc;
    }

}
