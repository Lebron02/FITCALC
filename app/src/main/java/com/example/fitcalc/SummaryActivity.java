package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private TextView proteinEditText;
    private TextView caloriesEditText;
    private TextView carbohydratesEditText;
    private TextView fatEditText;

    private TextView proteinsum;
    private TextView caloriessum;
    private TextView carbohydratessum;
    private TextView fatsum;
    private TextView burnedCaloriesTextView;
    private TextView netCaloriesTextView;

    private volatile boolean summaryDataFetched = false;
    private volatile boolean burnedCaloriesFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        caloriessum = findViewById(R.id.textView_sum_kcal_all);
        burnedCaloriesTextView = findViewById(R.id.textView21);
        caloriessum.setText("0");
        burnedCaloriesTextView.setText("0");

        // Inicjalizacja widoków
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        proteinEditText = findViewById(R.id.textView5);
        caloriesEditText = findViewById(R.id.textView14);
        carbohydratesEditText = findViewById(R.id.textView18);
        fatEditText = findViewById(R.id.textView16);
        proteinsum = findViewById(R.id.textView_sum_b);
        carbohydratessum = findViewById(R.id.textView_sum_w);
        fatsum = findViewById(R.id.textView_sum_t);
        netCaloriesTextView = findViewById(R.id.textView_sum_kcal);


        TextView datetext = findViewById(R.id.training_text);
        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("userId", "0");
        String date = sharedPreferences.getString("selectedDate", "");
        int userId = Integer.parseInt(userIdString);

        fetchSummaryData(userId, date);
        fetchBurnedCalories(userId, date);
        datetext.setText(date);
        getUserGoal(userId);

        imageViewJedzenie.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, DietActivity.class);
            startActivity(intent);
        });

        imageViewStopa.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, TrainingActivity.class);
            startActivity(intent);
        });

        imageViewLudzik.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, UserActivity.class);
            startActivity(intent);
        });
    }

    private void getUserGoal(int userId) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<UserGoal> call = apiService.getUserGoal(userId);

        call.enqueue(new Callback<UserGoal>() {
            @Override
            public void onResponse(Call<UserGoal> call, Response<UserGoal> response) {
                if (response.isSuccessful()) {
                    UserGoal userGoal = response.body();
                    if (userGoal != null) {
                        caloriesEditText.setText(String.valueOf(userGoal.getCalories()));
                        proteinEditText.setText(String.valueOf(userGoal.getProtein()));
                        carbohydratesEditText.setText(String.valueOf(userGoal.getCarbohydrates()));
                        fatEditText.setText(String.valueOf(userGoal.getFat()));
                    } else {
                        caloriesEditText.setText("0");
                        proteinEditText.setText("0");
                        carbohydratesEditText.setText("0");
                        fatEditText.setText("0");
                    }
                } else {
                    Toast.makeText(SummaryActivity.this, "Błąd pobierania celu użytkownika", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserGoal> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchSummaryData(int userId, String date) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Summary>> call = apiService.getSummaryByUserAndDate(userId, date);

        call.enqueue(new Callback<List<Summary>>() {
            @Override
            public void onResponse(Call<List<Summary>> call, Response<List<Summary>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Summary summary = response.body().get(0);
                    caloriessum.setText(String.valueOf(summary.getConsumed_calories()));
                    proteinsum.setText(String.valueOf(summary.getConsumed_protein()));
                    carbohydratessum.setText(String.valueOf(summary.getConsumed_carbohydrates()));
                    fatsum.setText(String.valueOf(summary.getConsumed_fat()));
                    summaryDataFetched = true;
                    updateNetCaloriesIfNeeded();
                } else {
                    // Set all TextViews to "0" if no data was returned
                    caloriessum.setText("0");
                    proteinsum.setText("0");
                    carbohydratessum.setText("0");
                    fatsum.setText("0");
                    Toast.makeText(SummaryActivity.this, "No data found for this date.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Summary>> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "Network failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                // Set all TextViews to "0" on network failure as well
                caloriessum.setText("0");
                proteinsum.setText("0");
                carbohydratessum.setText("0");
                fatsum.setText("0");
            }
        });
    }

    private void fetchBurnedCalories(int userId, String date) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<ExerciseData>> call = apiService.getBurnedCalories(userId, date);

        call.enqueue(new Callback<List<ExerciseData>>() {
            @Override
            public void onResponse(Call<List<ExerciseData>> call, Response<List<ExerciseData>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Assuming there is only one entry or we only care about the first one
                    ExerciseData data = response.body().get(0);
                    burnedCaloriesTextView.setText(String.valueOf(data.getBurned_calories()));
                    burnedCaloriesFetched = true;
                    updateNetCaloriesIfNeeded();
                } else {
                    burnedCaloriesTextView.setText("0");
                    burnedCaloriesFetched = true;
                    updateNetCaloriesIfNeeded();
                    Toast.makeText(SummaryActivity.this, "No burned calories data found for this date.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseData>> call, Throwable t) {
                Toast.makeText(SummaryActivity.this, "Network failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                burnedCaloriesTextView.setText("0");
                burnedCaloriesFetched = true;
                updateNetCaloriesIfNeeded();
            }
        });
    }

    private void updateNetCaloriesIfNeeded() {
        if (summaryDataFetched && burnedCaloriesFetched) {
            try {
                int totalCalories = Integer.parseInt(caloriessum.getText().toString());
                int burnedCalories = Integer.parseInt(burnedCaloriesTextView.getText().toString());
                int netCalories = totalCalories - burnedCalories;
                netCaloriesTextView.setText(String.valueOf(netCalories));
            } catch (NumberFormatException e) {
                Toast.makeText(SummaryActivity.this, "Error calculating net calories", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
