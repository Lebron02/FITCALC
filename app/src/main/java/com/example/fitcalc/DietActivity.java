package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietActivity extends AppCompatActivity {

    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        int userId_int = Integer.parseInt(userId);

        // Sprawdzanie, czy istnieje zapisana data
        String savedDate = sharedPreferences.getString("selectedDate", null);

        // Inicjalizacja dat i ustawienie widoków dat
        initializeDate(savedDate);

        Log.d("DietActivity", "Fetching meals for user ID: " + userId + " on date: " + formattedDate);

        fetchData(userId_int);

        initializeViews();

        setupClickListeners();
    }

    private void initializeDate(String currentDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fdate = new SimpleDateFormat("dd-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        if (currentDate != null) {
            formattedDate = currentDate;
        } else {
            formattedDate = dateFormat.format(calendar.getTime());
        }

        String tempDate = formattedDate;

        try {
            Date formattedDateObj = dateFormat.parse(tempDate);
            tempDate = fdate.format(formattedDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Ustawienie i formatowanie daty dla "dzisiaj"
        String dateToday = fdate.format(calendar.getTime());
        TextView textViewToday = findViewById(R.id.textView_today);
        textViewToday.setText(dateToday);

        // Ustawienie i formatowanie daty dla "jutro"
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String dateTomorrow = fdate.format(calendar.getTime());
        TextView textViewTomorrow = findViewById(R.id.textView_tomorrow);
        textViewTomorrow.setText(dateTomorrow);

        // Ustawienie i formatowanie daty dla "wczoraj"
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        String dateYesterday = fdate.format(calendar.getTime());
        TextView textViewYesterday = findViewById(R.id.textView_yesterday);
        textViewYesterday.setText(dateYesterday);

        Log.d("DietActivity", "formatted" + tempDate);
        Log.d("DietActivity", "date" + dateToday);
        // Porównanie formattedDate z datami "jutro", "wczoraj" i "dzisiaj" i ustawienie koloru tekstu
        if (tempDate.equals(dateTomorrow)) {
            textViewTomorrow.setTextColor(Color.RED);
        }else {
            textViewTomorrow.setTextColor(Color.WHITE);
        }
        if (tempDate.equals(dateYesterday)) {
            textViewYesterday.setTextColor(Color.RED);
        }else {
            textViewYesterday.setTextColor(Color.WHITE);
        }
        if (tempDate.equals(dateToday)) {
            textViewToday.setTextColor(Color.RED);
        }else {
            textViewToday.setTextColor(Color.WHITE);
        }

        setupDateClickListeners(dateFormat, calendar);
    }

    private void setupDateClickListeners(SimpleDateFormat dateFormat, Calendar calendar) {
        TextView textViewToday = findViewById(R.id.textView_today);
        TextView textViewTomorrow = findViewById(R.id.textView_tomorrow);
        TextView textViewYesterday = findViewById(R.id.textView_yesterday);

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        int userId_int = Integer.parseInt(userId);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        textViewToday.setOnClickListener(v -> {
            calendar.setTime(new Date()); // Reset do dzisiaj
            formattedDate = dateFormat.format(calendar.getTime());
            editor.putString("selectedDate", formattedDate);
            editor.apply();
            fetchData(userId_int);
            initializeDate(formattedDate);
        });

        textViewTomorrow.setOnClickListener(v -> {
            calendar.setTime(new Date()); // Reset
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Jutro
            formattedDate = dateFormat.format(calendar.getTime());
            editor.putString("selectedDate", formattedDate);
            editor.apply();
            fetchData(userId_int);
            initializeDate(formattedDate);
        });

        textViewYesterday.setOnClickListener(v -> {
            calendar.setTime(new Date()); // Reset
            calendar.add(Calendar.DAY_OF_YEAR, -1); // Wczoraj
            formattedDate = dateFormat.format(calendar.getTime());
            editor.putString("selectedDate", formattedDate);
            editor.apply();
            fetchData(userId_int);
            initializeDate(formattedDate);
        });
    }

    private void fetchData(int userId) {
        fetchMealData(userId, formattedDate);
        fetchSummaryData(userId, formattedDate);
    }

    private void initializeViews() {
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        Button buttonAddSniadanie = findViewById(R.id.textview_sniadanie_button);
        Button buttonAddLunch = findViewById(R.id.textview_lunch_button);
        Button buttonAddObiad = findViewById(R.id.textview_obiad_button);
        Button buttonAddKolacja = findViewById(R.id.textview_kolacja_button);

        setupTextViewClickListener(R.id.textview_sniadanie, "śniadanie");
        setupTextViewClickListener(R.id.textview_lunch, "lunch");
        setupTextViewClickListener(R.id.textview_obiad, "obiad");
        setupTextViewClickListener(R.id.textview_kolacja, "kolacja");
    }
    private void setupTextViewClickListener(int textViewId, String mealType) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(DietActivity.this, MealItemsActivity.class);
            intent.putExtra("mealType", mealType);
            startActivity(intent);
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.imageView6).setOnClickListener(v -> startActivity(new Intent(DietActivity.this, TrainingActivity.class)));
        findViewById(R.id.imageView5).setOnClickListener(v -> startActivity(new Intent(DietActivity.this, SummaryActivity.class)));
        findViewById(R.id.imageView4).setOnClickListener(v -> startActivity(new Intent(DietActivity.this, UserActivity.class)));

        setupMealButtons();
    }

    private void setupMealButtons() {
        setupMealButton(R.id.textview_sniadanie_button, "śniadanie");
        setupMealButton(R.id.textview_lunch_button, "lunch");
        setupMealButton(R.id.textview_obiad_button, "obiad");
        setupMealButton(R.id.textview_kolacja_button, "kolacja");
    }

    private void setupMealButton(int buttonId, String mealType) {
        findViewById(buttonId).setOnClickListener(v -> {
            Intent intent = new Intent(DietActivity.this, SearchActivity.class);
            intent.putExtra("mealType", mealType);
            startActivity(intent);
        });
    }
    private void fetchMealData(int userId, String date) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Meal>> call = apiService.getMealsByUserAndDate(userId, date);
        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealAdapter(response.body());
                } else {
                    Toast.makeText(DietActivity.this, "Error fetching meals: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                Toast.makeText(DietActivity.this, "Network failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchSummaryData(int userId, String date) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Summary>> call = apiService.getSummaryByUserAndDate(userId, date);
        call.enqueue(new Callback<List<Summary>>() {
            @Override
            public void onResponse(Call<List<Summary>> call, Response<List<Summary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SummaryAdapter(response.body());
                } else {
                    Toast.makeText(DietActivity.this, "Error fetching meals: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Summary>> call, Throwable t) {
                Toast.makeText(DietActivity.this, "Network failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void MealAdapter(List<Meal> meals) {

        ((TextView) findViewById(R.id.textview_sniadanie_kcal)).setText("0 kcal");
        ((TextView) findViewById(R.id.textview_sniadanie_b)).setText("0 g");
        ((TextView) findViewById(R.id.textview_sniadanie_t)).setText("0 g");
        ((TextView) findViewById(R.id.textview_sniadanie_w)).setText("0 g");

        ((TextView) findViewById(R.id.textview_lunch_kcal)).setText("0 kcal");
        ((TextView) findViewById(R.id.textview_lunch_b)).setText("0 g");
        ((TextView) findViewById(R.id.textview_lunch_t)).setText("0 g");
        ((TextView) findViewById(R.id.textview_lunch_w)).setText("0 g");

        ((TextView) findViewById(R.id.textview_obiad_kcal)).setText("0 kcal");
        ((TextView) findViewById(R.id.textview_obiad_b)).setText("0 g");
        ((TextView) findViewById(R.id.textview_obiad_t)).setText("0 g");
        ((TextView) findViewById(R.id.textview_obiad_w)).setText("0 g");

        ((TextView) findViewById(R.id.textview_kolacja_kcal)).setText("0 kcal");
        ((TextView) findViewById(R.id.textview_kolacja_b)).setText("0 g");
        ((TextView) findViewById(R.id.textview_kolacja_t)).setText("0 g");
        ((TextView) findViewById(R.id.textview_kolacja_w)).setText("0 g");

        for (Meal meal : meals) {
            if (meal.getMeal_type() == null) {
                Log.e("MealAdapter", "Meal or Meal type is null");
                continue;
            }
            String mealType = meal.getMeal_type().toLowerCase();
            switch (mealType) {
                case "śniadanie":
                    ((TextView) findViewById(R.id.textview_sniadanie_kcal)).setText(meal.getTotal_calories() + " kcal");
                    ((TextView) findViewById(R.id.textview_sniadanie_b)).setText(meal.getTotal_protein() + " g");
                    ((TextView) findViewById(R.id.textview_sniadanie_t)).setText(meal.getTotal_carbs() + " g");
                    ((TextView) findViewById(R.id.textview_sniadanie_w)).setText(meal.getTotal_fat() + " g");
                    break;
                case "lunch":
                    ((TextView) findViewById(R.id.textview_lunch_kcal)).setText(meal.getTotal_calories() + " kcal");
                    ((TextView) findViewById(R.id.textview_lunch_b)).setText(meal.getTotal_protein() + " g");
                    ((TextView) findViewById(R.id.textview_lunch_t)).setText(meal.getTotal_carbs() + " g");
                    ((TextView) findViewById(R.id.textview_lunch_w)).setText(meal.getTotal_fat() + " g");
                    break;
                case "obiad":
                    ((TextView) findViewById(R.id.textview_obiad_kcal)).setText(meal.getTotal_calories() + " kcal");
                    ((TextView) findViewById(R.id.textview_obiad_b)).setText(meal.getTotal_protein() + " g");
                    ((TextView) findViewById(R.id.textview_obiad_t)).setText(meal.getTotal_carbs() + " g");
                    ((TextView) findViewById(R.id.textview_obiad_w)).setText(meal.getTotal_fat() + " g");
                    break;
                case "kolacja":
                    ((TextView) findViewById(R.id.textview_kolacja_kcal)).setText(meal.getTotal_calories() + " kcal");
                    ((TextView) findViewById(R.id.textview_kolacja_b)).setText(meal.getTotal_protein() + " g");
                    ((TextView) findViewById(R.id.textview_kolacja_t)).setText(meal.getTotal_carbs() + " g");
                    ((TextView) findViewById(R.id.textview_kolacja_w)).setText(meal.getTotal_fat() + " g");
                    break;
            }
        }
    }
    void SummaryAdapter(List<Summary> meals) {
        ((TextView) findViewById(R.id.textview_podsumowanie_kcal)).setText("0 kcal");
        ((TextView) findViewById(R.id.textview_podsumowanie_b)).setText("0 g");
        ((TextView) findViewById(R.id.textview_podsumowanie_w)).setText("0 g");
        ((TextView) findViewById(R.id.textview_podsumowanie_t)).setText("0 g");
        for (Summary summary : meals) {
            if (summary.getSummary_id() == 0) {
                Log.e("SummaryAdapter", "Summary is null");
                continue;
            }
            ((TextView) findViewById(R.id.textview_podsumowanie_kcal)).setText(summary.getConsumed_calories() + " kcal");
            ((TextView) findViewById(R.id.textview_podsumowanie_b)).setText(summary.getConsumed_protein() + " g");
            ((TextView) findViewById(R.id.textview_podsumowanie_w)).setText(summary.getConsumed_carbohydrates() + " g");
            ((TextView) findViewById(R.id.textview_podsumowanie_t)).setText(summary.getConsumed_fat() + " g");
        }
    }

}
