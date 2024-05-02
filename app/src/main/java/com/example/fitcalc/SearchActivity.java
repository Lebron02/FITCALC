package com.example.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mealType = getIntent().getStringExtra("mealType");
        initializeViews();
        setupClickListeners();
        loadProducts();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        ImageView imageViewJedzenie = findViewById(R.id.imageView7);
        ImageView imageViewAnaliza = findViewById(R.id.imageView5);
        ImageView imageViewStopa = findViewById(R.id.imageView6);
        ImageView imageViewLudzik = findViewById(R.id.imageView4);
        EditText editTextSzukaj = findViewById(R.id.text_szukaj);
        Button buttonSzukaj = findViewById(R.id.szukaj_button);

        imageViewJedzenie.setOnClickListener(v -> navigateToActivity(DietActivity.class));
        imageViewAnaliza.setOnClickListener(v -> navigateToActivity(SummaryActivity.class));
        imageViewStopa.setOnClickListener(v -> navigateToActivity(TrainingActivity.class));
        imageViewLudzik.setOnClickListener(v -> navigateToActivity(UserActivity.class));

        buttonSzukaj.setOnClickListener(v -> searchProduct());
    }

    private void navigateToActivity(Class<?> cls) {
        Intent intent = new Intent(SearchActivity.this, cls);
        startActivity(intent);
    }

    private void loadProducts() {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProducts();

        call.enqueue(new ProductsCallback());
    }

    private void searchProduct() {
        String productName = getSearchQuery();
        if (!productName.isEmpty()) {
            loadProductsByName(productName);
            Toast.makeText(SearchActivity.this, "Szukanie: " + productName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SearchActivity.this, "Podaj produkt.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSearchQuery() {
        EditText editTextSzukaj = findViewById(R.id.text_szukaj);
        return editTextSzukaj.getText().toString().trim();
    }

    private void loadProductsByName(String productName) {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProductsByName(productName);

        call.enqueue(new ProductsCallback());
    }

    private void fetchProductDetails(String productName) {
        ProductApi api = RetrofitClient.getClient("http://10.0.2.2:3000/").create(ProductApi.class);
        Call<List<Product>> call = api.getProductDetails(productName);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Product detailedProduct = response.body().get(0); // Assuming the response returns at least one product
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
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        intent.putExtra("mealType", mealType);
        intent.putExtra("productDetails", product);
        startActivity(intent);
    }

    private class ProductsCallback implements Callback<List<Product>> {
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
    }
}
