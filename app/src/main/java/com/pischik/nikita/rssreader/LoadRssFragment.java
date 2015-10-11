package com.pischik.nikita.rssreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.SQLException;

/**
 * fragment contain logic and view Loading screen
 */
public class LoadRssFragment extends SherlockFragment {

    /**
     * field contain url rss feed
     */
    private final String urlFeed = "http://news.tut.by/rss/pda/all.rss";

    private static boolean hasConnect(Context context) {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);

        if (hasConnect(getSherlockActivity().getApplicationContext())) {
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
            /**
             * clear database from old RSS news
             */
            DatabaseHelper databaseHelper = OpenHelperManager
                    .getHelper(getSherlockActivity().getApplicationContext(), DatabaseHelper.class);
            try {
                TableUtils.dropTable(databaseHelper.getConnectionSource(), NewsItem.class, false);
                TableUtils.createTable(databaseHelper.getConnectionSource(), NewsItem.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        getSherlockActivity().getSupportActionBar().setTitle("Загрузка новостей");

        /**
         * call method that download and parse RSS feed
         */
        RssDownload.Download(urlFeed,
                getSherlockActivity().getApplicationContext(),
                getSherlockActivity());

        return view;
    }
}
