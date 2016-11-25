package com.openxu.anima;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * author : openXu
 * create at : 2016/11/25 16:25
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : JueAnimation
 * class name : CustomWeekView
 * version : 1.0
 * class describe：自定义日期控件
 */
public class CustomWeekView extends LinearLayout {

    private String TAG = "CustomWeekViewextends";

    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7;
    private LinearLayout ll_now, ll_down;   //当前可见的，下面不可见的（切换）
    private int limit;          //可见条目数量
    private int durationTime;   //动画执行时间
    private int periodTime;     //间隔时间
    private int scrollHeight;   //滚动高度（控件高度）

    private final int MSG_SETDATA = 1;
    private final int MSG_SCROL = 2;

    public CustomWeekView(Context context) {
        this(context, null);
    }

    public CustomWeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_week_layout, this, true);
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);
        ll_5 = (LinearLayout) findViewById(R.id.ll_5);
        ll_6 = (LinearLayout) findViewById(R.id.ll_6);
        ll_7 = (LinearLayout) findViewById(R.id.ll_7);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LimitScroller);
            limit = ta.getInt(R.styleable.LimitScroller_limit, 1);
            durationTime = ta.getInt(R.styleable.LimitScroller_durationTime, 1000);
            periodTime = ta.getInt(R.styleable.LimitScroller_periodTime, 1000);
            ta.recycle();  //注意回收
            Log.v(TAG, "limit=" + limit);
            Log.v(TAG, "durationTime=" + durationTime);
            Log.v(TAG, "periodTime=" + periodTime);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //EXACTLY=1073741824
        //AT_MOST=-2147483648
    /*        int specMode = MeasureSpec.getMode(heightMeasureSpec);
            int specSize = MeasureSpec.getSize(heightMeasureSpec);
            Log.w(TAG, "specMode="+specMode);
            Log.w(TAG, "specSize="+specSize);
            int newHeightSpec = MeasureSpec.makeMeasureSpec(specSize, MeasureSpec.EXACTLY);
            int childCount = ll_content1.getChildCount();
            if(childCount>0){
                View item = ll_content1.getChildAt(0);
                item.measure(widthMeasureSpec, newHeightSpec);
                Log.w(TAG, "条目高度="+   item.getMeasuredHeight());
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight()/2);
                scrollHeight = getMeasuredHeight();
                return;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);*/

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.v(TAG, "测量完成，宽度*高度="+getMeasuredWidth()+"*"+getMeasuredHeight());
        Log.v(TAG, "ll_1测量完成，宽度*高度="+ll_1.getMeasuredWidth()+"*"+ll_1.getMeasuredHeight());







        //设置高度为整体高度的一般，以达到遮盖预备容器的效果
//        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() / 2);
        //此处记下控件的高度，此高度就是动画执行时向上滚动的高度
//        scrollHeight = getMeasuredHeight();
        //        Log.w(TAG, "getMeasuredWidth="+getMeasuredWidth());
        //        Log.w(TAG, "getMeasuredHeight="+getMeasuredHeight());
        //        Log.w(TAG, "scrollHeight="+scrollHeight);
    }

    private void startAnimation() {
        Log.i(TAG, "滚动");
        //当前展示的容器，从当前位置（0）,向上滚动scrollHeight
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ll_now, "Y", ll_now.getY(), ll_now.getY() - scrollHeight);
        //预备容器，从当前位置，向上滚动scrollHeight
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ll_down, "Y", ll_down.getY(), ll_down.getY() - scrollHeight);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(durationTime);
        animSet.playTogether(anim1, anim2);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //                Log.v(TAG, "ll_now动画开始前位置："+ll_now.getX()+"*"+ll_now.getY());
                //                Log.v(TAG, "ll_down动画开始前位置："+ll_down.getX()+"*"+ll_down.getY());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //滚动结束后，now的位置变成了-scrollHeight，这时将他移动到最底下
                ll_now.setY(scrollHeight);
                //down的位置变为0，也就是当前看见的
                ll_down.setY(0);
                //                Log.v(TAG, "1调整之后ll_now位置："+ll_now.getX()+"*"+ll_now.getY());
                //                Log.v(TAG, "1调整之后ll_down位置："+ll_down.getX()+"*"+ll_down.getY());
                LinearLayout temp = ll_now;
                ll_now = ll_down;
                ll_down = temp;
                //                Log.v(TAG, "2调整之后ll_now位置："+ll_now.getX()+"*"+ll_now.getY());
                //                Log.v(TAG, "2调整之后ll_down位置："+ll_down.getX()+"*"+ll_down.getY());
                //给不可见的控件绑定新数据

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animSet.start();
    }

}