package com.example.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TrainingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Inicjalizacja widoków
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);


        imageViewJedzenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });

        // Ustawienie OnClickListener dla obrazka Analiza
        imageViewAnaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        imageViewJedzenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });

        imageViewLudzik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}