package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;
import com.example.joker.dao.JokeDao;

public class JokeRandomGenerator
{
    private int cursor = -1;
    private final String searchCondition;
    public Map<Integer, Joke> jokes;
    public Map<Integer, Integer> idCursors;
    public int total = 0;
    public boolean initTotal = false;

    public JokeRandomGenerator(String searchCondition)
    {
        this.jokes = new HashMap<Integer, Joke>();
        this.idCursors = new HashMap<Integer, Integer>();
        this.searchCondition = searchCondition;
    }

    public void computeTotal()
    {
        total = JokeDao.getTotalByType(searchCondition);
        initTotal = true;
    }

    /**
     * 
     * @return
     */
    public Joke getNextJoke()
    {
        Joke joke = null;
        this.cursor++;
        if (hasContent() && jokes.containsKey(cursor))
        {
            joke = jokes.get(cursor);
            jokes.put(cursor, joke);
            return joke;
        }

        int id = (int) (Math.random() * total);
        if (idCursors.containsKey(id))
        {
            joke = jokes.get(idCursors.get(id));
            jokes.put(cursor, joke);
            return joke;
        }
        try
        {
            // System.out.println("数据库操作.......");
            joke = JokeDao.getCurJokeByType(id, this.searchCondition);
            jokes.put(cursor, joke);
            idCursors.put(id, cursor);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }

        return joke;
    }

    private boolean hasContent()
    {
        return jokes.size() > 0 && jokes.size() > cursor;
    }

    private void rollbackCursor()
    {
        this.cursor--;
    }

    public Joke getPreJoke()
    {
        if (!checkPreable())
        {
            return null;
        }
        rollbackCursor();
        return this.jokes.get(this.cursor);
    }

    public boolean checkPreable()
    {
        if (this.cursor < 1)
        {
            return false;
        }
        return true;
    }

    /**
     * 随机模式永远没有结束
     * 
     * @return
     */
    public boolean checkNextable()
    {
        return true;
    }
}
