package com.example.fitcalc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;


public class MealItemsAdapter extends RecyclerView.Adapter<MealItemsAdapter.ViewHolder> {
    private List<MealItem> mealItems;

    public MealItemsAdapter(List<MealItem> mealItems) {
        this.mealItems = mealItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MealItem item = mealItems.get(position);
        holder.productName.setText(item.getProductName());
        holder.productWeight.setText(String.format(Locale.getDefault(), "%d g", item.getWeight()));
        holder.productCalories.setText(String.format(Locale.getDefault(), "%d kcal", item.getCalories()));
    }

    @Override
    public int getItemCount() {
        return mealItems != null ? mealItems.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productWeight;
        TextView productCalories;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productWeight = itemView.findViewById(R.id.product_weight);
            productCalories = itemView.findViewById(R.id.product_kcal);
        }
    }
}
class MealId {
    private int meal_id ;

    public MealId(int meal_id ) {
        this.meal_id  = meal_id ;
    }

    public int getId() {
        return meal_id ;
    }

    public void setId(int meal_id ) {
        this.meal_id  = meal_id ;
    }
}
class MealItem {
    @SerializedName("product_name")
    private String productName;
    @SerializedName("kcal")
    private int calories;
    @SerializedName("weight")
    private int weight;

    public MealItem(String productName, int calories, int weight) {
        this.productName = productName;
        this.calories = calories;
        this.weight = weight;
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

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
