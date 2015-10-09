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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            listNewsFragment = new ListNewsFragment();
            fragmentTransaction.add(R.id.container, listNewsFragment);
            fragmentTransaction.commit();
        }
    }


}
