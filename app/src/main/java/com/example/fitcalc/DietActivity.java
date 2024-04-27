package com.example.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class DietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Inicjalizacja widoków
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        Button ButtonAddSniadanie = findViewById(R.id.textview_sniadanie_button);


        // Ustawienie OnClickListener dla obrazka Analiza
        imageViewAnaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        imageViewStopa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        imageViewLudzik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        ButtonAddSniadanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ups", "jesteśmy w bb");
                Intent intent = new Intent(DietActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
