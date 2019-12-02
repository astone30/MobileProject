package com.example.whackmole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView showscore = (TextView) findViewById(R.id.score);
        TextView highscore = (TextView) findViewById(R.id.highScore);

        Button restart = (Button) findViewById(R.id.restart);

        int score = getIntent().getIntExtra("SCORE", 0);
        showscore.setText(score +"");

        SharedPreferences settings = getSharedPreferences("SCORE_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if(score > highScore)
        {
            highscore.setText("High Score : " + score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else{
            highscore.setText("High Score : " + highScore);
        }
    }
    public void TryAgain(View view)
    {
        startActivity(new Intent(getApplicationContext(), GameScene.class));
    }

    public void BackToMenu(View view)
    {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
