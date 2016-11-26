package com.openxu.anima;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
public class CustomWeekView extends LinearLayout implements View.OnClickListener {

    private String TAG = "CustomWeekViewextends";


    /**七天枚举*/
    private enum WEEKDAY{
        wk1,wk2,wk3,wk4,wk5,wk6,wk7
    }

    //字条目（5个当前显示、四个预备）
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7, ll_8, ll_9;
    //字条目中显示字体的TextView
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7, tv_8, tv_9;
    //用于存放所有字条目的引用
    private List<LinearLayout> llList;
    //根据索引获取 大小 展示星期几
    private String[] WEEK_STR = new String[]{"0","一","二","三","四","五","六","日"};
    //当前一周7天的日期
    private List<String> DATA_STR;
    /*自定义属性*/
    private float textSize;
    private float dateTextSize;
    private int textColor;
    private int dateTextColor;
    private int durationTime;   //动画执行时间
    private float scaleSize;    //缩放幅度


    private WEEKDAY centerNow;  //当前显示在中间的是星期X
    private int ITEM_WIDTH;     //每小项的宽高
    private int limit;          //可见条目数量

    private boolean animalFinish = false;   //动画期间不嫩点击

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
        ll_8 = (LinearLayout) findViewById(R.id.ll_8);
        ll_9 = (LinearLayout) findViewById(R.id.ll_9);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LimitScroller);
            limit = 5;

            textSize = ta.getDimension(R.styleable.LimitScroller_android_textSize, 15f);
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            textSize = textSize / fontScale + 0.5f;
            dateTextSize = ta.getInteger(R.styleable.LimitScroller_dateTextSize, 12);

            textColor = ta.getColor(R.styleable.LimitScroller_android_textColor, Color.BLACK);
            dateTextColor = ta.getColor(R.styleable.LimitScroller_dateTextColor, Color.RED);
            durationTime = ta.getInt(R.styleable.LimitScroller_durationTime, 1000);
            scaleSize = ta.getFloat(R.styleable.LimitScroller_scaleSize, 0);
            ta.recycle();  //注意回收
        }

        tv_1.setTextSize(textSize);
        tv_2.setTextSize(textSize);
        tv_3.setTextSize(textSize);
        tv_4.setTextSize(textSize);
        tv_5.setTextSize(textSize);
        tv_6.setTextSize(textSize);
        tv_7.setTextSize(textSize);
        tv_8.setTextSize(textSize);
        tv_9.setTextSize(textSize);
        tv_1.setTextColor(textColor);
        tv_2.setTextColor(textColor);
        tv_3.setTextColor(textColor);
        tv_4.setTextColor(textColor);
        tv_5.setTextColor(textColor);
        tv_6.setTextColor(textColor);
        tv_7.setTextColor(textColor);
        tv_8.setTextColor(textColor);
        tv_9.setTextColor(textColor);

        llList = new ArrayList<>();
        llList.add(ll_1);
        llList.add(ll_2);
        llList.add(ll_3);
        llList.add(ll_4);
        llList.add(ll_5);
        llList.add(ll_6);
        llList.add(ll_7);
        llList.add(ll_8);
        llList.add(ll_9);

        DATA_STR = new ArrayList<>();
        //日历
        Calendar calend = Calendar.getInstance();
        int firstData = calend.getFirstDayOfWeek();
        Log.e(TAG, "================本周第一天时间："+firstData);
        DATA_STR.add("");
        DATA_STR.add("11/21");
        DATA_STR.add("11/22");
        DATA_STR.add("11/23");
        DATA_STR.add("11/24");
        DATA_STR.add("11/25");
        DATA_STR.add("11/26");
        DATA_STR.add("11/27");

        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);
        ll_5.setOnClickListener(this);
        ll_6.setOnClickListener(this);
        ll_7.setOnClickListener(this);
        ll_8.setOnClickListener(this);
        ll_9.setOnClickListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "测量完成，宽度*高度="+getMeasuredWidth()+"*"+getMeasuredHeight());
        ITEM_WIDTH = getMeasuredWidth()/limit;
        Log.w(TAG, "每小项尺寸："+ITEM_WIDTH+"*"+getMeasuredHeight());

        measureInit();
    }

    /**根据屏幕的宽度和显示的个数，设置item的宽度*/
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
        lp = (LinearLayout.LayoutParams)ll_8.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_8.setLayoutParams(lp);
        lp = (LinearLayout.LayoutParams)ll_9.getLayoutParams();
        lp.width = ITEM_WIDTH;
        ll_9.setLayoutParams(lp);

        Log.v(TAG, "位置："+ll_1.getX()+" "+ll_2.getX()+" "+ll_3.getX()+" "
                +ll_4.getX()+" "+ll_5.getX()+" "+ll_6.getX()
                +" "+ll_7.getX()+" "+ll_8.getX()+" "+ll_9.getX());

        if(ll_2.getX()>0) {
            //默认周3在中间
            setCenter(WEEKDAY.wk3);
        }

    }


    /*这些引用代表当前正在显示的5个条目 和 四个预备条目，
     *由于ll_x系列条目是不断移动的，所以此处需要根据ll_x的位置重新为llx赋值
     * 其中ll1-ll5为当前正在显示的条目， ll6、ll7为右边隐藏的预备条目， ll8、ll9为左边的隐藏预备条目
     */
    private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7, ll8, ll9;
    /**1、找到正在展示的五个item，并将预备item复位*/
    private void setCenter(WEEKDAY weekDay){
        //记录当前显示在中间的星期X
        centerNow = weekDay;
        //1、找到当前显示的5个条目的位置
        List<LinearLayout> list = new ArrayList<>(llList);
        for(int i = 0; i<5; i++){
            for(int j = 0; j<list.size(); j++){
                LinearLayout ll = list.get(j);
                if(ll.getX()==ITEM_WIDTH*i){
                    list.remove(ll);      //找到之后就remove可以减少后面遍历的次数
                    Log.d(TAG, "找到"+i+"了"+ll);
                    switch (i) {
                        case 0:
                            ll1 = ll;
                            break;
                        case 1:
                            ll2 = ll;
                            break;
                        case 2:
                            ll3 = ll;
                            //当前中间位置item放大
                            ll3.getChildAt(0).setScaleX(scaleSize);
                            ll3.getChildAt(0).setScaleY(scaleSize);
                            break;
                        case 3:
                            ll4 = ll;
                            break;
                        case 4:
                            ll5 = ll;
                            break;
                    }
                }
            }
        }
        Log.i(TAG, "找完后还剩"+list.size()+"  总："+llList.size());
        //2、剩余的四个作为预备，归位，左边隐藏两个，右边隐藏两个
        for(int i = 0; i<list.size(); i++){
            LinearLayout ll = list.get(i);
            switch (i){
                case 0:   //左1
                    ll.setX(-ITEM_WIDTH*2);
                    ll8=ll;
                    break;
                case 1:   //左2
                    ll.setX(-ITEM_WIDTH*1);
                    ll9=ll;
                    break;
                case 2:   //右1
                    ll.setX(ITEM_WIDTH*5);
                    ll6=ll;
                    break;
                case 3:   //右2
                    ll.setX(ITEM_WIDTH*6);
                    ll7=ll;
                    break;
            }
        }

        reBoundDataByCenter(weekDay);

    }

    /**2、重新绑定数据*/
    private void reBoundDataByCenter(WEEKDAY weekDay){
        if(weekDay == WEEKDAY.wk1){
            /*星期1在中间，依次为4、5、6、7、1、2、3、4、5*/
            setLLText(ll8, 4);
            setLLText(ll9, 5);
            setLLText(ll1, 6);
            setLLText(ll2, 7);
            setLLText(ll3, 1);
            setLLText(ll4, 2);
            setLLText(ll5, 3);
            setLLText(ll6, 4);
            setLLText(ll7, 5);
        }else if(weekDay == WEEKDAY.wk2){
            /*星期2在中间，依次为5、6、7、1、2、3、4、5、6*/
            setLLText(ll8, 5);
            setLLText(ll9, 6);
            setLLText(ll1, 7);
            setLLText(ll2, 1);
            setLLText(ll3, 2);
            setLLText(ll4, 3);
            setLLText(ll5, 4);
            setLLText(ll6, 5);
            setLLText(ll7, 6);
        }else if(weekDay == WEEKDAY.wk3){
            /*星期3在中间，依次为6、7、1、2、3、4、5、6、7*/
            setLLText(ll8, 6);
            setLLText(ll9, 7);
            setLLText(ll1, 1);
            setLLText(ll2, 2);
            setLLText(ll3, 3);
            setLLText(ll4, 4);
            setLLText(ll5, 5);
            setLLText(ll6, 6);
            setLLText(ll7, 7);
        }else if(weekDay == WEEKDAY.wk4){
            /*星期4在中间，依次为7、1、2、3、4、5、6、7、1*/
            setLLText(ll8, 7);
            setLLText(ll9, 1);
            setLLText(ll1, 2);
            setLLText(ll2, 3);
            setLLText(ll3, 4);
            setLLText(ll4, 5);
            setLLText(ll5, 6);
            setLLText(ll6, 7);
            setLLText(ll7, 1);
        }else if(weekDay == WEEKDAY.wk5){
            /*星期5在中间，依次为1、2、3、4、5、6、7、1、2*/
            setLLText(ll8, 1);
            setLLText(ll9, 2);
            setLLText(ll1, 3);
            setLLText(ll2, 4);
            setLLText(ll3, 5);
            setLLText(ll4, 6);
            setLLText(ll5, 7);
            setLLText(ll6, 1);
            setLLText(ll7, 2);
        }else if(weekDay == WEEKDAY.wk6){
            /*星期6在中间，依次为2、3、4、5、6、7、1、2、3*/
            setLLText(ll8, 2);
            setLLText(ll9, 3);
            setLLText(ll1, 4);
            setLLText(ll2, 5);
            setLLText(ll3, 6);
            setLLText(ll4, 7);
            setLLText(ll5, 1);
            setLLText(ll6, 2);
            setLLText(ll7, 3);
        }else if(weekDay == WEEKDAY.wk7){
            /*星期7在中间，依次为3、4、5、6、7、1、2、3、4*/
            setLLText(ll8, 3);
            setLLText(ll9, 4);
            setLLText(ll1, 5);
            setLLText(ll2, 6);
            setLLText(ll3, 7);
            setLLText(ll4, 1);
            setLLText(ll5, 2);
            setLLText(ll6, 3);
            setLLText(ll7, 4);
        }
        animalFinish = true;
    }

    private String setLLText(LinearLayout ll, int witchDay){
        ll.setTag(witchDay);   //便于区分点击事件
        TextView tv = (TextView)ll.getChildAt(0);
        String text = "星期" + WEEK_STR[witchDay];
        tv.setText(text);
        return text;
    }

    @Override
    public void onClick(View v) {
        if(!animalFinish){
            Log.e(TAG, "动画还没执行完毕，不能点击");
            return;
        }
        switch (v.getId()){
            case R.id.ll_1:
                setClickWitch(ll_1);
                break;
            case R.id.ll_2:
                setClickWitch(ll_2);
                break;
            case R.id.ll_3:
                setClickWitch(ll_3);
                break;
            case R.id.ll_4:
                setClickWitch(ll_4);
                break;
            case R.id.ll_5:
                setClickWitch(ll_5);
                break;
            case R.id.ll_6:
                setClickWitch(ll_6);
                break;
            case R.id.ll_7:
                setClickWitch(ll_7);
                break;
            case R.id.ll_8:
                setClickWitch(ll_8);
                break;
            case R.id.ll_9:
                setClickWitch(ll_9);
                break;
        }
    }

    private void setClickWitch(LinearLayout ll){
        Log.v(TAG, "点击的item:"+ll.toString().substring(ll.toString().lastIndexOf("ll_"),ll.toString().length())+
                   "  TAG="+ll.getTag()+" 显示的是：星期"+WEEK_STR[(int)ll.getTag()]);
        startAnimation(getEnumByNum((int)ll.getTag()), ll);
    }


    private WEEKDAY getEnumByNum(int num){
        switch (num){
            case 1:
                return WEEKDAY.wk1;
            case 2:
                return WEEKDAY.wk2;
            case 3:
                return WEEKDAY.wk3;
            case 4:
                return WEEKDAY.wk4;
            case 5:
                return WEEKDAY.wk5;
            case 6:
                return WEEKDAY.wk6;
            case 7:
                return WEEKDAY.wk7;
        }
        return WEEKDAY.wk1;

    }


    private void startAnimation(final WEEKDAY centerWitch, LinearLayout llClickView) {

        if(centerWitch==centerNow)
            return;
        animalFinish = false;

        String date = DATA_STR.get((int)llClickView.getTag());
        TextView tv_date = new TextView(getContext());
        tv_date.setTextSize(dateTextSize);
        tv_date.setTextColor(dateTextColor);
        tv_date.setText(date);
        llClickView.addView(tv_date);



        //根据当前中间位置显示的 和 被点击的日期，获取需要偏移的增量
        int offset = getXOffset(centerWitch);
        Log.d(TAG, "当前中间为"+centerNow+"，点击的是"+centerWitch+ "  偏移量："+offset);

        //当前中间位置的需要缩放到原尺寸
        Log.v(TAG, "中间item缩放量scaleX＝"+ll3.getChildAt(0).getScaleX()+" scaleY="+ll3.getChildAt(0).getScaleY());
        ObjectAnimator anim100 = ObjectAnimator.ofFloat(ll3.getChildAt(0), "scaleX", ll3.getChildAt(0).getScaleX(), 1.0f);
        ObjectAnimator anim101 = ObjectAnimator.ofFloat(ll3.getChildAt(0), "scaleY", ll3.getChildAt(0).getScaleY(), 1.0f);
        //被点击的需要放大
        ObjectAnimator anim102 = ObjectAnimator.ofFloat(llClickView.getChildAt(0), "scaleX", 1, scaleSize);
        ObjectAnimator anim103 = ObjectAnimator.ofFloat(llClickView.getChildAt(0), "scaleY", 1, scaleSize);

        //透明度动画
        ObjectAnimator anim104 = ObjectAnimator.ofFloat(llClickView.getChildAt(0), "scaleY", 1, scaleSize);


        //位移动画
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ll_1, "X", ll_1.getX(), ll_1.getX() + offset);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ll_2, "X", ll_2.getX(), ll_2.getX() + offset);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(ll_3, "X", ll_3.getX(), ll_3.getX() + offset);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(ll_4, "X", ll_4.getX(), ll_4.getX() + offset);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(ll_5, "X", ll_5.getX(), ll_5.getX() + offset);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(ll_6, "X", ll_6.getX(), ll_6.getX() + offset);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(ll_7, "X", ll_7.getX(), ll_7.getX() + offset);
        ObjectAnimator anim8 = ObjectAnimator.ofFloat(ll_8, "X", ll_8.getX(), ll_8.getX() + offset);
        ObjectAnimator anim9 = ObjectAnimator.ofFloat(ll_9, "X", ll_9.getX(), ll_9.getX() + offset);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(durationTime);
        animSet.playTogether(anim100, anim101, anim102, anim103, anim1, anim2, anim3, anim4, anim5, anim6, anim7, anim8, anim9);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {

                Log.w(TAG, "动画结束后位置："+ll_1.getX()+" "+ll_2.getX()+" "+ll_3.getX()+" "
                        +ll_4.getX()+" "+ll_5.getX()+" "+ll_6.getX()
                        +" "+ll_7.getX()+" "+ll_8.getX()+" "+ll_9.getX());

                setCenter(centerWitch);
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

    /**获取偏移量*/
    private int getXOffset(WEEKDAY centerWitch) {
        int offset = 0;
        if(centerNow==WEEKDAY.wk1){
            /*星期1在中间，依次为6、7、1、2、3*/
            if(centerWitch==WEEKDAY.wk6){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk7){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk2){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk3){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk2){
            /*星期1在中间，依次为7、1、2、3、4*/
            if(centerWitch==WEEKDAY.wk7){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk1){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk3){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk4){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk3){
            /*星期1在中间，依次为1、2、3、4、5*/
            if(centerWitch==WEEKDAY.wk1){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk2){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk4){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk5){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk4){
            /*星期1在中间，依次为2、3、4、5、6*/
            if(centerWitch==WEEKDAY.wk2){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk3){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk5){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk6){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk5){
            /*星期1在中间，依次为3、4、5、6、7*/
            if(centerWitch==WEEKDAY.wk3){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk4){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk6){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk7){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk6){
            /*星期1在中间，依次为4、5、6、7、1*/
            if(centerWitch==WEEKDAY.wk4){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk5){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk7){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk1){
                offset = ITEM_WIDTH * -2;
            }
        }if(centerNow==WEEKDAY.wk7){
            /*星期1在中间，依次为5、6、7、1、2*/
            if(centerWitch==WEEKDAY.wk5){
                offset = ITEM_WIDTH * 2;
            }else if(centerWitch==WEEKDAY.wk6){
                offset = ITEM_WIDTH * 1;
            }else if(centerWitch==WEEKDAY.wk1){
                offset = ITEM_WIDTH * -1;
            }else if(centerWitch==WEEKDAY.wk2){
                offset = ITEM_WIDTH * -2;
            }
        }

        return offset;

    }


}