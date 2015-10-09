package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.SQLException;
import java.util.List;


public class ListNewsFragment extends SherlockFragment {

    List<NewsItem> news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_news_fragment,
                container, false);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getSherlockActivity()));

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(
                getActivity().getApplicationContext(),
                DatabaseHelper.class);

        Dao<NewsItem, Integer> newsDao = databaseHelper.getNewsItemsDao();

        getSherlockActivity().getSupportActionBar().setTitle("Список новостей");

        try {
            news = newsDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) view.findViewById(R.id.list_news);
        ArrayAdapter<NewsItem> adapter = new NewsArrayAdapter(getActivity(), news, imageLoader);
        listView.setAdapter(adapter);

        return view;
    }
}
