package com.example.joker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.example.joker.dao.JokeDao;
import com.example.joker.service.LogService;
import com.example.joker.util.data.BackUpDataUtil;
import com.example.joker.util.data.SysInit;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity
{
    /**
     * ѡ�����
     */
    // private int TAB_COUNT = 0;
    List<String> titles = new ArrayList<String>();
    /**
     * ѡ�����
     */
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private Map<String, Integer> map = new HashMap<String, Integer>(10);
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Ц�������� (^_^)");
        // ��ֹ����
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        System.out.println("AAA");
        SysInit.init(this);
        JokeDao.initDb(this);
        Intent logService = new Intent(this, LogService.class);
        startService(logService);
        System.out.println("BBB");

        initMap();
        initFragments();

        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int arg0)
            {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });
    }

    private void initFragments()
    {
        Fragment fragment = new ViewJokeFragment("%%");
        this.fragmentList.add(fragment);
        addTagString("ȫ��");

        for (Map.Entry<String, Integer> entry : this.map.entrySet())
        {
            String str = entry.getKey();
            Log.i("mytabs", "��Ч��ǩ:" + str);
            if (!str.equals("�ҵ��ղ�"))
            {
                fragment = new ViewJokeFragment("%" + entry.getValue() + "%");
            }
            else
            {
                fragment = new ViewJokeFragment("%" + entry.getValue() + "%")
                        .inEnjoyTab();
            }
            this.fragmentList.add(fragment);
            addTagString(str);
        }
    }

    private void initMap()
    {
        this.map.put("��Ĭ", 0);
        this.map.put("��ͥ", 1);
        this.map.put("У԰", 2);
        this.map.put("����", 3);
        this.map.put("�ֲ�", 4);
        this.map.put("���", 5);
        this.map.put("������¼", 6);
        this.map.put("�ҵ��ղ�", 7);
    }

    @Override
    public void onDestroy()
    {
        try
        {
            super.onDestroy();
            BackUpDataUtil.backup(getApplicationContext());
        }
        catch (Exception ex)
        {
            Log.e("err", ex.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    private void addTagString(String tagname)
    {
        this.titles.add(tagname);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        ViewJokeFragment fm = (ViewJokeFragment) ((TabPageIndicatorAdapter) adapter).currentFragment;
        switch (keyCode)
        {
        case KeyEvent.KEYCODE_BACK:
            JokeDao.closeDb();
            return super.onKeyDown(keyCode, event);
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            System.out.println("down vol");
            fm.nextJoke();
            return true;

        case KeyEvent.KEYCODE_VOLUME_UP:
            System.out.println("up vol");
            fm.preJoke();
            return true;
        case KeyEvent.KEYCODE_VOLUME_MUTE:
            System.out.println("mute vol");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            return true;
        case KeyEvent.KEYCODE_VOLUME_UP:
            return true;
        case KeyEvent.KEYCODE_VOLUME_MUTE:
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * ViewPager������
     * 
     * @author len
     * 
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter
    {
        final int tabcounts = MainActivity.this.titles.size();

        public TabPageIndicatorAdapter(FragmentManager fm)
        {
            super(fm);
        }

        Fragment currentFragment;

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                Object object)
        {
            currentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position)
        {
            return MainActivity.this.fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return MainActivity.this.titles.get(position % this.tabcounts);
        }

        @Override
        public int getCount()
        {
            return MainActivity.this.titles.size();
        }
    }
}