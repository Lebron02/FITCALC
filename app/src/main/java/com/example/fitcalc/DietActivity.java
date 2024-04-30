package com.example.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DietActivity extends AppCompatActivity {

    private String formattedDate;  // Variable to store the formatted date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");

        if (userId != null) {
            // Use the userId for whatever you need here
            Log.d("DietActivity", "Logged in User ID: " + userId);
        }

        // Inicjalizacja widoków
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        Button ButtonAddSniadanie = findViewById(R.id.textview_sniadanie_button);
        TextView dateTextView = findViewById(R.id.textView_today);


        // Setup the date in the required format
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fdate = new SimpleDateFormat("dd-MM", Locale.getDefault());
        String datef = fdate.format(calendar.getTime());
        dateTextView.setText(datef);  // Display today's date in TextView

        // OnClickListener for the date TextView to update the formatted date
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the date format if needed or perform other actions
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar newCalendar = Calendar.getInstance();
                formattedDate = dateFormat.format(newCalendar.getTime());  // Update the formattedDate
            }
        });

        // Setting OnClickListener for image views and buttons
        imageViewAnaliza.setOnClickListener(v -> startActivity(new Intent(DietActivity.this, SummaryActivity.class)));
        imageViewStopa.setOnClickListener(v -> startActivity(new Intent(DietActivity.this, TrainingActivity.class)));
        imageViewLudzik.setOnClickListener(v -> {
            Intent intent = new Intent(DietActivity.this, UserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Pass user ID, date, and meal type when clicking the button
        ButtonAddSniadanie.setOnClickListener(v -> {
            Log.d("DietActivity", "User ID: " + userId);
            Log.d("DietActivity", "Date: " + formattedDate);
            Log.d("DietActivity", "Meal Type: śniadanie");


            Intent intent = new Intent(DietActivity.this, SearchActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("date", formattedDate);
            intent.putExtra("mealType", "śniadanie");
            startActivity(intent);
        });
    }

}
