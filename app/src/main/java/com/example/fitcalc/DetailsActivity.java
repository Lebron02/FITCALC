package com.example.fitcalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initializeViews();
        setupClickListeners();
        displayProductDetails();
    }

    private void initializeViews() {
        TextView productName = findViewById(R.id.text_name);
        TextView calories = findViewById(R.id.kalorie);
        EditText weight = findViewById(R.id.weight);
        EditText carbohydrates = findViewById(R.id.carbohydrates);
        EditText protein = findViewById(R.id.protein);
        EditText fat = findViewById(R.id.fat);
        Button przelicz = findViewById(R.id.button_przelicz);
        Button buttonDodaj = findViewById(R.id.button_dodaj);
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        String date = sharedPreferences.getString("selectedDate", null);

        mealType = getIntent().getStringExtra("mealType");

        imageViewJedzenie.setOnClickListener(v -> navigateToActivity(DietActivity.class));
        imageViewAnaliza.setOnClickListener(v -> navigateToActivity(SummaryActivity.class));
        imageViewStopa.setOnClickListener(v -> navigateToActivity(TrainingActivity.class));
        imageViewLudzik.setOnClickListener(v -> navigateToActivity(UserActivity.class));
    }

    private void setupClickListeners() {
        Button buttonDodaj = findViewById(R.id.button_dodaj);
        buttonDodaj.setOnClickListener(v -> addMeal());
    }

    private void navigateToActivity(Class<?> cls) {
        Intent intent = new Intent(DetailsActivity.this, cls);
        startActivity(intent);
    }

    private void displayProductDetails() {
        Product product = getIntent().getParcelableExtra("productDetails");
        if (product != null) {
            populateFields(product);
        } else {
            Toast.makeText(this, "No product details found.", Toast.LENGTH_LONG).show();
        }
    }

    private void populateFields(Product product) {
        TextView productName = findViewById(R.id.text_name);
        TextView calories = findViewById(R.id.kalorie);
        EditText weight = findViewById(R.id.weight);
        EditText carbohydrates = findViewById(R.id.carbohydrates);
        EditText protein = findViewById(R.id.protein);
        EditText fat = findViewById(R.id.fat);

        productName.setText(product.getProductName());
        calories.setText(product.getCalories() + " Kcal");
        weight.setText(product.getWeight() + " g");
        carbohydrates.setText(product.getCarbohydrates() + " g");
        protein.setText(product.getProtein() + " g");
        fat.setText(product.getFat() + " g");
    }

    private void addMeal() {
        EditText weight = findViewById(R.id.weight);
        TextView calories = findViewById(R.id.kalorie);
        TextView carbohydrates = findViewById(R.id.carbohydrates);
        TextView protein = findViewById(R.id.protein);
        TextView fat = findViewById(R.id.fat);

        try {
            Product product = getIntent().getParcelableExtra("productDetails");
            if (product != null) {
                int userId = Integer.parseInt(getSharedPreferences("FitCalcPrefs", MODE_PRIVATE).getString("userId", "DefaultUser"));
                int calorieValue = Integer.parseInt(calories.getText().toString().replace(" Kcal", "").trim());
                int carbohydrateValue = Integer.parseInt(carbohydrates.getText().toString().replace(" g", "").trim());
                int proteinValue = Integer.parseInt(protein.getText().toString().replace(" g", "").trim());
                int fatValue = Integer.parseInt(fat.getText().toString().replace(" g", "").trim());
                int weightValue = Integer.parseInt(weight.getText().toString().replace(" g", "").trim());
                int productIdValue = product.getProductid();
                String date = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE).getString("selectedDate", null);

                MealData mealData = new MealData(userId, date, mealType, calorieValue, proteinValue, carbohydrateValue, fatValue, productIdValue, weightValue, calorieValue);
                ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
                Call<Void> call = api.addMeal(mealData);
                call.enqueue(new AddMealCallback());
            } else {
                Toast.makeText(DetailsActivity.this, "Product details not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(DetailsActivity.this, "Invalid input format.", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddMealCallback implements Callback<Void> {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                Toast.makeText(DetailsActivity.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsActivity.this, DietActivity.class));
            } else {
                Toast.makeText(DetailsActivity.this, "Failed to add meal", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(DetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
