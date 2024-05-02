package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MealItemsAdapter adapter;
    private List<MealItem> mealItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealitems);
        initializeViews();
        loadMealData();
    }
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealItemsAdapter(mealItemsList);
        recyclerView.setAdapter(adapter);
        setupClickListeners();
    }
    private void loadMealData() {
        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        String meal_date = sharedPreferences.getString("selectedDate", null);
        int userId_int = Integer.parseInt(userId);
        String mealType = getIntent().getStringExtra("mealType");

        Log.d("MealItemsActivity", "Fetching for User ID: " + userId_int + ", Date: " + meal_date + ", Meal Type: " + mealType);
        getMealIds(userId_int, meal_date, mealType);
    }
    private void setupClickListeners() {
        findViewById(R.id.imageView7).setOnClickListener(v -> startActivity(new Intent(MealItemsActivity.this, DietActivity.class)));
        findViewById(R.id.imageView6).setOnClickListener(v -> startActivity(new Intent(MealItemsActivity.this, TrainingActivity.class)));
        findViewById(R.id.imageView5).setOnClickListener(v -> startActivity(new Intent(MealItemsActivity.this, SummaryActivity.class)));
        findViewById(R.id.imageView4).setOnClickListener(v -> startActivity(new Intent(MealItemsActivity.this, UserActivity.class)));
    }
    private void getMealIds(int userId_int, String meal_date, String meal_type){
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<MealId> call = apiService.getMealId(userId_int, meal_date, meal_type);

        call.enqueue(new Callback<MealId>() {
            @Override
            public void onResponse(Call<MealId> call, Response<MealId> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealId mealId = response.body();
                    Log.d("MealItemsActivity", "Fetched meal ID: " + mealId.getId());
                    fetchMealItems(mealId.getId());
                } else {
                    Log.e("MealItemsActivity", "No meal found or error: " + response.code());
                    Toast.makeText(MealItemsActivity.this, "No meal found or error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MealId> call, Throwable t) {
                Log.e("MealItemsActivity", "Network or server error: ", t);
                Toast.makeText(MealItemsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchMealItems(int mealId) {
        ProductApi apiService = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<MealItem>> call = apiService.getMealItems(mealId);
        call.enqueue(new Callback<List<MealItem>>() {
            @Override
            public void onResponse(Call<List<MealItem>> call, Response<List<MealItem>> response) {
                if (response.isSuccessful()) {
                    mealItemsList.clear();
                    mealItemsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MealItemsActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MealItem>> call, Throwable t) {
                Toast.makeText(MealItemsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
