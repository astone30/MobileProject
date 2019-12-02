package com.example.whackmole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

public class GameScene extends AppCompatActivity {

    public int left_time = 30; //남은 시간 시작시 30초로 설정
    int GoldenMolenum; //황금두더지가 나오는 위치설정
    int GoldenMolepoptime; //황금두더지가 나오는 위치설정
    public int fast = 0;

    Random random;

    public boolean run = true; //두더지 스레드의 동작 유무
    public boolean isGoldhere = false; //화면상 골드 두더지 있는지 없는지
    public boolean ready = false;

    Handler mHandler; //두더지 동작 스레드 헨들러

    ImageView[] moles = new ImageView[9]; //두더지 이미지 array

    ImageView timernumT; //타이머 십의자리
    ImageView timernumO; //타이머 일의자리
    ImageView scorenumT; //점수 십의자리
    ImageView scorenumO; //점수 일의자리

    TextView readyset;

    final String UP = "up"; //두저지가 올라와있는지
    final String DOWN = "down"; //내려가 있는지 판별하는 태그

    public int point = 0;

    int[] moleID = {R.id.mole_0, R.id.mole_1, R.id.mole_2, R.id.mole_3, R.id.mole_4, R.id.mole_5, R.id.mole_6, R.id.mole_7, R.id.mole_8}; //두저지 ID
    int[] numImg = {R.drawable.number_0, R.drawable.number_1, R.drawable.number_2, R.drawable.number_3, R.drawable.number_4, R.drawable.number_5, R.drawable.number_6, R.drawable.number_7, R.drawable.number_8,R.drawable.number_9};
    int[] molAnimaiotn = {R.drawable.molepop,R.drawable.molepop_1, R.drawable.molepop_2, R.drawable.molepop_3}; //일반 두더지 속도별 애니메이션
    int[] goldmolAnimaiotn = {R.drawable.goldmolepop,R.drawable.goldmolepop1, R.drawable.goldmolepop2, R.drawable.goldmolepop3}; //황금 두더지 속도별 애니메이션
    int[] howFast = {16000, 12800, 8000, 4800};//두더지 속도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fast = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);
        random = new Random();
        timernumT =findViewById(R.id.Tnumten); //십의자리 시간초 이미지
        timernumO =findViewById(R.id.Tnumone); //일의자리 시간초 이미지

        scorenumT = findViewById(R.id.ScoreTen);
        scorenumO = findViewById(R.id.ScoreOne);

        for(int i = 0; i < moles.length; i++) {
            //시작알림
            moles[i] = (ImageView) findViewById(moleID[i]);
            moles[i].setTag(DOWN); //두더지를 내려가있는 상태로 초기화

            if (moles[i].getTag().toString().equals(DOWN)) {
                moles[i].setImageResource(R.drawable.mole1); //두더지 이미지 세팅
            }
                moles[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // 두더지 이미지 클릭 리스너
                        if (!((ImageView) v).getTag().toString().equals(DOWN)) //두더지가 내려가있지 않을때 점수를 획득한다.
                        {
                            if(isGoldhere) { //황금두더지가 화면상에 있을 때
                                if (((ImageView) v) != moles[GoldenMolenum]) {
                                    point++;
                                    NumberToImage(point, scorenumT, scorenumO);
                                    ((ImageView) v).setImageResource(R.drawable.molegothit);
                                    AnimationDrawable molegothit = (AnimationDrawable) ((ImageView) v).getDrawable();
                                    molegothit.start();
                                    if (!molegothit.isRunning()) {
                                        ((ImageView) v).setImageResource(R.drawable.mole1);
                                        v.setTag(DOWN);
                                    }
                                } else if (((ImageView) v) == moles[GoldenMolenum]) { //
                                    point = point +2;
                                    if(fast < howFast.length-1) {
                                        fast++;
                                    }
                                    left_time = left_time + 3;
                                    NumberToImage(point, scorenumT, scorenumO);
                                    ((ImageView) v).setImageResource(R.drawable.molegothit);
                                    AnimationDrawable molegothit = (AnimationDrawable) ((ImageView) v).getDrawable();
                                    molegothit.start();
                                    if (!molegothit.isRunning()) {
                                        ((ImageView) v).setImageResource(R.drawable.mole1);
                                        v.setTag(DOWN);
                                        isGoldhere = false;
                                    }
                                }
                            }
                            else  if(!isGoldhere) //화면상에 황금 두더지가 없을 때
                            {
                                point++;
                                NumberToImage(point, scorenumT, scorenumO);
                                ((ImageView) v).setImageResource(R.drawable.molegothit);
                                AnimationDrawable molegothit = (AnimationDrawable) ((ImageView) v).getDrawable();
                                molegothit.start();
                                if (!molegothit.isRunning()) {
                                    ((ImageView) v).setImageResource(R.drawable.mole1);
                                    v.setTag(DOWN);
                                }
                            }
                        } else {
                            if (point <= 0) {
                                point = 0; //점수는 0이하로 내려가지 않는다.
                                NumberToImage(point, scorenumT, scorenumO);
                            } else {
                                point--; //점수 감소
                                NumberToImage(point, scorenumT, scorenumO);
                            }
                        }
                    }
                });
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                while (ready){
                    if (left_time != 0) {
                        if (!isGoldhere) {
                            GoldenMolepoptime = random.nextInt(left_time) + 1; //1초부터 29초중 선택
                            GoldenMolenum = random.nextInt(8); //0-8까지의 두더지 번호 중 랜덤 선택
                        }
                        if (left_time == GoldenMolepoptime) {
                            moles[GoldenMolenum].setImageResource(R.drawable.goldmole_0);//황금두더지 준비
                            isGoldhere = true;
                        }
                        left_time--;
                        NumberToImage(left_time, timernumT, timernumO);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    } else if (left_time == 0) {
                        for (int k = 0; k < moles.length; k++) {
                            moles[k].setImageResource(R.drawable.mole1);
                        }
                        run = false;
                        //SharedPreferences score = getSharedPreferences("ScoreList", 0);
                        Intent intent = new Intent(getApplicationContext(), result.class);
                        intent.putExtra("SCORE", point);
                        startActivity(intent);
                    }
                }
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    Handler onHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(!isGoldhere)
            {
                moles[msg.arg1].setImageResource(molAnimaiotn[fast]); //일반 두더지 애니메이션
                AnimationDrawable molepop = (AnimationDrawable) moles[msg.arg1].getDrawable();
                molepop.start();
                moles[msg.arg1].setTag(UP);
            }
            else if(isGoldhere)
            {
                if(msg.arg1 == GoldenMolenum)
                {
                    moles[msg.arg1].setImageResource(goldmolAnimaiotn[fast]); //황금두더지 애니메이션
                    AnimationDrawable molepop = (AnimationDrawable) moles[msg.arg1].getDrawable();
                    molepop.start();
                }
                else if(msg.arg1 != GoldenMolenum)
                {
                    moles[msg.arg1].setImageResource(molAnimaiotn[fast]); // 그외의 일반두더지
                    AnimationDrawable molepop = (AnimationDrawable) moles[msg.arg1].getDrawable();
                    molepop.start();
                }
                moles[msg.arg1].setTag(UP);
            }
        }
    };

    Handler offHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            moles[msg.arg1].setTag(DOWN);
            moles[msg.arg1].setImageResource(R.drawable.mole1);
        }
    };

    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌
        int index = 0; //두더지 번호

        DThread(int index){
            this.index=index;
        }

        @Override
        public void run() {
            while(run){
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(15000) + 500 ;
                    Thread.sleep(offtime); //두더지가 내려가있는 시간

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int ontime = howFast[fast]; //두더지의 속도 조정
                    Thread.sleep(ontime); //두더지가 올라가있는 시간
                    Message msg2 = new Message();
                    msg2.arg1= index;
                    offHandler.sendMessage(msg2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void NumberToImage(int num, ImageView Timg, ImageView Oimg) //점수, 또는 시간정보를 이미지로 바꾸어 보여준다.
    {
        if(num < 10)
        {
            Timg.setImageResource(R.drawable.number_0);
            Oimg.setImageResource(numImg[num]);
        }
        else if(num >= 10)
        {
            Timg.setImageResource(numImg[num/10]);
            Oimg.setImageResource(numImg[num%10]);
        }
    }

}
