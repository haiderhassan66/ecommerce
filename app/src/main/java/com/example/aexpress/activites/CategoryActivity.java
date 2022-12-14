package com.example.aexpress.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aexpress.adapter.ProductAdapter;
import com.example.aexpress.databinding.ActivityCategoryBinding;
import com.example.aexpress.model.Product;
import com.example.aexpress.utils.Constraints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        int catId = getIntent().getIntExtra("catId",0);
        String categoryName = getIntent().getStringExtra("categoryName");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getProducts(catId);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

    }

    void getProducts(int catId){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constraints.GET_PRODUCTS_URL + "?category_id=" +catId;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productArray = object.getJSONArray("products");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject childObj = productArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("name"),
                                Constraints.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> { });

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}