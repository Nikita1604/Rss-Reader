package com.pischik.nikita.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * fragment contain logic and view Full News screen
 */

public class FullNewsFragment extends SherlockFragment {

    private WebView webView;
    private TextView lostConnectionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_news_fragment,
                container, false);

        getSherlockActivity().getSupportActionBar().setTitle(R.string.full_news_screen_title);
        webView = (WebView) view.findViewById(R.id.webView);
        lostConnectionTextView = (TextView) view.findViewById(R.id.lostConnectionTV);
        if (RssDownload.hasConnect(getActivity().getApplicationContext())) {
            lostConnectionTextView.setVisibility(View.INVISIBLE);
            Bundle bundle = getArguments();
            if (bundle != null) {
                webView.loadUrl(bundle.getString("URL"));
            }
        } else {
            lostConnectionTextView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
        }

        return view;
    }


}
