package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.Reference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private EditText proteinEditText;
    private EditText caloriesEditText;
    private EditText carbohydratesEditText;
    private EditText fatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicjalizacja widoków
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        proteinEditText = findViewById(R.id.training_text);
        caloriesEditText = findViewById(R.id.training_text4);
        carbohydratesEditText = findViewById(R.id.training_text2);
        fatEditText = findViewById(R.id.training_text3);

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("userId", "0");
        int userId = Integer.parseInt(userIdString);
        String username = sharedPreferences.getString("username", "DefaultUser");

        getUserGoal(userId);

        // Ustawienie tekstu dla TextView
        TextView textViewUsername = findViewById(R.id.textView26);
        textViewUsername.setText(username);

        imageViewJedzenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });

        imageViewAnaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        imageViewStopa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        // Ustawienie OnClickListener dla przycisku "Zaktualizuj"
        Button updateButton = findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobierz dane wprowadzone przez użytkownika
                String calories = caloriesEditText.getText().toString().trim();
                String protein = proteinEditText.getText().toString().trim();
                String carbohydrates = carbohydratesEditText.getText().toString().trim();
                String fat = fatEditText.getText().toString().trim();

                // Sprawdź, czy wszystkie pola zostały wypełnione
                if (!calories.isEmpty() && !protein.isEmpty() && !carbohydrates.isEmpty() && !fat.isEmpty()) {
                    // Pobierz ID użytkownika z SharedPreferences


                    // Wywołaj metodę do dodawania lub aktualizowania celów kalorycznych
                    addOrUpdateUserGoal(userId, Integer.parseInt(calories), Integer.parseInt(protein), Integer.parseInt(carbohydrates), Integer.parseInt(fat));
                } else {
                    Toast.makeText(UserActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addOrUpdateUserGoal(int userId, int calories, int protein, int carbohydrates, int fat) {
        UserGoal usergoal = new UserGoal(userId, calories, protein, carbohydrates, fat);

        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<Void> call = apiService.addOrUpdateUserGoal(usergoal);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Cele kaloryczne zostały zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Błąd podczas aktualizacji celów kalorycznych", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void getUserGoal(int userId) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<UserGoal> call = apiService.getUserGoal(userId);

        call.enqueue(new Callback<UserGoal>() {
            @Override
            public void onResponse(Call<UserGoal> call, Response<UserGoal> response) {
                if (response.isSuccessful()) {
                    UserGoal userGoal = response.body();
                    if (userGoal != null) {
                        // Ustaw pola EditText z danymi z userGoal
                        caloriesEditText.setText(String.valueOf(userGoal.getCalories()));
                        proteinEditText.setText(String.valueOf(userGoal.getProtein()));
                        carbohydratesEditText.setText(String.valueOf(userGoal.getCarbohydrates()));
                        fatEditText.setText(String.valueOf(userGoal.getFat()));
                    } else {
                        // Jeśli userGoal jest null, ustaw pola EditText na wartości domyślne (0)
                        caloriesEditText.setText("0");
                        proteinEditText.setText("0");
                        carbohydratesEditText.setText("0");
                        fatEditText.setText("0");
                    }
                } else {
                    Toast.makeText(UserActivity.this, "Błąd pobierania celu użytkownika", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserGoal> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
