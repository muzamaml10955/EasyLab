package com.wmwebs.easylab.ui.gallery;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wmwebs.easylab.R;

public class GalleryFragment extends Fragment {


    WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        webView = root.findViewById(R.id.chuwebview);
loadwebview();
        return root;
    }

    public void loadwebview() {
        // fruitewebview.clearCache(true);
        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//faster to load
      //  webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
     //   webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
      //  webSettings.setSavePassword(true);
     //   webSettings.setSaveFormData(true);
        webView.loadUrl("https://easylab.pk/chughtai-lab/");


        //  swipeRefreshLayout.setRefreshing(true);


        //back button implimetation
        webView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(getContext(), "Failed loading app!, No Internet Connection found.", Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(WebView view, String url) {

                //Hide the SwipeReefreshLayout


            }

        });


    }
}
