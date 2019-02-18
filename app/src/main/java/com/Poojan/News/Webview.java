package com.Poojan.News;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class Webview extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String load;
        setContentView(R.layout.activity_webview);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.cordinatorLayout);
        WebView webView=(WebView)findViewById(R.id.load);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Bundle extras = getIntent().getExtras();
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        boolean hasConnect = (i!= null && i.isConnected() && i.isAvailable());

        if(hasConnect)
        {
            // show the webview
            if(extras == null) {
                load= null;
            } else {
                load= extras.getString("url");
            }
//            webView.clearCache(true);
//            webView.clearHistory();
            webView.loadUrl(load);


        }
        else
        {
            // do what ever you need when when no internet connection
            Snackbar snackbar = Snackbar.make(constraintLayout, "Check Your Internet Connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }
}
