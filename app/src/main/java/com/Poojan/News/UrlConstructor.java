package com.Poojan.News;

import android.support.annotation.Nullable;
import android.util.Log;




public final class UrlConstructor {

    // Tag for the log messages
    private static final String LOG_TAG = "UrlConstructor";

    // URL Base
    private static final String URL_BASE = "https://newsapi.org/v2/top-headlines?country=in";

    /**
     * API Key Value which you need to store in your gradle.properties file as:
     * GoodNewsFirst_GuardianApp_ApiKey="YOUR-API-KEY-HERE"
     */
    private static final String apiKey = BuildConfig.ApiKey;
    private static final String URL_API_KEY = "&apiKey=" + apiKey;

    /**
     * Returns a Guardian API URL string from all the components
     * @param section section or tag in Guardian
     * @return URL string
     */
    public static String constructUrl(@Nullable String section) {

        // Start the StringBuilder and add the URL Base to the beginning
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL_BASE);

        // If the section isn't null then add that
        if (section != null) {
            stringBuilder.append(section);
        } else {
            stringBuilder.append(MyApplication.getAppContext().getResources().getString(R.string.pref_topic_0_label_value));
            stringBuilder.append("&pageSize=50");
        }

//        // If the orderBy isn't null then add that
//        if (orderBy != null) {
//            stringBuilder.append("&order-by="
//                    + orderBy);
//        } else {
//            // order by newest articles by default if no preference set
//            stringBuilder.append("&order-by="
//                    + MyApplication.getAppContext().getResources().getString(R.string.pref_order_by_default));
//        }

        // Add the API Key to the end of the query
        stringBuilder.append(URL_API_KEY);

        // LOG the API URL
        Log.e(LOG_TAG, "API GUARDIAN_REQUEST_URL: " + stringBuilder.toString());

        return stringBuilder.toString();
    }
}
