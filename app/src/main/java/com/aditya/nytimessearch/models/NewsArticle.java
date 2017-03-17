package com.aditya.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amodi on 3/14/17.
 */

public class NewsArticle {

    private String webUrl;
    private String snippet;
    private Headline headline;

    static class Headline {
        String main;

        @SerializedName("print_headline")
        String printHeadline;

    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getHeadline() {
        return headline.main;
    }
}
