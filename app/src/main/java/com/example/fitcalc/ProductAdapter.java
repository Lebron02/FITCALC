package com.example.fitcalc;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static List<Product> productList;


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_row, parent, false);
        return new ProductViewHolder(itemView, listener);
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

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, calories, weight;

        public ProductViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            productName = view.findViewById(R.id.product_name);
            calories = view.findViewById(R.id.product_kcal);
            weight = view.findViewById(R.id.product_weight);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(productList.get(position));
                    }
                }
            });
        }
    }

}


class Product implements Parcelable {

    @SerializedName("product_id")
    private int productid;
    @SerializedName("product_name")
    private String productName;

    @SerializedName("calories")
    private int calories;

    @SerializedName("weight")
    private int weight;

    @SerializedName("protein")
    private int protein;

    @SerializedName("carbohydrates")
    private int carbohydrates;

    @SerializedName("fat")
    private int fat;

    protected Product(Parcel in) {
        productid = in.readInt();
        productName = in.readString();
        calories = in.readInt();
        weight = in.readInt();
        protein = in.readInt();
        carbohydrates = in.readInt();
        fat = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productid);
        dest.writeString(productName);
        dest.writeInt(calories);
        dest.writeInt(weight);
        dest.writeInt(protein);
        dest.writeInt(carbohydrates);
        dest.writeInt(fat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // Getters and setters

    public int getProductid() {
        return productid;
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

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}
class MealData {
    int user_id;
    String meal_date;
    String meal_type;
    int total_calories;
    int total_protein;
    int total_carbs;
    int total_fat;
    int product_id;  // Single product ID
    int weight;      // Weight of the single product
    int kcal;        // Calories of the single product

    // Constructor
    public MealData(int userId, String date, String mealType, int calories, int protein, int carbs, int fat, int productId, int weight, int kcal) {
        this.user_id = userId;
        this.meal_date = date;
        this.meal_type = mealType;
        this.total_calories = calories;
        this.total_protein = protein;
        this.total_carbs = carbs;
        this.total_fat = fat;
        this.product_id = productId;
        this.weight = weight;
        this.kcal = kcal;
    }

    // Getters and Setters
    public int getUser_id() { return user_id; }
    public String getMeal_date() { return meal_date; }
    public String getMeal_type() { return meal_type; }
    public int getTotal_calories() { return total_calories; }
    public int getTotal_protein() { return total_protein; }
    public int getTotal_carbs() { return total_carbs; }
    public int getTotal_fat() { return total_fat; }
    public int getProduct_id() { return product_id; }
    public int getWeight() { return weight; }
    public int getKcal() { return kcal; }
}
class Meal {
    // Member variables for the meal details
    private int meal_id;
    private int user_id;
    private String meal_date;
    private String meal_type;
    private int total_calories;
    private int total_protein;
    private int total_carbs;
    private int total_fat;

    // Constructor
    public Meal(int meal_id, int user_id, String meal_date, String meal_type, int total_calories, int total_protein, int total_carbs, int total_fat) {
        this.meal_id = meal_id;
        this.user_id = user_id;
        this.meal_date = meal_date;
        this.meal_type = meal_type;
        this.total_calories = total_calories;
        this.total_protein = total_protein;
        this.total_carbs = total_carbs;
        this.total_fat = total_fat;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getMeal_date() {
        return meal_date;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public int getTotal_calories() {
        return total_calories;
    }

    public int getTotal_protein() {
        return total_protein;
    }

    public int getTotal_carbs() {
        return total_carbs;
    }

    public int getTotal_fat() {
        return total_fat;
    }
}

class Summary {
    // Member variables for the meal details
    private int summary_id;
    private int user_id;
    private String date ;
    private int consumed_calories;
    private int consumed_protein;
    private int consumed_carbohydrates;
    private int consumed_fat;

    // Constructor
    public Summary(int summary_id, int user_id, String date, int consumed_calories, int consumed_protein, int consumed_carbohydrates, int consumed_fat) {
        this.summary_id = summary_id;
        this.user_id = user_id;
        this.date = date;
        this.consumed_calories = consumed_calories;
        this.consumed_protein = consumed_protein;
        this.consumed_carbohydrates = consumed_carbohydrates;
        this.consumed_fat = consumed_fat;
    }

    public int getSummary_id() {
        return summary_id;
    }
    public int getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public int getConsumed_calories() {
        return consumed_calories;
    }

    public int getConsumed_protein() {
        return consumed_protein;
    }

    public int getConsumed_carbohydrates() {
        return consumed_carbohydrates;
    }

    public int getConsumed_fat() {
        return consumed_fat;
    }
}



