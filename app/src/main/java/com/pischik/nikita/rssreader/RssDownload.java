package com.pischik.nikita.rssreader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/* class contain method download and parse RSS feed*/

public class RssDownload{

    public static ArrayList<RSSNewsModel> listNews = new ArrayList<>();
    private static XmlPullParserFactory xmlFactoryObject;

    //method that parse xml rss feed
    private static void parseXml(XmlPullParser myParser) {
        //field "event" contain type of XML part
        // such as: start tag, end tag or text between tags
        int event;
        String text = null;
        String title = "title";
        String link = "link";
        String description = "description";
        String pubDate = "pubDate";
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
                                description = text;
                                break;
                            case "pubDate":
                                pubDate = text;
                                break;
                            case "item":
                                listNews.add(new RSSNewsModel(title, pubDate, description, link));
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
        }
    }


    //method that use AsyncTask to Download and Parse XML RSS feed
    public static ArrayList<RSSNewsModel> Download(final String url, final TextView tv) {
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

                    parseXml(parser);

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
        return listNews;
    }
}
