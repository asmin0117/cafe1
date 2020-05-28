package com.example.cafe1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class DayActivity extends AppCompatActivity {

    RelativeLayout rel;
    TextView textView3;
    ImageView setting;
    int cuscount;
    Boolean onoff;
    ArrayList<String> cus1;

    SharedPreferenceUtill sharedPreferenceUtill = new SharedPreferenceUtill();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        //소프트키(네비게이션바) 없애기 시작
        View decorView = getWindow().getDecorView();

        int uiOption = getWindow().getDecorView().getSystemUiVisibility();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOption);
        //소프트키(네비게이션바) 없애기 끝

        rel = (RelativeLayout) findViewById(R.id.rel);
        textView3 =(TextView)findViewById(R.id.textView3);
        setting =(ImageView) findViewById(R.id.setting);
        stopService(new Intent(getApplicationContext(),MusicService.class));

        SharedPreferenceUtill sharedPreference = new SharedPreferenceUtill();
        cuscount = sharedPreference.getInt(this,"count");
        onoff = sharedPreference.getBoolean(this,"sound");


        if(onoff == true){
            startService(new Intent(getApplicationContext(),GameMusicService.class));
        }
        if(onoff == false){
            stopService(new Intent(getApplicationContext(),GameMusicService.class));
        }
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivity.this, SettingActivity2.class);
                startActivity(intent);
            }
        });

        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DayActivity.this, NewMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


        switch (cuscount){
            case 1:
                textView3.setText("1일차");
                cus1 = new ArrayList<>();
                cus1.add("저기요. 아이스 아메리카노 1잔 주세요.");
                cus1.add("뜨거운 카페라떼 1잔 주실래요?");
                cus1.add("아이스 아메리카노 한잔, 뜨거운 카페라떼 한잔이요.");
                cus1.add("음....초코스무디 한잔이요.");
                cus1.add("바닐라라떼, 아메리카노 둘다 따뜻하게요.");
                cus1.add("뭐 먹지.....아이스카페라떼 주세요.");
                cus1.add("한정 초콜릿라떼 핫이요!");
                cus1.add("안녕하세요! 핫 아메리카노, 초코 스무디로 할게요.");
                cus1.add("초콜릿라떼 뜨거운거, 아메리카노 아이스로 주세요.");
                sharedPreferenceUtill.setStringArrayPref(DayActivity.this,"list",cus1);

                break;

            case 2:
                textView3.setText("2일차");
                break;

            case 4:
                textView3.setText("3일차");
                if(onoff == true){
                    stopService(new Intent(getApplicationContext(),GameMusicService.class));
                    startService(new Intent(getApplicationContext(),ChristmasMusicService.class));
                }
                if(onoff == false){
                    stopService(new Intent(getApplicationContext(),GameMusicService.class));
                    stopService(new Intent(getApplicationContext(),ChristmasMusicService.class));
                }
                break;

            case 7:
                textView3.setText("4일차");
                if(onoff == true){
                    stopService(new Intent(getApplicationContext(),ChristmasMusicService.class));
                    startService(new Intent(getApplicationContext(),GameMusicService.class));
                }
                if(onoff == false){
                    stopService(new Intent(getApplicationContext(),GameMusicService.class));
                    stopService(new Intent(getApplicationContext(),ChristmasMusicService.class));
                }
                break;
        }

    }
}
