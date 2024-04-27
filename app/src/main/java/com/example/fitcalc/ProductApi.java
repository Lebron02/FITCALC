package com.example.fitcalc;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("/products/search")
    Call<List<Product>> getProductsByName(@Query("product_name") String productName);
}
