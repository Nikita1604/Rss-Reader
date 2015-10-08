package com.pischik.nikita.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * class that get access to database, create database or update it
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "news.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<NewsItem, Integer> newsItemsDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NewsItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, NewsItem.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * method that return Database Access Object
     */

    public Dao<NewsItem, Integer> getNewsItemsDao() {
        if (newsItemsDao == null) {
            try {
                newsItemsDao = getDao(NewsItem.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return newsItemsDao;
    }
}
