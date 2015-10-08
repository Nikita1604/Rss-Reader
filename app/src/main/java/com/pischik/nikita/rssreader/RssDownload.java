package com.pischik.nikita.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

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
                                newsItem.setPubDate(pubDate);
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
        int index = text.indexOf('"');
        String newDescription = text.substring(index+1);
        index = newDescription.indexOf('"');
        return newDescription.substring(0,index);
    }

    /**
     * method that use AsyncTask to Download and Parse XML RSS feed
     */

    public static void Download(final String url, final Context context) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String urlStr = url;
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
            }
        };
        asyncTask.execute();
    }


}
