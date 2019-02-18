
package com.Poojan.News;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An {@link NewsArticleAdapter} knows how to create a list item layout for each article
 * in the data source (a list of {@link NewsArticle} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {
    private Context context;

    /**
     * Constructs a new {@link NewsArticleAdapter}.
     *
     * @param context      of the app
     * @param newsArticles is the list of newsArticles, which is the data source of the adapter
     */
    public NewsArticleAdapter(@NonNull Context context, @NonNull List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
        this.context = context;
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        NewsArticle currentNewsArticle = getItem(position);

        // Get and display the article's Section
        String newsSection = currentNewsArticle.getSectionName();
        TextView sectionNameView = listItemView.findViewById(R.id.article_section);
        sectionNameView.setText(newsSection);

        // Get and display the article's Title
        String newsTitle = currentNewsArticle.getTitle();
        TextView titleView = listItemView.findViewById(R.id.article_title);
        titleView.setText(newsTitle);

        // Get and display the article's Trail Text
        String newsTrail = currentNewsArticle.getTrailText();
        TextView trailView = listItemView.findViewById(R.id.article_trailtext);
        // Display the trailtext for the current article in that TextView or hide it if null
        if (newsTrail != null && !newsTrail.isEmpty()) {
            newsTrail = newsTrail + ".";
            trailView.setText(newsTrail);
        } else {
            trailView.setVisibility(View.GONE);
        }

        // Create a new Date object from the time in milliseconds of the article
        // Format the article_date string (i.e. "Mar 3, '18")
        String formattedDate = formatDate(currentNewsArticle.getPublishedDate());
        // Find and display the article's Date
        TextView dateView = listItemView.findViewById(R.id.article_date);
        dateView.setText(formattedDate);

        // Format the time string (i.e. "4:30 PM")
        String formattedTime = formatTime(currentNewsArticle.getPublishedDate());
        // Find and display the article's Time
        TextView timeView = listItemView.findViewById(R.id.article_time);
        timeView.setText(formattedTime);

        // Get and display the article's Author
        String newsAuthor = currentNewsArticle.getAuthor();
        String name="Poijan Savani";
        TextView authorView = listItemView.findViewById(R.id.article_author);
        authorView.setText(newsAuthor);
        Log.e("Author:---",newsAuthor);
        ImageView photoView = listItemView.findViewById(R.id.article_image);
        titleView.setMaxLines(3);
        titleView.setMinLines(3);
        ConstraintLayout constraintLayout = listItemView.findViewById(R.id.newslist_constraint_layout);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.setDimensionRatio(photoView.getId(), "16:9");
        set.applyTo(constraintLayout);
        set.clear(R.id.article_title, ConstraintSet.START);
        // Then attach a new constraint connection.
        set.connect(R.id.article_title, ConstraintSet.END, R.id.article_image, ConstraintSet.END, 0);
        set.applyTo(constraintLayout);

        // Find and display the article's Thumbnail
        Picasso.with(context).load(currentNewsArticle.getThumbnail()).placeholder(R.drawable.news_placeholder).
                error(R.drawable.news_placeholder)
                .into(photoView);

        Animation animation=AnimationUtils.loadAnimation(context, R.anim.abc_grow_fade_in_from_bottom);
        listItemView.startAnimation(animation);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return a formatted time string (i.e. "Mar 3, '18") from a Date object.
     */
    private String formatDate(String date) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        Date date_out = null;
        try {
            date_out = inputParser.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd ''yy", Locale.US);
        return outputFormatter.format(date_out);
    }

    /**
     * Return a formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(String date) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        Date date_out = null;
        try {
            date_out = inputParser.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        return outputFormatter.format(date_out);
    }
}