package com.openxu.anima;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

    private int ITEM_WIDTH;   //每小项的宽高

    private int limit;          //可见条目数量
    private float textSize;
    private int textColor;
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
            limit = 5;

            textSize = ta.getDimension(R.styleable.LimitScroller_android_textSize, 15f);
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            textSize = textSize / fontScale + 0.5f;
            textColor = ta.getColor(R.styleable.LimitScroller_android_textColor, Color.BLACK);

            durationTime = ta.getInt(R.styleable.LimitScroller_durationTime, 1000);
            periodTime = ta.getInt(R.styleable.LimitScroller_periodTime, 1000);
            ta.recycle();  //注意回收
        }

        tv_1.setTextSize(textSize);
        tv_2.setTextSize(textSize);
        tv_3.setTextSize(textSize);
        tv_4.setTextSize(textSize);
        tv_5.setTextSize(textSize);
        tv_6.setTextSize(textSize);
        tv_7.setTextSize(textSize);
        tv_1.setTextColor(textColor);
        tv_2.setTextColor(textColor);
        tv_3.setTextColor(textColor);
        tv_4.setTextColor(textColor);
        tv_5.setTextColor(textColor);
        tv_6.setTextColor(textColor);
        tv_7.setTextColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "测量完成，宽度*高度="+getMeasuredWidth()+"*"+getMeasuredHeight());
        ITEM_WIDTH = getMeasuredWidth()/limit;
        Log.w(TAG, "每小项尺寸："+ITEM_WIDTH+"*"+getMeasuredHeight());

        measureInit();
    }

    private void measureInit(){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)ll_1.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_1.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_2.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_2.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_3.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_3.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_4.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_4.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_5.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_5.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_6.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_6.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_7.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_7.setLayoutParams(lp);
        //初始化时，让周日在左边隐藏备用
        ll_7.setX(-ITEM_WIDTH);

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