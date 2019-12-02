package com.example.whackmole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = (Button)findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView mole = (ImageView)findViewById(R.id.imageView);
                //mole.setImageResource(R.drawable.molepop);
                //AnimationDrawable molepop = (AnimationDrawable) mole.getDrawable();
                //molepop.start();
                Intent intent = new Intent(getApplicationContext(), GameScene.class);
                startActivity(intent);

            }
        });

    }
}
