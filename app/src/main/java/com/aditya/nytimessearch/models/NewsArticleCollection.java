package com.aditya.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amodi on 3/16/17.
 */

public class NewsArticleCollection {

    @SerializedName("status")
    private String status;

    @SerializedName("response")
    private Response response;

    static class Response {
        @SerializedName("docs")
        private List<NewsArticle> articles;
    }

    public List<NewsArticle> getArticles() {
        return response.articles;
    }

//    @SerializedName("docs")
//    @Expose
//    private List<NewsArticle> articles;
//
//    public List<NewsArticle> getArticles() {
//        return articles;
//    }
//
//    public void setArticles(List<NewsArticle> articles) {
//        this.articles = articles;
//    }
}
