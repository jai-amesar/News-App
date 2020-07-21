package com.example.android.news;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int News_LOADER_ID = 1;

    NewsAdapter  adapter_list;

    /**
     * URL to query the USGS dataset for earthquake information
     */
    private static final String NEWS_REQUEST_URL =
            " https://newsapi.org/v1/articles?source=the-times-of-india&apiKey=f1c1303d80374d749566f210985ab8ca";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(News_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {

        return new NewsLoader(this,NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> data) {

        if(data != null) {

            ArrayList allNews = new ArrayList(data);
            // Find a reference to the {@link ListView} in the layout
            // ListView earthquakeListView = (ListView) findViewById(R.id.list);
            ListView allnewsListView = (ListView) findViewById(R.id.News_item_latest);

            // Create a new {@link ArrayAdapter} of earthquakes

            adapter_list = new NewsAdapter(MainActivity.this, allNews);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            allnewsListView.setAdapter(adapter_list);


        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {

        // Loader reset, so we can clear out our existing data.
        // Log.v("EarthquakeActivity.this","TEST: onLoadReset() called");

        adapter_list.clear();

    }
}



