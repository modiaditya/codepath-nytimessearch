package com.aditya.nytimessearch;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.nytimessearch.fragments.FilterDialogFragment;
import com.aditya.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.aditya.nytimessearch.models.Filter;
import com.aditya.nytimessearch.models.NewsArticle;
import com.aditya.nytimessearch.net.RetrofitClient;

import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FILTER_EXTRA = "filter_extra";
    private static final String SEARCH_QUERY_EXTRA = "search_query_extra";

    @BindView(R.id.rvNewsArticles) RecyclerView newsArticleRecyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.relative_layout) RelativeLayout relativeLayout;

    StaggeredGridLayoutManager staggeredGridLayoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    ArticleAdapter articleAdapter;
    String searchQuery;
    Filter filter;


    @Override
    public void onSave(Filter filter) {
        this.filter = filter;
        clearData();
        fetchData();
    }

    @Override
    public void onDismiss() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.SecondaryStyle);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        filter = new Filter();
        articleAdapter = new ArticleAdapter(this);
        newsArticleRecyclerView.setAdapter(articleAdapter);
        int numCols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(numCols, VERTICAL);
        newsArticleRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchData(page);
            }
        };
        newsArticleRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                clearData();
                searchView.clearFocus();
                fetchData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final MenuItem filter = menu.findItem(R.id.action_filter);
        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FilterDialogFragment filterFragment = FilterDialogFragment.newInstance(MainActivity.this.filter);
                filterFragment.show(getSupportFragmentManager(), "filterDialog");
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_filter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILTER_EXTRA, filter);
        outState.putString(SEARCH_QUERY_EXTRA, searchQuery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            filter = savedInstanceState.getParcelable(FILTER_EXTRA);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY_EXTRA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchQuery != null) {
            // restoring
            fetchData();
        }

    }

    private void fetchData(int page) {
        RetrofitClient.fetchData(this,
                                 searchQuery,
                                 filter,
                                 page,
                                 new RetrofitClient.NewsArticleCollectionCallback() {
             @Override
             public void onSuccess(List<NewsArticle> newsArticleList) {
                 articleAdapter.addNewsArticleItems(newsArticleList);
                 articleAdapter.notifyDataSetChanged();
             }

             @Override
             public void onLimitExceeded() {
                 Snackbar.make(relativeLayout, R.string.limit_exceeded, Snackbar.LENGTH_SHORT).show();
             }

             @Override
             public void onFailure(Throwable t) {
                 Log.e(TAG, "Error while fetching data from from the server", t);
                 Snackbar.make(relativeLayout, R.string.network_error, Snackbar.LENGTH_SHORT).show();
             }

             @Override
             public void noInternetConnectivity() {
                 Snackbar.make(relativeLayout, R.string.no_internet, Snackbar.LENGTH_LONG).show();
             }
        });
    }

    private void fetchData() {
        fetchData(0);
    }

    private void clearData() {
        articleAdapter.clearNewsArticleItems();
        articleAdapter.notifyDataSetChanged();
        endlessRecyclerViewScrollListener.resetState();
    }
}
