package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.SQLException;
import java.util.List;


/**
 * fragment contain logic and view List News screen
 */

public class ListNewsFragment extends SherlockFragment {

    List<NewsItem> news;
    ImageLoader imageLoader;
    DisplayImageOptions displayImageOptions;
    DatabaseHelper databaseHelper;
    Dao<NewsItem, Integer> newsDao;
    ListView listView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_news_fragment,
                container, false);

        listView = (ListView) view.findViewById(R.id.list_news);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarListNews);
        progressBar.setVisibility(View.INVISIBLE);
        /**
         * initialization imageLoader to use Universal Image Loader
         */
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        imageLoader = ImageLoader.getInstance();
        getSherlockActivity().getSupportActionBar().setTitle(R.string.list_news_screen_title);

        databaseHelper = OpenHelperManager.getHelper(
                getActivity().getApplicationContext(),
                DatabaseHelper.class);

        newsDao = databaseHelper.getNewsItemsDao();



        try {
            news = newsDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ArrayAdapter<NewsItem> adapter = new NewsArrayAdapter(getActivity(), news, imageLoader,
                displayImageOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((NewsActivity) getSherlockActivity())
                        .onNewsListItemClick(
                                ((NewsItem)listView.getItemAtPosition(position)).getFullNewsUrl());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add("Refresh")
                .setIcon(R.drawable.ic_cached_black_24dp)
                .setOnMenuItemClickListener(this.refreshButtonClickListener)
                .setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener refreshButtonClickListener =
            new com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
            listView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            RssDownload.clearDatabase(getSherlockActivity().getApplicationContext());
            RssDownload.Download(false, getSherlockActivity().getApplicationContext(),
                    getSherlockActivity());
            return false;
        }
    };

    public void updateListNews() {

        newsDao = databaseHelper.getNewsItemsDao();
        try {
            news = newsDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<NewsItem> adapter = new NewsArrayAdapter(getActivity(), news, imageLoader,
                displayImageOptions);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }
}
