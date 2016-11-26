package com.openxu.anima;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {


    private CustomWeekView weekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weekView = (CustomWeekView) findViewById(R.id.weekView);

        weekView.setOnItemClickListener(new CustomWeekView.OnItemClickListener() {
            @Override
            public void onItemClick(View v, CustomWeekView.WEEKDAY day, int dayNum, String date) {
                Toast.makeText(MainActivity.this,"点击了："+dayNum, Toast.LENGTH_SHORT).show();
                Log.v("oepnxu","点击"+day);
            }
        });


    }
}
