package com.example.whackmole;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameScene extends AppCompatActivity {
    int left_time = 30;
    TextView mText;
    TextView score;
    Handler mHandler;
    ImageView[] moles = new ImageView[9];

    final String UP = "up";
    final String DOWN = "down";

    int point = 0;
    int[] moleID = {R.id.mole_0, R.id.mole_1, R.id.mole_2, R.id.mole_3, R.id.mole_4, R.id.mole_5, R.id.mole_6, R.id.mole_7, R.id.mole_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        mText = (TextView)findViewById(R.id.timer);
        score = (TextView)findViewById(R.id.score);

        for(int i = 0; i < moles.length; i++)
        {
            moles[i] = (ImageView)findViewById(moleID[i]);
            moles[i].setTag(DOWN);

            moles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((ImageView)v).getTag().toString().equals(UP))
                    {
                        point++;
                        score.setText(""+ point);
                        ((ImageView) v).setImageResource(R.drawable.molegothit);
                        AnimationDrawable molegothit = (AnimationDrawable)((ImageView) v).getDrawable();
                        molegothit.start();
                        if(!molegothit.isRunning()) {
                            v.setTag(DOWN);
                        }
                    }else{
                        if(point <= 0)
                        {
                            point=0;
                            score.setText(""+ point);
                        }
                        else{
                            point--;
                            score.setText(""+ point);
                        }
                    }
                }
            });

            for(int j = 0; j<moles.length; j++){
                new Thread(new DThread(j)).start();
            }
        }

        mHandler = new Handler(){
            public  void handleMessage(Message msg)
            {
                left_time--;
                mText.setText(""+left_time);
                mHandler.sendEmptyMessageDelayed(0,1000);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    Handler onHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            moles[msg.arg1].setImageResource(R.drawable.molepop);
            AnimationDrawable molepop = (AnimationDrawable)moles[msg.arg1].getDrawable();
            molepop.start();
            moles[msg.arg1].setTag(UP);
        }
    };

    Handler offHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            moles[msg.arg1].setImageResource(R.drawable.mole1);
            moles[msg.arg1].setTag(DOWN);
        }
    };

    public class DThread implements Runnable{ //두더지를 올라갔다 내려갔다 해줌
        int index = 0; //두더지 번호

        DThread(int index){
            this.index=index;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Message msg1 = new Message();
                    int offtime = new Random().nextInt(5000) + 500 ;
                    Thread.sleep(offtime); //두더지가 내려가있는 시간

                    msg1.arg1 = index;
                    onHandler.sendMessage(msg1);

                    int ontime = 1700;
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

}
