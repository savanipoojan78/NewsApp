
package com.Poojan.News;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    /** Query URL */
    private String mUrl;
    public static boolean mPrefThumbnail;
    List<NewsArticle> newsArticles = null;

    /**
     * Constructs a new {@link NewsArticleLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     * @param prefThumbnail to load data from
     */
    public NewsArticleLoader(Context context, String url, Boolean prefThumbnail) {
        super(context);
        mUrl = url;
        mPrefThumbnail = prefThumbnail;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsArticle> loadInBackground() {


        if (mUrl == null) {
            return null;
        }
        if(mUrl.equals("Nirma News"))
        {
            newsArticles =NewsQueryUtils.fetchArticleDatafromfirebase();
        }
        else{
            newsArticles = NewsQueryUtils.fetchArticleData(mUrl);

        }

        // Perform the network request, parse the response, and extract a list of newsArticles.
        return newsArticles;
    }
}