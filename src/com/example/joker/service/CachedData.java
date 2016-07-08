package com.example.joker.service;

import java.util.HashMap;
import java.util.Map;

import com.example.joker.bean.Joke;

public class CachedData
{
    static Map<Integer, Joke> map = new HashMap<Integer, Joke>();

    public synchronized static void saveJoke(Joke joke)
    {
        int id = joke.getId();
        if (!map.containsKey(id))
        {
            map.put(id, joke);
        }
    }

    public static Joke getJoke(Integer jokeId)
    {
        return map.get(jokeId);
    }

    public static int getJokeCount()
    {
        return map.size();
    }

}
