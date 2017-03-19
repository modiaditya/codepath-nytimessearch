package com.aditya.nytimessearch.net;

import com.aditya.nytimessearch.helpers.DateHelper;
import com.aditya.nytimessearch.models.Filter;
import com.aditya.nytimessearch.models.NewsArticleCollection;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

/**
 * Created by amodi on 3/16/17.
 */

public class RetrofitClient {

    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/";
    private static Retrofit retrofit;
    private static NytimesApiEndpointInterface nytimesApiService;

    static {
        OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        nytimesApiService = retrofit.create(NytimesApiEndpointInterface.class);
    }

    public static void fetchData(String query,
                                 Filter filter,
                                 Callback<NewsArticleCollection> newsArticleCollectionCallback) {
        Call<NewsArticleCollection> newsArticleCollectionCall =
            nytimesApiService.findNews(query,
                                       DateHelper.getFormattedDate(filter.beginDate),
                                       filter.sortOrder,
                                       getFdQueryString(filter.newsDesks));
        newsArticleCollectionCall.enqueue(newsArticleCollectionCallback);
    }

    private static String getFdQueryString(List<String> newsDesks) {
        if (newsDesks == null || newsDesks.size() == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String newsDesk : newsDesks) {
            sb.append("\"").append(newsDesk).append("\"");
        }
        return String.format("news_desk:(%s)", sb.toString());
    }
}
