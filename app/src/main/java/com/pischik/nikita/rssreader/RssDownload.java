package com.pischik.nikita.rssreader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * class contain method download and parse RSS feed
 */

public class RssDownload{

    private static XmlPullParserFactory xmlFactoryObject;

    /**
     * field contain url rss feed
     */
    private static final String urlFeed = "http://news.tut.by/rss/pda/all.rss";

    /**
     * method that parse xml rss feed
     */
    private static void parseXml(XmlPullParser myParser, Context context) {
        /**
         * field "event" contain type of XML part
         * such as: start tag, end tag or text between tags
         */

        int event;
        String text = null;
        String title = "title";
        String link = "link";
        String imageUrl = "description";
        String pubDate = "pubDate";

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        Dao<NewsItem, Integer> newsDao = databaseHelper.getNewsItemsDao();

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "title":
                                title = text;
                                break;
                            case "link":
                                link = text;
                                break;
                            case "description":
                                imageUrl = text;
                                break;
                            case "pubDate":
                                pubDate = text;
                                break;
                            case "item":
                                NewsItem newsItem = new NewsItem();
                                newsItem.setTitle(title);
                                newsItem.setPubDate(parsePubDate(pubDate));
                                newsItem.setFullNewsUrl(link);
                                newsItem.setImageUrl(parseDescription(imageUrl));
                                newsDao.create(newsItem);
                                break;
                        }
                        break;
                }
                //get next part of xml file
                event = myParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that parse <description> tag and return image url
     */
    private static String parseDescription(String text) {
        if (!text.contains("img")) {
            return "no image";
        }

        int index = text.indexOf("img");
        String newDescription = text.substring(index);
        index = newDescription.indexOf('"');
        newDescription = newDescription.substring(index + 1);
        index = newDescription.indexOf('"');
        return newDescription.substring(0,index);
    }

    public  static void clearDatabase(Context context) {
        if (RssDownload.hasConnect(context)) {
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
            /**
             * clear database from old RSS news
             */
            DatabaseHelper databaseHelper = OpenHelperManager
                    .getHelper(context, DatabaseHelper.class);
            try {
                TableUtils.dropTable(databaseHelper.getConnectionSource(), NewsItem.class, false);
                TableUtils.createTable(databaseHelper.getConnectionSource(), NewsItem.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * method checked Internet connection
     */
    public static boolean hasConnect(Context context) {

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

    /**
     * method that represent publishing date in new format
     */
    private static String parsePubDate(String pubDate) {
        String day = pubDate.substring(5,7);
        String month = pubDate.substring(8,11);
        String year = pubDate.substring(12,16);
        String time = pubDate.substring(17,22);
        switch (month) {
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
        }
        return (day + "." + month + "." + year + ", " + time);
    }

    /**
     * method that use AsyncTask to Download XML file RSS feed
     */
    public static void Download(final boolean isFirst, final Context context,
                                final SherlockFragmentActivity activity) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String urlStr = urlFeed;
                InputStream is = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setReadTimeout(10 * 1000);
                    connection.setConnectTimeout(10 * 1000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    is = connection.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlFactoryObject.newPullParser();

                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(is, null);

                    parseXml(parser, context);

                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (isFirst) {
                    ((NewsActivity)activity).onDownloadAndParseFinished();
                } else {
                    ((NewsActivity)activity).updateListNews();
                }

            }
        };
        asyncTask.execute();
    }


}
