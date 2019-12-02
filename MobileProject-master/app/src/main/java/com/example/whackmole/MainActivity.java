package com.example.whackmole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Handler mHandler;

    ImageView[] moles = new ImageView[9];

    public boolean run = true;
    int[] moleID = {R.id.mole_0, R.id.mole_1, R.id.mole_2, R.id.mole_3, R.id.mole_4, R.id.mole_5, R.id.mole_6, R.id.mole_7, R.id.mole_8};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences("ScoreList", MODE_PRIVATE);

        Button start = (Button)findViewById(R.id.start);
        Button quit = (Button) findViewById(R.id.quit);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < moles.length; j++) {
                    if (j != 4) {
                        moles[j].setImageResource(R.drawable.molegothit);
                        AnimationDrawable molepop = (AnimationDrawable) moles[j].getDrawable();
                        molepop.start();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), GameScene.class);
                startActivity(intent);
            }
        });


        for(int i = 0; i < moles.length; i++) {
            //시작알림
            moles[i] = (ImageView) findViewById(moleID[i]);

        }
        for (int j = 0; j < moles.length; j++) {
            if(j != 4) {
                moles[j].setImageResource(R.drawable.molepop);
                AnimationDrawable molepop = (AnimationDrawable) moles[j].getDrawable();
                molepop.start();
            }
            else if(j == 4)
            {
                moles[j].setImageResource(R.drawable.goldmolepop);
                AnimationDrawable molepop = (AnimationDrawable) moles[j].getDrawable();
                molepop.start();
            }

        }
    }

    public void Exit(View view)
    {
        ActivityCompat.finishAffinity(this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

}
