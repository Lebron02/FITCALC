package com.example.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    private String userId;
    private String date;
    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userId = getIntent().getStringExtra("userId");
        date = getIntent().getStringExtra("date");
        mealType = getIntent().getStringExtra("mealType");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(productList);

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Toast.makeText(SearchActivity.this, "Clicked: " + product.getProductName(), Toast.LENGTH_SHORT).show();
                fetchProductDetails(product.getProductName());
            }
        });

        recyclerView.setAdapter(adapter);

        loadProducts();

        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        EditText editTextSzukaj = findViewById(R.id.text_szukaj);
        Button buttonSzukaj = findViewById(R.id.szukaj_button);

        imageViewJedzenie.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, DietActivity.class);
            startActivity(intent);
        });

        imageViewAnaliza.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, SummaryActivity.class);
            startActivity(intent);
        });

        imageViewStopa.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, TrainingActivity.class);
            startActivity(intent);
        });

        imageViewLudzik.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, UserActivity.class);
            startActivity(intent);
        });

        buttonSzukaj.setOnClickListener(v -> {
            final String product = editTextSzukaj.getText().toString().trim();
            if (!product.isEmpty()) {
                loadProductsv2(product);
                Toast.makeText(SearchActivity.this, "Szukanie: " + product, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchActivity.this, "Podaj produkt.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProducts() {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadProductsv2(String productName) {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProductsByName(productName);  // Adjusted to use a new API method

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void fetchProductDetails(String productName) {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProductDetails(productName);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Product detailedProduct = response.body().get(0); // Assuming the response returns at least one product
                    // Here you can update the UI with the details or open a new detail activity
                    showProductDetails(detailedProduct);
                } else {
                    Toast.makeText(SearchActivity.this, "No details found for selected product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error fetching product details: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showProductDetails(Product product) {
        Intent Intent = new Intent(SearchActivity.this, DetailsActivity.class);
        Intent.putExtra("userId", userId);
        Intent.putExtra("date", date);
        Intent.putExtra("mealType", mealType);
        Intent.putExtra("productDetails", product);
        startActivity(Intent);
    }
}
