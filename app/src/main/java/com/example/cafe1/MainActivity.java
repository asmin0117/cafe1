package com.example.cafe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import static com.example.cafe1.UserInputContract.UserTable.TABLE_USER;

public class MainActivity extends AppCompatActivity {
    //카운터 답변 제한 시간 15초
    private CountDownTimer timer;
    String strColor = "#FF0000";

    MenuDbhelper userdbhelper = new MenuDbhelper(this);
    SQLiteDatabase db;
    private List<User_Input> userList;


    ImageView setting, guest, imageView16;
    Button btnSelect1, btnSelect2, btnSelect3, goKitchen;
    LinearLayout linBtn, lin;
    TextView txtTalk, txtTalk2, guestname, datetext;
    FrameLayout frame;

    String customarray[], blackarray[], customname[], blackname[], name;

    Boolean touchonoff, savename;
    Random rand = new Random();
    int i, r, j, count;

    Drawable drawable;
    SharedPreferenceUtill sharedPreference;
    ArrayList<Drawable> drawables = new ArrayList<Drawable>();
    ArrayList<String> cuslist, cus1;
    Handler handler = new Handler();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    DatabaseReference databaseReference = firebaseDatabase.getReference();

    private static final String SETTINGS_PLAYER_JSON ="settings_item_json";

    ArrayList<String > list = new ArrayList<String>();
    SoundPool pool;
    int bip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService(new Intent(getApplicationContext(), MusicService.class));

        db = userdbhelper.getWritableDatabase();

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


        setting = (ImageView) findViewById(R.id.setting);
        guest = (ImageView) findViewById(R.id.guest);
        imageView16 = (ImageView) findViewById(R.id.imageView16);


        lin = (LinearLayout) findViewById(R.id.lin);
        linBtn = (LinearLayout) findViewById(R.id.linBtn);
        frame = (FrameLayout) findViewById(R.id.frame);

        txtTalk = (TextView) findViewById(R.id.txtTalk);
        txtTalk2 = (TextView) findViewById(R.id.txtTalk2);
        guestname = (TextView) findViewById(R.id.guestname);
        datetext = (TextView) findViewById(R.id.datetext);

        btnSelect1 = (Button) findViewById(R.id.btnSelect1);
        btnSelect2 = (Button) findViewById(R.id.btnSelect2);
        btnSelect3 = (Button) findViewById(R.id.btnSelect3);
        goKitchen = (Button) findViewById(R.id.goKitchen);
        guest = (ImageView) findViewById(R.id.guest);
        Resources res1 = getResources();

        drawable = res1.getDrawable(R.drawable.speech);
        sharedPreference = new SharedPreferenceUtill();
        count = sharedPreference.getInt(this, "count");

        touchonoff = sharedPreference.getBoolean(this, "sound");

        savename = sharedPreference.getBoolean(this, "savename");

//        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//        angry = pool.load(this, R.raw.angry, 1);
//        bip = pool.load(this, R.raw.wrong, 1);


        if (touchonoff == true) {
            pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

            bip = pool.load(this, R.raw.wrong, 1);
        }
        if (touchonoff == false) {

            pool = new SoundPool(0, AudioManager.STREAM_MUSIC, 0);
            bip = pool.load(this, R.id.wrong, 1);
        }

        SharedPreferenceUtill.setInt(MainActivity.this, "count", count + 1);


        timer = new CountDownTimer(15 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //엔딩 화면 바꿔야함
                pool.play(bip, 1,1,0,0,1);
                Intent intent = new Intent(MainActivity.this, TimeOverEndingActivity.class);
                startActivity(intent);
                finish();

            }
        };
        Resources resources = getResources();

        drawables.add(resources.getDrawable(R.drawable.selec_bar5));
        drawables.add(resources.getDrawable(R.drawable.selec_bar4));
        drawables.add(resources.getDrawable(R.drawable.selec_bar3));
        drawables.add(resources.getDrawable(R.drawable.selec_bar2));
        drawables.add(resources.getDrawable(R.drawable.selec_bar1));
        drawables.add(resources.getDrawable(R.drawable.selec_bar0));
        cuslist = sharedPreference.getStringArrayPref(this, "list");

        switch (count) {
            case 1:
                datetext.setText("23");
                imageView16.setVisibility(View.INVISIBLE);
                i = rand.nextInt(2);

                switch (i) {
                    case 0: {
                        txtTalk.setText(cuslist.get(0));
                        gcus0();
                        cuslist.set(0, "빈칸");
                        break;
                    }

                    case 1: {
                        txtTalk.setText(cuslist.get(1));
                        gcus1();
                        cuslist.set(1, "빈칸");
                        break;
                    }

                    case 2: {
                        txtTalk.setText(cuslist.get(2));
                        gcus2();
                        cuslist.set(2, "빈칸");
                        break;
                    }

                }
                break;
            case 2:
                datetext.setText("24");
                imageView16.setVisibility(View.INVISIBLE);
                txtTalk.setText("시원한 카페라떼 한 잔, 통신사랑 쿠폰, 포인트 할인 해줘.");
                frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        txtTalk.setVisibility(View.GONE);
                        linBtn.setVisibility(View.VISIBLE);
                        guestname.setVisibility(View.INVISIBLE);
                        imageView16.setVisibility(View.VISIBLE);
                        setting.setVisibility(View.INVISIBLE);
                        frame.setClickable(false);
                        frame.setBackground(null);

                        timer.start();
                        MAnimThread thread = new MAnimThread();
                        thread.start();
                    }
                });
                bcus2();
                break;
            case 3:
                datetext.setText("24");
                imageView16.setVisibility(View.INVISIBLE);
                i = rand.nextInt(5);

                while ((cuslist.get(i)).equals("빈칸")) {
                    i--;
                    if (i == -1) {
                        i++;
                        while ((cuslist.get(i)).equals("빈칸")) {
                            i++;
                        }
                    }
                }
                switch (i) {

                    case 0: {
                        txtTalk.setText(cuslist.get(0));
                        gcus0();
                        cuslist.set(0, "빈칸");
                        break;
                    }

                    case 1: {
                        txtTalk.setText(cuslist.get(1));
                        gcus1();
                        cuslist.set(1, "빈칸");
                        break;
                    }

                    case 2: {
                        txtTalk.setText(cuslist.get(2));
                        gcus2();
                        cuslist.set(2, "빈칸");
                        break;
                    }
                    case 3: {
                        txtTalk.setText(cuslist.get(3));
                        gcus3();
                        cuslist.set(3, "빈칸");
                        break;
                    }
                    case 4: {
                        txtTalk.setText(cuslist.get(4));
                        gcus4();
                        cuslist.set(4, "빈칸");
                        break;
                    }
                    case 5: {
                        txtTalk.setText(cuslist.get(5));
                        gcus5();
                        cuslist.set(5, "빈칸");
                        break;
                    }
                }
                break;
            case 4:
                datetext.setText("25");
                datetext.setTextColor(Color.parseColor(strColor));
                txtTalk.setText("어제 제가 시킨 메뉴가 뭐게~요?");
                imageView16.setVisibility(View.INVISIBLE);
                frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtTalk.setVisibility(View.GONE);
                        linBtn.setVisibility(View.VISIBLE);
                        guestname.setVisibility(View.INVISIBLE);
                        imageView16.setVisibility(View.VISIBLE);
                        setting.setVisibility(View.INVISIBLE);
                        frame.setClickable(false);
                        frame.setBackground(null);

                        timer.start();
                        MAnimThread thread = new MAnimThread();
                        thread.start();
                    }
                });
                bcus1();
                break;
            case 5:
            case 6:
                Resources gu1 = getResources();
                Drawable dr2 = gu1.getDrawable(R.drawable.countback_cm);
                lin.setBackground(dr2);
                datetext.setText("25");
                datetext.setTextColor(Color.parseColor(strColor));
                imageView16.setVisibility(View.INVISIBLE);
                i = rand.nextInt(8);

                while ((cuslist.get(i)).equals("빈칸")) {
                    i--;
                    if (i == -1) {
                        i++;
                        while ((cuslist.get(i)).equals("빈칸")) {
                            i++;
                        }
                    }
                }
                switch (i) {
                    case 0: {
                        txtTalk.setText(cuslist.get(0));
                        gcus0();
                        cuslist.set(0, "빈칸");
                        break;
                    }

                    case 1: {
                        txtTalk.setText(cuslist.get(1));
                        gcus1();
                        cuslist.set(1, "빈칸");
                        break;
                    }

                    case 2: {
                        txtTalk.setText(cuslist.get(2));
                        gcus2();
                        cuslist.set(2, "빈칸");
                        break;
                    }
                    case 3: {
                        txtTalk.setText(cuslist.get(3));
                        gcus3();
                        cuslist.set(3, "빈칸");
                        break;
                    }
                    case 4: {
                        txtTalk.setText(cuslist.get(4));
                        gcus4();
                        cuslist.set(4, "빈칸");
                        break;
                    }
                    case 5: {
                        txtTalk.setText(cuslist.get(5));
                        gcus5();
                        cuslist.set(5, "빈칸");
                        break;
                    }
                    case 6: {
                        txtTalk.setText(cuslist.get(6));
                        gcus6();
                        cuslist.set(6, "빈칸");
                        break;
                    }
                    case 7: {
                        txtTalk.setText(cuslist.get(7));
                        gcus7();
                        cuslist.set(7, "빈칸");
                        break;
                    }
                    case 8: {
                        txtTalk.setText(cuslist.get(8));
                        gcus8();
                        cuslist.set(8, "빈칸");
                        break;
                    }

                }

                break;

//                    case 7:
//                    case 8:
//                    case 9:
//                    case 10:
//                    case 11:
//                        datetext.setText("26");
//                        j = rand.nextInt(2);
//                        break;
                }


                setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, SettingActivity2.class);
                        startActivity(intent);

                    }
                });



                customarray = getResources().getStringArray(R.array.CUSTOM);
                blackarray = getResources().getStringArray(R.array.BLACK);
                customname = getResources().getStringArray(R.array.NAME);
                blackname = getResources().getStringArray(R.array.BLACKNAME);


//        i = rand.nextInt(customarray.length);
//        r = rand.nextInt(blackarray.length);
//
//
//
//
//        if (savename == true) {
//            switch (j) {
//                case 0:
//                    imageView16.setVisibility(View.INVISIBLE);
//                        txtTalk.setText(customarray[i]);
//                        switch (i) {
//                            case 0: {
//                                gcus0();
//                                break;
//                            }
//
//                            case 1: {
//                                gcus1();
//                                break;
//                            }
//
//                            case 2: {
//                                gcus2();
//                                break;
//                            }
//                            case 3: {
//                                gcus3();
//                                break;
//                            }
//                            case 4: {
//                                gcus4();
//                                break;
//                            }
//                            case 5: {
//                                gcus5();
//                                break;
//                            }
//                            case 6: {
//                                gcus6();
//                                break;
//                            }
//
//                        }
//
//                    break;
//
//                case 1: {
//                    frame.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            txtTalk.setVisibility(View.GONE);
//                            linBtn.setVisibility(View.VISIBLE);
//                            guestname.setVisibility(View.INVISIBLE);
//                            frame.setClickable(false);
//                            frame.setBackground(null);
//
//                            timer.start();
//
//
//                        }
//                    });
//
//                    txtTalk.setText((blackarray[r]));
//                    switch (r) {
//                        case 0:
//                            bcus0();
//                            break;
//                        case 1:
//                            bcus1();
//                            break;
//                        case 2:
//                            bcus2();
//                            break;
//                    }
//                }
//            }
//        }
//        if (savename == false) {
//            name = sharedPreference.getString(MainActivity.this, "name");
//            switch (name) {
//                case "0":
//                    txtTalk.setText(cuslist.get(0));
//                    gcus0();
//                    break;
//                case "1":
//                    txtTalk.setText(cuslist.get(1));
//                    gcus1();
//                    break;
//                case "2":
//                    txtTalk.setText(cuslist.get(2));
//                    gcus2();
//                    break;
//                case "3":
//                    txtTalk.setText(cuslist.get(3));
//                    gcus3();
//                    break;
//                case "4":
//                    txtTalk.setText(cuslist.get(4));
//                    gcus4();
//                    break;
//                case "5":
//                    txtTalk.setText(cuslist.get(5));
//                    gcus5();
//                    break;
//                case "6":
//                    txtTalk.setText(cuslist.get(6));
//                    gcus6();
//                    break;
//                case "b0":
//                    frame.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            txtTalk.setVisibility(View.GONE);
//                            linBtn.setVisibility(View.VISIBLE);
//                            guestname.setVisibility(View.INVISIBLE);
//                            frame.setClickable(false);
//                            frame.setBackground(null);
//
//                            timer.start();
//
//
//                        }
//                    });
//                    txtTalk.setText(blackarray[0]);
//                    bcus0();
//                    break;
//                case "b1":
//                    frame.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            txtTalk.setVisibility(View.GONE);
//                            linBtn.setVisibility(View.VISIBLE);
//                            guestname.setVisibility(View.INVISIBLE);
//                            frame.setClickable(false);
//                            frame.setBackground(null);
//
//                            timer.start();
//
//
//                        }
//                    });
//                    datetext.setText("25");
//                    txtTalk.setText("어제 제가 시킨 메뉴가 뭐게~요?");
//                    bcus1();
//                    break;
//                case "b2":
//                    frame.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            txtTalk.setVisibility(View.GONE);
//                            linBtn.setVisibility(View.VISIBLE);
//                            guestname.setVisibility(View.INVISIBLE);
//                            frame.setClickable(false);
//                            frame.setBackground(null);
//
//                            timer.start();
//
//
//                        }
//                    });
//                    datetext.setText("24");
//                    txtTalk.setText("시원한 카페라떼 한 잔, 통신사랑 쿠폰, 포인트 할인 해줘.");
//                    bcus2();
//                    break;
//            }



//    }

    }

    public void gcus0() {
        guestname.setText("23살 여성");
        Glide.with(MainActivity.this).load(R.raw.g1).into(guest);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "0");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "아이스\n아메리카노");
                insertExtra(1);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gcus1() {
       guestname.setText("45세 남자");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.m60);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "1");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "핫\n 카페라떼");
                insertExtra(4);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gcus2() {
        guestname.setText("32살 여성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f30);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "2");
        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);

                intent.putExtra("bil1", "아이스\n아메리카노");
                insertExtra(1);
                intent.putExtra("bil2", "핫\n 카페라떼");
                insertExtra(4);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gcus3() {
        guestname.setText("16살 여성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f10);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "3");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "초코스무디");
                insertExtra(7);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gcus4() {
        guestname.setText("46세 여성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f60);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "4");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "핫\n바닐라라떼");
                insertExtra(6);
                intent.putExtra("bil2", "핫\n아메리카노");
                insertExtra(2);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }
    public void gcus5() {
        guestname.setText("26세 남자");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.m20);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "5");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "아이스\n 카페라떼");
                insertExtra(3);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }
    public void gcus6() {
        guestname.setText("31세 남성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.m30);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "6");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "한정 핫\n초콜릿라떼");
                insertExtra(10);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }
    public void gcus7() {
        guestname.setText("18세 여성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f10_2);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "6");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "핫\n아메리카노");
                insertExtra(2);
                intent.putExtra("bil2", "\n초코스무디");
                insertExtra(7);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }
    public void gcus8() {
        guestname.setText("56세 여성");
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f60_2);
        guest.setImageDrawable(dr);
        goKitchen.setVisibility(View.VISIBLE);
        goKitchen.setClickable(true);
        sharedPreference.setString(MainActivity.this, "name", "6");

        goKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                intent.putExtra("bil1", "크리스마스\n핫\n초콜릿라떼");
                insertExtra(10);
                intent.putExtra("bil1", "아이스\n아메리카노");
                insertExtra(1);
                sharedPreference.setStringArrayPref(MainActivity.this,"list",cuslist);
                startActivity(intent);
                finish();
            }
        });
    }


    public void bcus0() {
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f60_3);
        guest.setImageDrawable(dr);
        sharedPreference.setString(MainActivity.this, "name", "b0");

        guestname.setText("가족 손님");
        btnSelect1.setText("손님, 아이가 손으로 케잌을 만져서 판매가 어려워서\n이 케익까지 구매 해 주실 수 있을까요?");
        btnSelect2.setText("(못 본 척 한다.)");
        btnSelect3.setText("얘! 그걸 만지면 어떡하니!");

//        final MAnimThread thread = new MAnimThread();
//        thread.start();

        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView16.setVisibility(View.INVISIBLE);
                linBtn.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("아, 죄송해요. 애기가 만진 것까지 합쳐서 계산해주세요.");
                goKitchen.setVisibility(View.VISIBLE);
                guestname.setVisibility(View.VISIBLE);
                frame.setClickable(true);
                frame.setBackground(drawable);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case4").setValue(0);

                timer.cancel();

                goKitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        timer.cancel();
                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                        intent.putExtra("bil1", "핫\n바닐라라떼");
                        insertExtra(6);
                        intent.putExtra("bil2", "핫\n아메리카노");
                        insertExtra(2);
                        intent.putExtra("bil3", "핫\n 카페라떼");
                        insertExtra(4);
                        intent.putExtra("bil4", "핫\n  초코");
                        insertExtra(12);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

        btnSelect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                imageView16.setVisibility(View.INVISIBLE);
                linBtn.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("(양심에 찔리지만 넘어갔다.)");
                goKitchen.setVisibility(View.VISIBLE);
                guestname.setVisibility(View.VISIBLE);
                frame.setClickable(true);
                frame.setBackground(drawable);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case4").setValue(1);
                goKitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                        intent.putExtra("bil1", "핫\n바닐라라떼");
                        insertExtra(6);
                        intent.putExtra("bil2", "핫\n아메리카노");
                        insertExtra(2);
                        intent.putExtra("bil3", "핫\n 카페라떼");
                        insertExtra(4);
                        intent.putExtra("bil4", "핫\n  초코");
                        insertExtra(12);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        btnSelect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case4").setValue(0);
                timer.cancel();
                pool.play(bip, 1,1,0,0,1);
                Intent intent = new Intent(MainActivity.this, WrongEndingActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void bcus1() {
        guestname.setText("25세 남자");
        Resources gu1 = getResources();
        Drawable dr1 = gu1.getDrawable(R.drawable.m20_1);
        guest.setImageDrawable(dr1);
        sharedPreference.setString(MainActivity.this, "name", "b1");

        btnSelect1.setText("제가 궁예인 줄 아십니까?");
        btnSelect2.setText("죄송하지만 제가 기억이 안 나는데, 메뉴를 정확히 골라 주시겠어요?");
        btnSelect3.setText("네? 아메리카노?");

//        final MAnimThread thread = new MAnimThread();
//        thread.start();

        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case6").setValue(0);
                timer.cancel();
                pool.play(bip, 1,1,0,0,1);
                Intent intent = new Intent(MainActivity.this, WrongEndingActivity.class);
                startActivity(intent);
                finish();

            }
        });


        btnSelect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case6").setValue(0);
                timer.cancel();
                imageView16.setVisibility(View.INVISIBLE);
                linBtn.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("따뜻한 바닐라라떼 1잔 주세요...");
                goKitchen.setVisibility(View.VISIBLE);
                guestname.setVisibility(View.VISIBLE);
                frame.setBackground(drawable);

                goKitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                        intent.putExtra("bil1", "핫\n바닐라라떼");
                        insertExtra(6);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        btnSelect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case4").setValue(1);
                linBtn.setVisibility(View.INVISIBLE);
                imageView16.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("아니 아니, 그거 말고 다른거 시켰잖아요");
                frame.setClickable(true);
                guestname.setVisibility(View.VISIBLE);
                frame.setBackground(drawable);
                frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guestname.setVisibility(View.VISIBLE);
                        guestname.setText("나");
                        txtTalk2.setText("네? 혹시 카페라떼..?");
                        frame.setClickable(true);
                        frame.setBackground(drawable);
                        frame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                guestname.setVisibility(View.VISIBLE);
                                guestname.setText("25세 남자");
                                txtTalk2.setText("아니이~ 그거 말고 달짝지근한거!");
                                frame.setClickable(true);
                                frame.setBackground(drawable);
                                frame.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        guestname.setVisibility(View.VISIBLE);
                                        guestname.setText("나");
                                        txtTalk2.setText("잘 모르겠는데,,,");
                                        frame.setClickable(true);
                                        frame.setBackground(drawable);
                                        frame.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                guestname.setVisibility(View.VISIBLE);
                                                guestname.setText("25세 남자");
                                                txtTalk2.setText("에이, 내가 힌트 줄게 ‘바’로 시작하는거!");
                                                frame.setClickable(true);
                                                frame.setBackground(drawable);
                                                frame.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        guestname.setVisibility(View.VISIBLE);
                                                        guestname.setText("뒷 손님");
                                                        txtTalk2.setText("(짜증을 내며) 저기요, 저 빨리 주문하고 나가봐야 하거든요? \n\n언제까지 스무고개 하고 있을거에요?");
                                                        frame.setClickable(true);
                                                        frame.setBackground(drawable);
                                                        frame.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                guestname.setVisibility(View.VISIBLE);
                                                                guestname.setText("나");
                                                                txtTalk2.setText("아, 따뜻한 바닐라라떼요! \n\n(뒤의 손님에게) 죄송합니다, 조금만 더 기다려주세요!");
                                                                goKitchen.setVisibility(View.VISIBLE);


                                                                goKitchen.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        timer.cancel();
                                                                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                                                                        intent.putExtra("bil1", " 핫\n바닐라라떼");
                                                                        insertExtra(6);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                                guestname.setVisibility(View.VISIBLE);
                                                                frame.setClickable(true);
                                                                frame.setBackground(drawable);

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }
    public void bcus2 () {
        Resources gu = getResources();
        Drawable dr = gu.getDrawable(R.drawable.f60_3);
        guest.setImageDrawable(dr);
        sharedPreference.setString(MainActivity.this, "name", "b2");

        guestname.setText("63세 여성");
        btnSelect1.setText("할인이나 적립 둘 중 하나만 선택하세요.");
        btnSelect2.setText("저희 매장은 할인과 적립 동시 적용 가능 매장이 아니어서요. \n둘 중 하나만 이용 가능하신데, 어떤 걸로 하시겠어요?");
        btnSelect3.setText("(사장님께 전화해본다.)");

//        final MAnimThread thread = new MAnimThread();
//        thread.start();

        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case3").setValue(1);
                imageView16.setVisibility(View.INVISIBLE);
                linBtn.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("다른 곳은 다 되던데 왜 여기는 안 되는 거야! \n\n그리고 뭐 이리 불친절해?");
                goKitchen.setVisibility(View.VISIBLE);
                guestname.setVisibility(View.VISIBLE);
                frame.setClickable(true);
                frame.setBackground(drawable);
                goKitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                        intent.putExtra("bil1", "아이스\n 카페라떼");
                        insertExtra(3);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });


        btnSelect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                linBtn.setVisibility(View.INVISIBLE);
                imageView16.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                txtTalk2.setText("그럼 그렇게 해줘");
                goKitchen.setVisibility(View.VISIBLE);
                guestname.setVisibility(View.VISIBLE);
                frame.setClickable(true);
                frame.setBackground(drawable);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case3").setValue(0);

                goKitchen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, KitchenActivity.class);
                        intent.putExtra("bil1", "아이스\n 카페라떼");
                        insertExtra(3);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        btnSelect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();
                linBtn.setVisibility(View.INVISIBLE);
                imageView16.setVisibility(View.INVISIBLE);
                txtTalk2.setVisibility(View.VISIBLE);
                guestname.setText("사장님");
                txtTalk2.setText("아직도 우리 매장 방침을 다 못 외우신 건가요? \n이런 걸로 나를 부르다니....");
                Glide.with(MainActivity.this).load(R.raw.boss_angry).into(guest);
                guestname.setVisibility(View.VISIBLE);
                frame.setClickable(true);
                frame.setBackground(drawable);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference.child(uid).child("case3").setValue(0);
                frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pool.play(bip, 1,1,0,0,1);
                        Intent intent = new Intent(MainActivity.this, WrongEndingActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });
            }
        });
        }

    public void insertExtra(int name) {
        ContentValues values = new ContentValues();

        int cup = 0;
        int base = 0;
        int ice = 0;
        int coffee = 0;
        int ingredient1 = 0;
        int ingredient2 = 0;
        int ingredient3 = 0;

        values.put("menu_name", name);

        values.put("cup", cup);
        values.put("base", base);
        values.put("ice", ice);
        values.put("coffee", coffee);
        values.put("ingredient1", ingredient1);
        values.put("ingredient2", ingredient2);
        values.put("ingredient3", ingredient3);

        db.insert(TABLE_USER, null, values);
    }

    public class MAnimThread extends Thread{
        public void run(){
            int index = 0 ;
            for(int i = 0; i < 6; i++){
                final Drawable drawable3 = drawables.get(index);
                index ++;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView16.setImageDrawable(drawable3);
                    }
                });
                try{
                    Thread.sleep(2900);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}





