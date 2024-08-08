package com.example.tessentuhdigitalpart2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tessentuhdigitalpart2.Adapters.ChuckAdapter;
import com.example.tessentuhdigitalpart2.Models.ChuckModel;
import com.example.tessentuhdigitalpart2.Services.ApiService;
import com.example.tessentuhdigitalpart2.URL.Url;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ProgressDialog loading;

    ArrayList<ChuckModel> listChuck = new ArrayList<>();
    ChuckAdapter chuckAdapter;
    RecyclerView rvChuck;
    LinearLayoutManager layoutChuck;

    MaterialButton btnSearch;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btn_search);
        etSearch = findViewById(R.id.et_search);

        rvChuck = findViewById(R.id.rv_chuck);
        layoutChuck = new LinearLayoutManager(this);
        rvChuck.setLayoutManager(layoutChuck);
        listChuck = new ArrayList<>();
        chuckAdapter = new ChuckAdapter(this, listChuck);
        rvChuck.setAdapter(chuckAdapter);
        chuckAdapter.notifyDataSetChanged();

        btnSearch.setOnClickListener(view -> {
            loadItem(etSearch.getText().toString());
        });

        loadItem("empty");
    }


    private void loadItem(String query){
        try {

            android.content.SharedPreferences sharedPref = this.getSharedPreferences("PrefIP", Context.MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = sharedPref.edit();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Url.BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            // Create the ApiService interface
            ApiService apiService = retrofit.create(ApiService.class);

            // Make the request
            Call<JsonObject> call = apiService.getDataChuckNorris(query);

            loading = ProgressDialog.show(MainActivity.this, "Loading", "Harap tunggu sebentar...", false, false, null);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {

                    try {
                        listChuck = new ArrayList<>();
//                        assert response.body() != null;
                        String rawResponse = response.body().toString();
                        JSONObject jsonResponse = new JSONObject(rawResponse);
                        Integer total = jsonResponse.getInt("total");
                        if (total > 0) {
                            JSONArray data = jsonResponse.getJSONArray("result");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                ChuckModel itemModel = new ChuckModel();
                                itemModel.setCategories(item.getJSONArray("categories"));
                                itemModel.setId(item.getString("id"));
                                itemModel.setIcon_url(item.getString("icon_url"));
                                itemModel.setValue(item.getString("value"));
                                listChuck.add(itemModel);
                            }
                            chuckAdapter = new ChuckAdapter(MainActivity.this, listChuck);
                            rvChuck.setAdapter(chuckAdapter);
                            chuckAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Data kosong! ", Toast.LENGTH_LONG).show();
                        }
                        loading.dismiss();
                    } catch (JSONException e) {
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        loading.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    loading.dismiss();
                    try {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}