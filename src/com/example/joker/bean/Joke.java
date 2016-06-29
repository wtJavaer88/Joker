package com.example.joker.bean;

import java.io.Serializable;

import com.wnc.string.BeanToStringUtil;

public class Joke implements Serializable, Cloneable
{
    private int id;

    public Joke()
    {

    }

    public Joke(String title, String content)
    {
        setTitle(title);
        setContent(content);
    }

    @Override
    public Joke clone()
    {
        Joke o = null;
        try
        {
            o = (Joke) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return o;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    private String title;// �}Ŀ��Q

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getRecordTime()
    {
        return this.recordTime;
    }

    public void setRecordTime(String recordTime)
    {
        this.recordTime = recordTime;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSrcFrom()
    {
        return this.srcFrom;
    }

    public void setSrcFrom(String srcFrom)
    {
        this.srcFrom = srcFrom;
    }

    public int getStarLevel()
    {
        return this.starLevel;
    }

    public void setStarLevel(int starLevel)
    {
        this.starLevel = starLevel;
    }

    public int getViewTimes()
    {
        return this.viewTimes;
    }

    public void setViewTimes(int viewTimes)
    {
        this.viewTimes = viewTimes;
    }

    @Override
    public String toString()
    {
        String ret = null;
        try
        {
            ret = "Subject:" + BeanToStringUtil.getBeanString(this);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    private String content;
    private String recordTime;// ����ʱ��
    private String type;// �ؼ���
    private String srcFrom;// ��Դ
    private int starLevel;// �Ǽ�
    private int viewTimes;// ���ͣ���Ĵ���
}
