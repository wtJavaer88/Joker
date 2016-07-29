package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;
import com.example.joker.dao.JokeDao;

public class JokeRandomGenerator
{
    private int cursor = -1;
    private final String searchCondition;
    public Map<Integer, Integer> cursorIndexs;// 游标对应数据库中的位置
    public int total = 0;
    public boolean initTotal = false;

    public JokeRandomGenerator(String searchCondition)
    {
        this.cursorIndexs = new HashMap<Integer, Integer>();
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
        addCursor();
        if (hasLocalData())
        {
            joke = getCached();
        }
        else
        {
            int index = (int) (Math.random() * total);
            joke = CachedData.getJoke(index);
            if (joke != null)
            {
                cursorIndexs.put(cursor, index);
                return joke;
            }
            joke = computeNextFromDb(index);
        }
        return joke;
    }

    private Joke computeNextFromDb(int index)
    {
        Joke joke = null;
        try
        {
            joke = JokeDao.getCurJokeByType(index, this.searchCondition);
            CachedData.saveJoke(index, joke);
            cursorIndexs.put(cursor, index);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        return joke;
    }

    private synchronized void addCursor()
    {
        this.cursor++;
    }

    private synchronized void rollbackCursor()
    {
        this.cursor--;
    }

    // 算出当前cursor所指的缓存
    private Joke getCached()
    {
        System.out.println(CachedData.map);
        Integer index = cursorIndexs.get(cursor);
        Joke joke = CachedData.getJoke(index);
        // 如果已经从缓存中删除, 则重新算
        if (joke == null)
        {
            joke = computeNextFromDb(index);
        }
        return joke;
    }

    private boolean hasLocalData()
    {
        return cursorIndexs.containsKey(cursor);
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
