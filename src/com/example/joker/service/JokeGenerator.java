package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;
import com.example.joker.dao.JokeDao;

public class JokeGenerator
{
    private int cursor = -1;
    private final String searchCondition;
    private Map<Integer, Joke> jokes;
    private int total = 0;

    public JokeGenerator(String searchCondition)
    {
        this.jokes = new HashMap<Integer, Joke>();
        this.searchCondition = searchCondition;
        total = JokeDao.getTotalByType(searchCondition);
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
            return jokes.get(cursor);
        }

        try
        {
            joke = JokeDao.getCurJokeByType(cursor, this.searchCondition);
            jokes.put(cursor, joke);
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

    public boolean checkNextable()
    {
        if (cursor < total - 1)
        {
            return true;
        }
        return false;
    }
}
