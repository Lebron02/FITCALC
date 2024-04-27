package com.example.fitcalc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_row, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        Log.d("ProductAdapter", "Product at position " + position + ": " + product.getProductName());
        holder.productName.setText(" " +product.getProductName());
        holder.calories.setText(" " + product.getCalories() + "Kcal");
        holder.weight.setText(" " + product.getWeight() + "g");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, calories, weight;

        public ProductViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.product_name);
            calories = view.findViewById(R.id.product_kcal);
            weight = view.findViewById(R.id.product_weight);
        }
    }

}
class Product {
    @SerializedName("product_name")
    private String productName;

    @SerializedName("calories")
    private int calories;

    @SerializedName("weight")
    private int weight;


    public Product(String productName, int calories, int weight) {
        this.productName = productName;
        this.calories = calories;
        this.weight = weight;
        Log.d("Product", "Creating product: " + productName);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCalories() {
        return calories;
    }

    public int getWeight() {
        return weight;
    }
}

