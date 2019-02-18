
package com.Poojan.News;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsArticle>>,NavigationView.OnNavigationItemSelectedListener {

    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;
    /**
     * ListView that holds the articles
     **/
    private ListView articleListView;

    /**
     * Adapter for the list of articles
     */
    private NewsArticleAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    String SECTION_CHOICE;
    ImageView nevigationholder;
    /**
     * Swipe to reload spinner that is displayed while data is being downloaded
     */
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nevigation);
        SECTION_CHOICE=getString(R.string.pref_topic_6_label_value);



        // Find the menu toolbar for app compat
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToolbar.setNavigationIcon(R.drawable.bookmark2);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Hide the default title to use the designed one instead
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);
        nevigationholder=(ImageView)findViewById(R.id.imageView);

        // Find a reference to the {@link ListView} in the layout
        articleListView = findViewById(R.id.list);

        // Find the feedback_view that is only visible when the list has no items
        mEmptyStateTextView = findViewById(R.id.feedback_view);
        articleListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText("");


        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                NewsArticle currentNewsArticle = mAdapter.getItem(position);
                String s=currentNewsArticle.getUrl();
                Intent i=new Intent(MainActivity.this,Webview.class);
               i.putExtra("url",s);
               startActivity(i);

            }
        });

        // If there is a network connection, fetch data
       loadData();

        // Setup refresh listener which triggers new data loading
        refresh();

    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        // Get User Preferences or Defaults from Settings
        //String ORDER_BY = getPreferenceStringValue(R.string.pref_order_by_key, R.string.pref_order_by_default);
        boolean PREF_THUMBNAIL = getPreferenceBooleanValue(R.string.pref_thumbnail_key, R.bool.pref_thumbnail_default);

        // Change the Subtitle to Section Choice
        TextView SectionTitle = findViewById(R.id.toolbar_subtitle);
        String GUARDIAN_SECTION;
        SectionTitle.setText(HashMapper.urlToLabel(SECTION_CHOICE));
        if(SECTION_CHOICE.equals("Nirma News"))
        {
             GUARDIAN_SECTION=SECTION_CHOICE;
        }
        else{
            GUARDIAN_SECTION = UrlConstructor.constructUrl(SECTION_CHOICE);
        }
        // Construct the API URL to query the Guardian Dataset

        // Create a new loader for the given URL

        return new NewsArticleLoader(this, GUARDIAN_SECTION, PREF_THUMBNAIL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
        // Hide swipe to reload spinner
        swipeContainer.setRefreshing(false);

        // Set empty state text to display "No news articles found."
        mEmptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous article data
        mAdapter.clear();

        // If there is a valid list of newsFeed, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        } else {
            if (NewsQueryUtils.isConnected(getBaseContext())) {
                loadData();
                // Set empty state text to display "No articles found."
                mEmptyStateTextView.setText(R.string.no_articles);
            } else {
                // Update toast with no connection error message
                Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /**
     * Loads and reloads the data as requested
     */
    public void loadData() {
        refresh();
        // If there is a network connection, fetch data
        if (NewsQueryUtils.isConnected(getBaseContext())) {
            // destroy old loader to get new info
            getLoaderManager().destroyLoader(1);
            // Clear any potential messages on screen
            mEmptyStateTextView.setText("");
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            swipeContainer.setRefreshing(false);

            // Update toast with no connection error message
            Toast.makeText(MyApplication.getAppContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A helper method to extract current preference boolean value
     *
     * @param key          preference's key
     * @param defaultValue preference's default value
     * @return preference  current value
     */
    public boolean getPreferenceBooleanValue(int key, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(
                getString(key),
                getResources().getBoolean(defaultValue)
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_business) {

            SECTION_CHOICE=getString(R.string.pref_topic_0_label_value);
            // Handle the camera action
        } else if (id == R.id.nav_entertinment) {
            SECTION_CHOICE=getString(R.string.pref_topic_1_label_value);

        } else if (id == R.id.nav_general) {
            SECTION_CHOICE=getString(R.string.pref_topic_2_label_value);

        } else if (id == R.id.nav_health) {
            SECTION_CHOICE=getString(R.string.pref_topic_3_label_value);

        } else if (id == R.id.nav_science) {
            SECTION_CHOICE=getString(R.string.pref_topic_4_label_value);

        } else if (id == R.id.nav_sports) {
            SECTION_CHOICE=getString(R.string.pref_topic_5_label_value);

        }
        else if (id == R.id.nav_technology) {
            SECTION_CHOICE=getString(R.string.pref_topic_6_label_value);

        }
        else if (id == R.id.nav_nirma) {
            SECTION_CHOICE=getString(R.string.pref_topic_7_label_value);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        swipeContainer.setRefreshing(true);
        loadData();
        return true;
    }
    private void refresh()
    {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Check for internet connection and attempt to load data
                swipeContainer.setRefreshing(true);
                loadData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                R.color.primaryHilight,
                R.color.secondaryHilight);
    }
}