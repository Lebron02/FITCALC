package com.example.fitcalc;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Inicjalizacja widoków
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);


        imageViewJedzenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });

        // Ustawienie OnClickListener dla obrazka Analiza

        imageViewStopa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        imageViewLudzik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}