package com.aditya.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.nytimessearch.activities.WebViewActivity;
import com.aditya.nytimessearch.models.NewsArticle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amodi on 3/16/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleItemViewHolder> {

    private static final int TEXT_ONLY = 0;
    private static final int TEXT_WITH_IMAGE = 1;

    List<NewsArticle> newsArticleList;
    Context context;


    public ArticleAdapter(Context context) {
        newsArticleList = new ArrayList<>();
        this.context = context;
    }



    @Override
    public ArticleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(viewType == TEXT_ONLY ? R.layout.article_text_item : R.layout.article_item,
                                        parent,
                                        false);
        return new ArticleItemViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ArticleItemViewHolder holder, int position) {
        NewsArticle newsArticle = newsArticleList.get(position);
        holder.headline.setText(newsArticle.getHeadline());
        holder.snippet.setText(newsArticle.getSnippet());
        if (getItemViewType(position) == TEXT_WITH_IMAGE) {
            holder.thumbnail.setImageResource(0);
            String imageUrl = newsArticle.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_newspaper).into(holder.thumbnail);
            }
        }

    }


    @Override
    public int getItemCount() {
        return newsArticleList.size();
    }

    public void addNewsArticleItems(List<NewsArticle> newsArticles) {
        newsArticleList.addAll(newsArticles);
    }

    public void addNewsArticleItem(NewsArticle newsArticle) {
        newsArticleList.add(newsArticle);
    }

    public void clearNewsArticleItems() {
        newsArticleList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        NewsArticle newsArticle = newsArticleList.get(position);
        if (TextUtils.isEmpty(newsArticle.getImageUrl())) {
            return TEXT_ONLY;
        } else {
            return TEXT_WITH_IMAGE;
        }
    }

     class ArticleItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardview) CardView cardView;
        @BindView(R.id.ivThumbnail) @Nullable ImageView thumbnail;
        @BindView(R.id.tvHeadline) TextView headline;
        @BindView(R.id.tvSnippet) TextView snippet;

        public ArticleItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String webUrl = newsArticleList.get(getAdapterPosition()).getWebUrl();
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.WEB_URL_EXTRA, webUrl);
                    context.startActivity(intent);
                }
            });
        }


    }
}
