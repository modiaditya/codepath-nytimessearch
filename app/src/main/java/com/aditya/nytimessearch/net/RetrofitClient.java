package com.aditya.nytimessearch.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.aditya.nytimessearch.helpers.DateHelper;
import com.aditya.nytimessearch.models.Filter;
import com.aditya.nytimessearch.models.NewsArticle;
import com.aditya.nytimessearch.models.NewsArticleCollection;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
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

    public interface NewsArticleCollectionCallback {
        void onSuccess(List<NewsArticle> newsArticleList);
        void onLimitExceeded();
        void onFailure(Throwable t);
        void noInternetConnectivity();
    }

    public static void fetchData(Context context,
                                 String query,
                                 Filter filter,
                                 int page,
                                 NewsArticleCollectionCallback newsArticleCollectionCallback) {
        fetchData(context, query, filter, page, newsArticleCollectionCallback, 0);

    }

    private static void fetchData(final Context context,
                                  final String query,
                                  final Filter filter,
                                  final int page,
                                  final NewsArticleCollectionCallback newsArticleCollectionCallback,
                                  final int retry) {
        final Call<NewsArticleCollection> newsArticleCollectionCall =
            nytimesApiService.findNews(query,
                                       DateHelper.getFormattedDate(filter.beginDate),
                                       filter.sortOrder,
                                       getFdQueryString(filter.newsDesks),
                                       page);
        newsArticleCollectionCall.enqueue(new Callback<NewsArticleCollection>() {
            @Override
            public void onResponse(Call<NewsArticleCollection> call, Response<NewsArticleCollection> response) {
                if (response.code() == 429 && retry < 2) {
                    // retry 2 times
                    newsArticleCollectionCallback.onLimitExceeded();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            fetchData(context, query, filter, page, newsArticleCollectionCallback, (retry+1));
                        }
                    };
                    retryAfter3Seconds(runnable);
                    return;
                }
                if (response.body() != null && response.body().getArticles() != null) {
                    newsArticleCollectionCallback.onSuccess(response.body().getArticles());
                    return;
                }
            }

            @Override
            public void onFailure(Call<NewsArticleCollection> call, Throwable t) {
                // check if there is internet connectivity
                if (!isNetworkAvailable(context) || !isOnline()) {
                    newsArticleCollectionCallback.noInternetConnectivity();
                } else {
                    newsArticleCollectionCallback.onFailure(t);
                }

            }
        });
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

    private static void retryAfter3Seconds(Runnable runnable) {
        final Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
