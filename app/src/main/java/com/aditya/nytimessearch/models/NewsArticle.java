package com.aditya.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amodi on 3/14/17.
 */

public class NewsArticle {

    @SerializedName("web_url")
    private String webUrl;
    private String snippet;
    private Headline headline;
    private String imageUrl;

    @SerializedName("multimedia")
    private List<Multimedia> multimediaList;

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getHeadline() {
        return headline.main;
    }

    public String getImageUrl() {
        if (imageUrl != null) {
            return imageUrl;
        }

        if (multimediaList == null || multimediaList.size() == 0) {
            return "";
        }

        for (Multimedia multimedia : multimediaList) {
            if (multimedia.subtype.equals("wide")) {
                imageUrl = multimedia.getUrl();
                return imageUrl;
            }
        }

        // thumbnail not found, return any image
        imageUrl= multimediaList.get(0).getUrl();
        return imageUrl;
    }

    static class Headline {
        String main;

        @SerializedName("print_headline")
        String printHeadline;

    }

    static class Multimedia {

        String url;
        String subtype;

        String getUrl() {
            return "http://www.nytimes.com/"+url;
        }

    }
}
