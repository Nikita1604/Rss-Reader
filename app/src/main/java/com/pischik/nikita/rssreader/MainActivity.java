package com.pischik.nikita.rssreader;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
