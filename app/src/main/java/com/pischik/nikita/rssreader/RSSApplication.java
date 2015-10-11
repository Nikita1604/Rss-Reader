package com.pischik.nikita.rssreader;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by nikita1604 on 11.10.15.
 */
public class RSSApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }
}
