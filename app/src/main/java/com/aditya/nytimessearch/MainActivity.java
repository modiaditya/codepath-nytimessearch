package com.aditya.nytimessearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.aditya.nytimessearch.models.NewsArticleCollection;
import com.aditya.nytimessearch.net.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchData();
    }

    private void fetchData() {
        RetrofitClient.fetchData(new Callback<NewsArticleCollection>() {
            @Override
            public void onResponse(Call<NewsArticleCollection> call, Response<NewsArticleCollection> response) {
                Log.e("amodi", response.body() + "");
            }

            @Override
            public void onFailure(Call<NewsArticleCollection> call, Throwable t) {
                //Toast.makeText(context, "Error getting data", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting data", t);
            }
        });
    }
}
