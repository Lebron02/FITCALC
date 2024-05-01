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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    private String userId;
    private String date;
    private String mealType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        SharedPreferences sharedPreferences = getSharedPreferences("FitCalcPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "DefaultUser");
        int userId_int = Integer.parseInt(userId);

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

            // Retrieve the product from the intent
        date = getIntent().getStringExtra("date");
        mealType = getIntent().getStringExtra("mealType");
        Product product = getIntent().getParcelableExtra("productDetails");

        imageViewJedzenie.setOnClickListener(v -> startActivity(new Intent(DetailsActivity.this, DietActivity.class)));
        imageViewAnaliza.setOnClickListener(v -> startActivity(new Intent(DetailsActivity.this, SummaryActivity.class)));
        imageViewStopa.setOnClickListener(v -> startActivity(new Intent(DetailsActivity.this, TrainingActivity.class)));
        imageViewLudzik.setOnClickListener(v -> startActivity(new Intent(DetailsActivity.this, UserActivity.class)));

            if (product != null) {
                productName.setText(product.getProductName());
                calories.setText(String.valueOf(product.getCalories()) + " Kcal"); // assuming there's a getCalories method
                weight.setText(String.valueOf(product.getWeight()) + " g");
                carbohydrates.setText(String.valueOf(product.getCarbohydrates()) + " g");
                protein.setText(String.valueOf(product.getProtein()) + " g");
                fat.setText(String.valueOf(product.getFat()) + " g");
            } else {
                Toast.makeText(this, "No product details found.", Toast.LENGTH_LONG).show();
            }
        przelicz.setOnClickListener(v -> {
            try {
                double newWeight = Double.parseDouble(weight.getText().toString().replace(" g", ""));
                double factor = newWeight / product.getWeight();

                calories.setText((int) (product.getCalories() * factor) + " Kcal");
                carbohydrates.setText((int) (product.getCarbohydrates() * factor) + " g");
                protein.setText((int) (product.getProtein() * factor) + " g");
                fat.setText((int) (product.getFat() * factor) + " g");
            } catch (NumberFormatException e) {
                Toast.makeText(DetailsActivity.this, "Please enter a valid weight.", Toast.LENGTH_SHORT).show();
            }
        });



        Log.d("DietActivity", "User ID: " + product.getProductid());


        int finalUserId_int = userId_int;
        buttonDodaj.setOnClickListener(v -> {
            try {
                // Parsing input values
                int useridValue = Integer.parseInt(userId);
                int calorieValue = Integer.parseInt(calories.getText().toString().replace(" Kcal", "").trim());
                int carbohydrateValue = Integer.parseInt(carbohydrates.getText().toString().replace(" g", "").trim());
                int proteinValue = Integer.parseInt(protein.getText().toString().replace(" g", "").trim());
                int fatValue = Integer.parseInt(fat.getText().toString().replace(" g", "").trim());
                int weightValue = Integer.parseInt(weight.getText().toString().replace(" g", "").trim());
                int productIdValue = product.getProductid();

                // Initializing MealData with a single product detail
                MealData mealData = new MealData(useridValue, date, mealType, calorieValue, proteinValue, carbohydrateValue, fatValue, productIdValue, weightValue, calorieValue);

                // Assuming Retrofit setup and API interface are correctly configured
                ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
                Call<Void> call = api.addMeal(mealData);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailsActivity.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                            //DietActivity.fetchMealData(finalUserId_int, date);
                            //DietActivity.fetchSummaryData(finalUserId_int, date);
                            Log.d("DietActivity", "User ID: " + userId);
                            startActivity(new Intent(DetailsActivity.this, DietActivity.class));
                        } else {
                            Toast.makeText(DetailsActivity.this, "Failed to add meal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(DetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(DetailsActivity.this, "Invalid number format in input fields.", Toast.LENGTH_LONG).show();
            }
        });


    }

}
