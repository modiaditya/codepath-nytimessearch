package com.aditya.nytimessearch.net;

import com.aditya.nytimessearch.models.NewsArticleCollection;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by amodi on 3/16/17.
 */

public interface NytimesApiEndpointInterface {

    @GET("v2/articlesearch.json?api-key=eb47916a305b4291b5fd4e57b5d0295b")
    Call<NewsArticleCollection> findByBlah();

}
