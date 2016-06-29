package com.example.joker.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joker.bean.Joke;
import com.example.joker.util.data.DbMonitor;
import com.example.joker.util.data.MONITOR_TYPE;
import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;

public class JokeDao
{
    private static SQLiteDatabase db = null;

    public static void initDb(Context context)
    {
        if (db == null)
        {
            db = context.openOrCreateDatabase("joke.db", Context.MODE_PRIVATE,
                    null);
        }
    }

    public static void closeDb()
    {
        System.out.println("关闭subjectdao");
        if (db != null)
        {
            db.close();
            db = null;
        }
    }

    public static boolean insertJoke(String title, String content)
            throws RuntimeException
    {
        String currentTime = BasicDateUtil.getCurrentDateTimeString();
        try
        {
            db.execSQL(
                    "INSERT INTO subject(_ID,TITLE,CONTENT,recordtime) VALUES (NULL, ?, ?,?)",
                    new Object[]
                    { title, content, currentTime });
            DbMonitor.monitorWhenNeed(MONITOR_TYPE.ADD,
                    new Joke(title, content));
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        return true;
    }

    public static boolean insertJoke(Joke joke)
    {
        if (joke == null)
        {
            return false;
        }

        String currentTime = BasicDateUtil.getCurrentDateTimeString();
        try
        {
            db.execSQL(
                    "INSERT INTO subject(_ID,TITLE,CONTENT,type,recordtime) VALUES (NULL, ?,?, ?,?)",
                    new Object[]
                    { joke.getTitle(), joke.getContent(), joke.getType(),
                            currentTime });
            DbMonitor.monitorWhenNeed(MONITOR_TYPE.ADD, joke);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("添加Joke时异常," + ex.getMessage());
        }
        return true;
    }

    public static boolean updateSubject(Joke joke)
    {
        if (joke == null)
        {
            return false;
        }
        try
        {
            ContentValues cv = new ContentValues();
            cv.put("_id", joke.getId());
            cv.put("title", joke.getTitle());
            cv.put("content", joke.getContent());
            cv.put("type", joke.getType());
            db.update("joke", cv, "_id = ?", new String[]
            { String.valueOf(joke.getId()) });

            DbMonitor.monitorWhenNeed(MONITOR_TYPE.UPDATE, joke);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("修改Joke时异常," + ex.getMessage());
        }

        return true;
    }

    private static String getStrValue(Cursor c, String columnName)
    {
        return c.getString(c.getColumnIndex(columnName));
    }

    /**
     * 返回空表示真的没有数据了, 抛出异常则是操作失败
     * 
     * @param currentId
     * @param type
     * @return
     */
    public static Joke getCurJokeByType(int currentId, String type)
    {
        Log.i("dao", "currentId:" + currentId + " type:" + type);
        Joke subject = null;
        if (db == null)
        {
            Log.e("dao", "Not opened Db !");
            throw new RuntimeException("Not opened Db!");
        }
        Cursor c = null;
        try
        {
            c = db.rawQuery(
                    "SELECT * FROM joke WHERE ISDEL= 0 AND type like ? order by _id asc limit ?,1 ",
                    new String[]
                    { type, String.valueOf(currentId) });
            if (c.moveToNext())
            {
                subject = new Joke();
                subject.setId(BasicNumberUtil.getNumber(getStrValue(c, "_id")));
                subject.setTitle(getStrValue(c, "title"));
                subject.setContent(getStrValue(c, "content"));
                subject.setType(getStrValue(c, "type"));
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("数据操作错误:" + ex.getMessage());
        }
        finally
        {
            if (c != null)
            {
                c.close();
            }
        }
        return subject;
    }

    public static boolean deleteById(int id)
    {
        if (db == null)
        {
            Log.e("dao", "Not opened Db !");
            return false;
        }
        try
        {
            ContentValues cv = new ContentValues();
            cv.put("isdel", 1);
            db.update("joke", cv, "_id = ?", new String[]
            { String.valueOf(id) });
            DbMonitor.monitorWhenNeed(MONITOR_TYPE.DEL, id);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("修改joke时异常," + ex.getMessage());
        }
        return true;
    }

    public static int getTotalByType(String searchCondition)
    {
        if (db == null)
        {
            Log.e("dao", "Not opened Db !");
            throw new RuntimeException("Not opened Db!");
        }
        try
        {
            Cursor c = db
                    .rawQuery(
                            "SELECT COUNT(*) COUNT FROM joke WHERE ISDEL= 0 AND type like ?",
                            new String[]
                            { searchCondition });
            if (c.moveToNext())
            {
                return Integer.parseInt(getStrValue(c, "COUNT"));
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("查询joke时异常," + ex.getMessage());
        }
        return 0;
    }

}
