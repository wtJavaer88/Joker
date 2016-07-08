package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;
import com.example.joker.dao.JokeDao;

public class JokeRandomGenerator
{
    private int cursor = -1;
    private final String searchCondition;
    public Map<Integer, Integer> cursorIds;
    public int total = 0;
    public boolean initTotal = false;

    public JokeRandomGenerator(String searchCondition)
    {
        this.cursorIds = new HashMap<Integer, Integer>();
        this.searchCondition = searchCondition;
    }

    public void computeTotal()
    {
        total = JokeDao.getTotalByType(searchCondition);
        initTotal = true;
    }

    public Joke getPreJoke()
    {
        if (!checkPreable())
        {
            return null;
        }
        rollbackCursor();
        return getCached();
    }

    public Joke getNextJoke()
    {
        Joke joke = null;
        this.cursor++;
        if (hasLocalData())
        {
            joke = getCached();
        }
        else
        {
            int id = (int) (Math.random() * total);
            joke = CachedData.getJoke(id);
            if (joke != null)
            {
                cursorIds.put(cursor, id);
                return joke;
            }
            joke = computeNextFromDb(id);
        }
        return joke;
    }

    private Joke computeNextFromDb(int id)
    {
        Joke joke = null;
        try
        {
            joke = JokeDao.getCurJokeByType(id, this.searchCondition);
            CachedData.saveJoke(joke);
            cursorIds.put(cursor, id);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        return joke;
    }

    private void rollbackCursor()
    {
        this.cursor--;
    }

    // 算出当前cursor所指的缓存
    private Joke getCached()
    {
        Integer jokeId = cursorIds.get(cursor);
        Joke joke = CachedData.getJoke(jokeId);
        // 如果已经从缓存中删除, 则重新算
        if (joke == null)
        {
            joke = computeNextFromDb(jokeId);
        }
        return joke;
    }

    private boolean hasLocalData()
    {
        return cursorIds.containsKey(cursor);
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
