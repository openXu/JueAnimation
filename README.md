##炫酷的星期控件

讲解博客：http://blog.csdn.net/xmxkf/article/details/53420889

##效果图
![](/WeekView.gif)

##使用方法：

**1、文件拷贝**

src\CustomWeekView.java
drawable\week_one_bg.xml        item颜色配置
layout\custom_week_layout.xml
values\dimens.xml
values\attrs.xml

**2、在layout中使用控件**
```xml
    <com.openxu.anima.CustomWeekView
        android:id="@+id/weekView"
        android:layout_width="match_parent"
        android:layout_height="100dip"
        android:textSize="13sp"
        openxu:dateTextSize="10"
        android:textColor="#ffffff"
        openxu:dateTextColor="#aaffffff"
        openxu:durationTime="500"
        openxu:scaleSize="1.3"/>
```

**属性说明：**
background：控件背景色 （item圆圈颜色请在drawable\week_one_bg.xml设置）
durationTime：动画持续时间
textSize：星期几字体大小
textColor：星期几字体颜色
dateTextSize：日期字体大小
dateTextColor：日期字体颜色
scaleSize：显示在中间的item的放大倍数，范围为1~2 （1：不放大  2:放大两倍）

**3、item点击事件回调**
```Java
    weekView.setOnItemClickListener(new CustomWeekView.OnItemClickListener() {
            /**
             * 条目点击事件
             * @param v 被点击的item控件
             * @param day 被点击的item是星期几，请参见枚举类型WEEKDAY
             * @param dayNum 被点击的item是一个星期的第几天（1-7）
             * @param date 被点击的item上的日期11/26
             * @see WEEKDAY
             */
            @Override
            public void onItemClick(View v, CustomWeekView.WEEKDAY day, int dayNum, String date) {
                Toast.makeText(MainActivity.this,"点击了："+dayNum, Toast.LENGTH_SHORT).show();
                Log.v("oepnxu","点击"+day);
            }
        });
```