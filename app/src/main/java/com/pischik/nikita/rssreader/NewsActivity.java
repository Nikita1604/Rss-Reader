package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class NewsActivity extends SherlockFragmentActivity {

    private FragmentManager fragmentManager;
    private ListNewsFragment listNewsFragment;
    private FullNewsFragment fullNewsFragment;
    private LoadRssFragment loadRssFragment;

    private boolean isFullNewsFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        fragmentManager = getSupportFragmentManager();
        listNewsFragment = new ListNewsFragment();
        fullNewsFragment = new FullNewsFragment();
        loadRssFragment = new LoadRssFragment();
        if (savedInstanceState == null) {
            /**
             * launch Welcome screen
             */
            isFullNewsFragment = false;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, loadRssFragment);
            fragmentTransaction.commit();
        } else {
            /**
             * launch List News screen
             */
            isFullNewsFragment = false;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, listNewsFragment);
            fragmentTransaction.commit();
        }
    }


    /**
     * launch Full News screen when clicked list item
     */
    public void onNewsListItemClick(String url) {
        isFullNewsFragment = true;
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        fullNewsFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fullNewsFragment);
        fragmentTransaction.commit();
    }

    /**
     * method that called after RSS feed downloaded and parsed
     * launch List News screen
     */
    public void onDownloadAndParseFinished() {
        isFullNewsFragment = false;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, listNewsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        /**
         * Change logic Back Button pressed
         */
        if (isFullNewsFragment) {
            isFullNewsFragment = false;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, listNewsFragment);
            fragmentTransaction.commit();
        } else {
            super.onBackPressed();
        }
    }
}
