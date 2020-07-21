package com.example.android.news;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.news.MainActivity.LOG_TAG;

/**
 * Created by HP on 6/19/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {



    private String mUrl;

    public NewsLoader(Context context, String newsRequestUrl) {

        super(context);
        mUrl = newsRequestUrl;
    }

    @Override
    protected void onStartLoading() {
        //Log.v("EarthquakeActivity.this","TEST: onStartLoading() called");

        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {

        // Create URL object
        URL url = createUrl(mUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<NewsData> arraylistOfnews = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}

        return arraylistOfnews;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{

                Log.e(LOG_TAG,"Error response code : " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG,"Error loading Json Results");

        } finally {
            if (urlConnection != null) {
            }
            if (inputStream != null) {
                urlConnection.disconnect();
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link NewsData} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private ArrayList<NewsData> extractFeatureFromJson(String newsJSON) {

        ArrayList<NewsData> arrayOfNews = new ArrayList<>();

        if(TextUtils.isEmpty(newsJSON))
            return null;


        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            String sourceOfNews = baseJsonResponse.getString("source");

            JSONArray articlesArray = baseJsonResponse.optJSONArray("articles");

                 for(int i = 0 ; i < articlesArray.length() ; i++)
            {



                // If there are results in the features array


                // Extract out the first feature (which is an earthquake)
                JSONObject firstArticle = articlesArray.getJSONObject(i);

                // Extract out the title, time, and tsunami values
                String title = firstArticle.getString("title");
                String desc = firstArticle.getString("description");
                String urlNews = firstArticle.getString("url");
                String imageUrl = firstArticle.getString("urlToImage");
                String time = firstArticle.getString("publishedAt");
                NewsData singleNews = new NewsData(sourceOfNews, title, desc, urlNews, imageUrl, time);
                arrayOfNews.add(singleNews);
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);

        }
        return arrayOfNews;
    }
}
