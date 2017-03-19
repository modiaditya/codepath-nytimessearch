package com.aditya.nytimessearch;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.aditya.nytimessearch.fragments.FilterDialogFragment;
import com.aditya.nytimessearch.models.Filter;
import com.aditya.nytimessearch.models.NewsArticleCollection;
import com.aditya.nytimessearch.net.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rvNewsArticles) RecyclerView newsArticleRecyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    ArticleAdapter articleAdapter;
    String searchQuery;
    Filter filter;


    @Override
    public void onSave(Filter filter) {
        this.filter = filter;
        articleAdapter.clearNewsArticleItems();
        articleAdapter.notifyDataSetChanged();
        fetchData();
    }

    @Override
    public void onDismiss() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        filter = new Filter();
        articleAdapter = new ArticleAdapter(this);
        newsArticleRecyclerView.setAdapter(articleAdapter);
        int numCols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numCols, VERTICAL);
        newsArticleRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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
                fetchData();
                searchView.clearFocus();
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
                Toast.makeText(MainActivity.this, "Clicked on filter", LENGTH_SHORT).show();
                FilterDialogFragment filterFragment = new FilterDialogFragment();
                filterFragment.show(getSupportFragmentManager(), "adad");
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

    private void fetchData() {
        RetrofitClient.fetchData(searchQuery,
                                 filter,
                                 new Callback<NewsArticleCollection>() {
            @Override
            public void onResponse(Call<NewsArticleCollection> call, Response<NewsArticleCollection> response) {
                Log.e("amodi", response.body() + "");
                articleAdapter.addNewsArticleItems(response.body().getArticles());
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsArticleCollection> call, Throwable t) {
                //Toast.makeText(context, "Error getting data", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting data", t);
            }
        });
    }
}
