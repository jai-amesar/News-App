package com.example.android.news;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 6/19/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(MainActivity context, ArrayList<NewsData> allNews) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, allNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main, parent, false);
        }


        final NewsData currentNews = getItem(position);

        ImageView news_img = (ImageView) listItemView.findViewById(R.id.imageNews);
        loadImageFromUrl(currentNews.newsImageUrl, news_img);

        final TextView news_header = (TextView) listItemView.findViewById(R.id.header);
        news_header.setText(currentNews.title);

        TextView news_des = (TextView) listItemView.findViewById(R.id.para);
        news_des.setText(currentNews.desc);

        TextView urlOfNews = (TextView) listItemView.findViewById(R.id.newsUrl);

        //to show the textview as link

        urlOfNews.setText(
                Html.fromHtml(
                        "<a href=\"" + currentNews.newsUrl + "\">" + currentNews.newsSource.toUpperCase() + "</a>"));
        urlOfNews.setLinkTextColor(Color.BLUE);

        urlOfNews.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView share_option = (ImageView) listItemView.findViewById(R.id.share_button);
        share_option.setImageResource(R.drawable.ic_action_name);
        share_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, currentNews.newsUrl);
                getContext().startActivity(sharingIntent);

            }
        });

         return listItemView;
    }

    /** to show image through url
     **/
    private void loadImageFromUrl(String urlofImage,ImageView news_img){

        Picasso.with(getContext()).load(urlofImage).into(news_img);
    }

}