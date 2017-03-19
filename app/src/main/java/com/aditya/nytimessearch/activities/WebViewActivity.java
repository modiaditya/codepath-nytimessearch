package com.aditya.nytimessearch.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.nytimessearch.R;

/**
 * Created by amodi on 3/16/17.
 */

public class WebViewActivity extends AppCompatActivity {
    public static final String WEB_URL_EXTRA = "web_url_extra";

    @BindView(R.id.webview) WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);
        String webUrl = getIntent().getStringExtra(WEB_URL_EXTRA);
        if (!TextUtils.isEmpty(webUrl)) {
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            // Enable responsive layout
            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new MyBrowser());
            // Load the initial URL
            webView.loadUrl(webUrl);
        }

    }

    private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
