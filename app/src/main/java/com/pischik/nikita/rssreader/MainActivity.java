package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends SherlockActivity {


    /**
     * field contain url rss feed
     */
    private final String urlFeed = "http://news.tut.by/rss/pda/all.rss";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * clearing database from old RSS news
         */
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            TableUtils.dropTable(databaseHelper.getConnectionSource(), NewsItem.class, false);
            TableUtils.createTable(databaseHelper.getConnectionSource(), NewsItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /**
         * call method that download and parse rss feed
         */
        RssDownload.Download(urlFeed, this);
    }


    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
