package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import java.util.ArrayList;

public class MainActivity extends SherlockActivity {

    //test field. must be deleted
    private ArrayList<RSSNewsModel> rssNewsList;

    //field contain url rss feed
    private final String urlFeed = "http://news.tut.by/rss/pda/all.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // call method download and parse rss feed
        rssNewsList = RssDownload.Download(urlFeed,(TextView) findViewById(R.id.hello_tv));
    }


    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
