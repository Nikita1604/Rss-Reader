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





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,
                container, false);

        RssDownload.clearDatabase(getSherlockActivity().getApplicationContext());
        getSherlockActivity().getSupportActionBar().setTitle(R.string.loading_news_screen_title);

        /**
         * call method that download and parse RSS feed
         */
        RssDownload.Download(true, getSherlockActivity().getApplicationContext(),
                getSherlockActivity());

        return view;
    }
}
