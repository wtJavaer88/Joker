package com.example.joker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joker.bean.Joke;
import com.example.joker.dao.JokeDao;
import com.example.joker.service.JokeRandomGenerator;
import com.example.joker.util.app.TextHelper;
import com.example.joker.util.common.ToastUtil;
import com.wnc.string.BeanToStringUtil;

@SuppressLint("ValidFragment")
public class ViewJokeFragment extends Fragment
{
    TextView tvTit;
    TextView tvContent;
    String curTitle;
    private String curContent = "";
    private Joke curJoke;
    private ViewJokeType curType = ViewJokeType.NEXT;

    Button preBt;
    Button nextBt;
    Button modifyBt;
    Button deleteBt;
    Button likeBt;
    JokeRandomGenerator jokeGenerator;

    String sqlCondition;
    public boolean hasExecute = false;

    public ViewJokeFragment(String sqlCondition)
    {
        this.sqlCondition = sqlCondition;
        this.jokeGenerator = new JokeRandomGenerator(sqlCondition);
    }

    View view;
    private boolean isEnjoyTab = false;

    public ViewJokeFragment inEnjoyTab()
    {
        this.isEnjoyTab = true;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.activity_viewsubject, null);
        initComp();
        return this.view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        JokeDao.initDb(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        if (!this.hasExecute)
        {
            this.hasExecute = true;
            getJokeAndFill();
        }

        super.onActivityCreated(savedInstanceState);
    }

    private void initComp()
    {
        this.tvTit = (TextView) this.view.findViewById(R.id.tvTitle);
        this.tvTit.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvTit.setText(this.curTitle);

        this.tvContent = (TextView) this.view.findViewById(R.id.tvContent);
        // this.tvContent.setMovementMethod(new ScrollingMovementMethod());
        this.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvContent.setGravity(Gravity.LEFT | Gravity.TOP);
        this.tvContent.setClickable(true);
        fillJokeText(this.curJoke);

        this.preBt = (Button) this.view.findViewById(R.id.preBt);
        preBtViewCtrl();
        this.nextBt = (Button) this.view.findViewById(R.id.nextBt);
        nextBtViewCtrl();
        this.modifyBt = (Button) this.view.findViewById(R.id.modifyBt);
        this.deleteBt = (Button) this.view.findViewById(R.id.deleteBt);
        this.likeBt = (Button) this.view.findViewById(R.id.likeBt);

        this.tvContent.setOnClickListener(new MyOnClickListener());
        this.nextBt.setOnClickListener(new MyOnClickListener());
        this.preBt.setOnClickListener(new MyOnClickListener());
        this.modifyBt.setOnClickListener(new MyOnClickListener());
        this.deleteBt.setOnClickListener(new MyOnClickListener());
        this.likeBt.setOnClickListener(new MyOnClickListener());

        if (this.isEnjoyTab)
        {
            this.likeBt.setText("取消收藏");
        }
        // otherBtsViewCtrl(false);
    }

    public enum ViewJokeType
    {
        NEXT, PRE;
    }

    public void getJokeAndFill()
    {
        try
        {
            getJoke();
        }
        catch (Exception ex)
        {
            ToastUtil.showShortToast(getActivity(), ex.getMessage());
        }

    }

    private void fillJokeText(Joke Joke)
    {
        if (Joke != null)
        {
            setTitle(Joke);
            setContent(Joke);
        }
    }

    private void setContent(Joke Joke)
    {
        this.curContent = Joke.getContent();
        ViewJokeFragment.this.tvContent.setText(this.curContent);
        this.tvContent.scrollTo(0, 0);
    }

    private void setTitle(Joke Joke)
    {
        this.curTitle = "  " + Joke.getId() + "." + Joke.getTitle();
        this.tvTit.setText(TextHelper.getHtmlFormatText(this.curTitle));
    }

    private void otherBtsViewCtrl(boolean b)
    {
        this.nextBt.setEnabled(b);
        this.modifyBt.setEnabled(b);
        this.deleteBt.setEnabled(b);
        this.likeBt.setEnabled(b);
    }

    private void nextBtViewCtrl()
    {
        if (this.jokeGenerator.checkNextable())
        {
            this.nextBt.setEnabled(Boolean.TRUE);
        }
        else
        {
            this.nextBt.setEnabled(Boolean.FALSE);
        }
    }

    private void preBtViewCtrl()
    {
        if (this.jokeGenerator.checkPreable())
        {
            this.preBt.setEnabled(Boolean.TRUE);
        }
        else
        {
            this.preBt.setEnabled(Boolean.FALSE);
        }
    }

    private void getJoke()
    {

        new Thread(new Runnable()
        {
            Message msg = new Message();

            @Override
            public void run()
            {
                if (!jokeGenerator.initTotal)
                {
                    jokeGenerator.computeTotal();
                }
                switch (curType)
                {
                case NEXT:
                    msg.obj = jokeGenerator.getNextJoke();
                    break;
                case PRE:
                    msg.obj = jokeGenerator.getPreJoke();
                    break;
                }
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                Joke tmp = (Joke) msg.obj;

                curJoke = tmp;
                otherBtsViewCtrl(curJoke != null);

                if (tmp != null)
                {
                    try
                    {
                        Log.i("viewJoke_Bean",
                                BeanToStringUtil.getBeanString(tmp).replace(
                                        "\n", ""));
                        fillJokeText(curJoke);
                        preBtViewCtrl();
                        nextBtViewCtrl();
                    }
                    catch (Exception e)
                    {
                        Log.e("viewJoke",
                                "getBeanString()  Error" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                else
                {
                    if (curType == ViewJokeType.NEXT)
                    {
                        nextBtViewCtrl();
                        ToastUtil.showShortToast(
                                ViewJokeFragment.this.getActivity(),
                                "已经没有下一条记录了!");
                    }
                    else
                    {
                        preBtViewCtrl();
                        ToastUtil.showShortToast(
                                ViewJokeFragment.this.getActivity(),
                                "上一条记录为空异常!");
                    }
                }
            }
        };
    };

    public void nextJoke()
    {
        if (this.jokeGenerator.checkNextable())
        {
            this.nextBt.setEnabled(Boolean.TRUE);
            curType = ViewJokeType.NEXT;
            getJokeAndFill();
        }
    }

    public void preJoke()
    {
        if (this.jokeGenerator.checkPreable())
        {
            this.preBt.setEnabled(Boolean.TRUE);
            curType = ViewJokeType.PRE;
            getJokeAndFill();
        }
    }

    public void modifyJoke()
    {
    }

    public void likeJoke()
    {
        String theme = this.likeBt.getText().toString().replace(" ", "");
        Joke cloneJoke = this.curJoke.clone();

        if (this.isEnjoyTab)
        {
            cloneJoke.setType(this.curJoke.getType().replace("7", ""));
            System.out.println(cloneJoke.getType() + " Joke.getType()  "
                    + this.curJoke.getType());
        }
        else
        {
            if (cloneJoke.getType().contains("7"))
            {
                ToastUtil.showShortToast(getActivity(), "已经收藏过!");
                return;
            }
            cloneJoke.setType(this.curJoke.getType() + " 7");
        }

        try
        {
            if (JokeDao.updateSubject(cloneJoke))
            {
                ToastUtil.showShortToast(getActivity(), theme + "成功!");
            }
            else
            {
                ToastUtil.showShortToast(getActivity(), theme + "失败!");
            }
        }
        catch (Exception ex)
        {
            Log.e("shoucang", ex.getMessage());
            ToastUtil.showShortToast(getActivity(), "异常!" + ex.getMessage());
        }
    }

    Dialog delAlertDialog = null;

    public void deleteJoke()
    {
        this.delAlertDialog = new AlertDialog.Builder(this.getActivity())
                .setTitle("确定删除？")
                .setMessage("您确定删除该笑话吗？")
                .setIcon(R.drawable.ic_launcher)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        System.out.println("Delete ");
                        try
                        {
                            if (JokeDao
                                    .deleteById(ViewJokeFragment.this.curJoke
                                            .getId()))
                            {
                                ToastUtil.showShortToast(
                                        ViewJokeFragment.this.getActivity(),
                                        "成功删除!");
                            }
                        }
                        catch (Exception ex)
                        {
                            ToastUtil.showShortToast(
                                    ViewJokeFragment.this.getActivity(),
                                    "删除异常:" + ex.getMessage());
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ViewJokeFragment.this.delAlertDialog.dismiss();
                    }

                }).create();
        this.delAlertDialog.show();
    }

    public void showAnswer(View v)
    {
        this.tvContent.setVisibility(View.VISIBLE);
    }

    class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            case R.id.tvContent:
                nextJoke();
                break;
            case R.id.nextBt:
                nextJoke();
                break;
            case R.id.preBt:
                preJoke();
                break;
            case R.id.modifyBt:
                modifyJoke();
                break;
            case R.id.deleteBt:
                deleteJoke();
                break;
            case R.id.likeBt:
                likeJoke();
                break;
            default:
                ToastUtil.showShortToast(ViewJokeFragment.this.getActivity(),
                        "错误的点击事件!");

            }
        }
    }

    /***
     * 答案的 显示/隐藏 控制
     * 
     * @param flag
     */
    public void switchAnswer(boolean flag)
    {

    }

}
