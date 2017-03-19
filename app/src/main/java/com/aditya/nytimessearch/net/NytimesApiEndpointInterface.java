package com.aditya.nytimessearch.net;

import com.aditya.nytimessearch.models.NewsArticleCollection;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by amodi on 3/16/17.
 */

public interface NytimesApiEndpointInterface {

    @GET("v2/articlesearch.json?api-key=eb47916a305b4291b5fd4e57b5d0295b")
    Call<NewsArticleCollection> findNews(@Query("q") String query,
                                         @Query("begin_date") String beginDate,
                                         @Query("sort") String sort,
                                         @Query("fq") String newsDesk);
}
