package com.aditya.nytimessearch.net;

import com.aditya.nytimessearch.models.NewsArticleCollection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amodi on 3/16/17.
 */

public class RetrofitClient {

    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/";
    private static Retrofit retrofit;
    private static NytimesApiEndpointInterface nytimesApiService;

    static {
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        nytimesApiService = retrofit.create(NytimesApiEndpointInterface.class);
    }

    public static void fetchData(Callback<NewsArticleCollection> newsArticleCollectionCallback) {
        Call<NewsArticleCollection> newsArticleCollectionCall = nytimesApiService.findByBlah();
        newsArticleCollectionCall.enqueue(newsArticleCollectionCallback);
    }






}
