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
    public int total = 0;
    public boolean initTotal = false;

    public JokeRandomGenerator(String searchCondition)
    {
        this.jokes = new HashMap<Integer, Joke>();
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
            return jokes.get(cursor);
        }

        int id = (int) (Math.random() * total);
        System.out.println(total + "  " + id);
        try
        {
            joke = JokeDao.getCurJokeByType(id, this.searchCondition);
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
        return true;
    }
}
