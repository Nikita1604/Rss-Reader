package com.pischik.nikita.rssreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * adapter for custom ListView
 */
public class NewsArrayAdapter extends ArrayAdapter<NewsItem> {

    private final List<NewsItem> list;
    private final Activity context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions displayImageOptions;

    public NewsArrayAdapter(Activity context, List<NewsItem> list, ImageLoader imageLoader,
                            DisplayImageOptions displayImageOptions) {
        super(context, R.layout.news_list_item_layout, list);
        this.context = context;
        this.list = list;
        this.imageLoader = imageLoader;
        this.displayImageOptions = displayImageOptions;
    }

    static class ViewHolder {
        protected TextView title;
        protected TextView pubDate;
        protected ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.news_list_item_layout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title_news);
            viewHolder.pubDate = (TextView) view.findViewById(R.id.pub_date);
            viewHolder.image = (ImageView) view.findViewById(R.id.icon_news);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(list.get(position).getTitle());
        holder.pubDate.setText(list.get(position).getPubDate());
        if (!list.get(position).getImageUrl().equals("no image"))
        imageLoader.displayImage(list.get(position).getImageUrl(), holder.image,
                displayImageOptions);
        return view;
    }
}
