package com.example.fitcalc;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("/products/search")
    Call<List<Product>> getProductsByName(@Query("product_name") String productName);

    @GET("/products/search/details")
    Call<List<Product>> getProductDetails(@Query("product_name") String productName);

    @POST("addMeal")
    Call<Void> addMeal(@Body MealData mealData);

    @GET("meals")
    Call<List<Meal>> getMealsByUserAndDate(@Query("user_id") int userId, @Query("meal_date") String date);

    @GET("summary")
    Call<List<Summary>> getSummaryByUserAndDate(@Query("user_id") int userId, @Query("date") String date);

    @GET("inmeal")
    Call<List<MealItem>> getMealItems(@Query("meal_id") int mealId);

    @GET("mealid")
    Call<MealId> getMealId(@Query("user_id") int userId, @Query("meal_date") String mealDate, @Query("meal_type") String mealType);

}
