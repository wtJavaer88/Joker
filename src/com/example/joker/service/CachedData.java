package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;

public class CachedData
{
    // 数据库中的位置与对应的Joke
    static Map<Integer, Joke> map = new HashMap<Integer, Joke>();

    public synchronized static void saveJoke(int index, Joke joke)
    {
        map.put(index, joke);
    }

    public static Joke getJoke(Integer index)
    {
        return map.get(index);
    }

    public static int getJokeCount()
    {
        return map.size();
    }

}
