package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingActivity extends AppCompatActivity {
    private EditText burnedCaloriesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Inicjalizacja widoków
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        TextView TextViewDate = findViewById(R.id.training_date);
        burnedCaloriesEditText = findViewById(R.id.kalorie); // Pobierz EditText dla spalonych kalorii

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        String meal_date = sharedPreferences.getString("selectedDate", null);
        int userId_int = Integer.parseInt(userId);

        TextViewDate.setText(meal_date);

        // Ustawienie OnClickListener dla obrazka Jedzenie
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

        // Ustawienie OnClickListener dla obrazka Ludzik
        imageViewLudzik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        // Ustawienie OnClickListener dla przycisku Dodaj Trening
        findViewById(R.id.button_przelicz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobierz wprowadzone dane
                String burnedCaloriesString = burnedCaloriesEditText.getText().toString().trim();

                if (!burnedCaloriesString.isEmpty()) {
                    int burnedCalories = Integer.parseInt(burnedCaloriesString);
                    addOrUpdateExercise(userId_int, meal_date, burnedCalories); // Wywołaj funkcję do dodawania lub aktualizacji treningu
                } else {
                    Toast.makeText(TrainingActivity.this, "Podaj spalone kalorie.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addOrUpdateExercise(int userId, String exerciseDate, int burnedCalories) {
        ExerciseData exerciseData = new ExerciseData(userId, exerciseDate, burnedCalories);

        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<Void> call = apiService.addOrUpdateExercise(exerciseData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TrainingActivity.this, "Trening dodany lub zaktualizowany pomyślnie", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TrainingActivity.this, "Błąd podczas dodawania lub aktualizacji treningu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TrainingActivity", "Error adding or updating exercise: " + t.getMessage());
                Toast.makeText(TrainingActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
