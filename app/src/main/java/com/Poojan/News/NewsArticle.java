
package com.Poojan.News;

import android.graphics.Bitmap;

/**
 * An {@link NewsArticle} object contains information related to a single article.
 */
public class NewsArticle {

    private String webSectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webTrailText;
    private String webUrl;
    private String byLine;
    private String thumbnail;


    /**
     * Constructs a new {@link NewsArticle} object
     *
     * @param webSectionName     Section for the article
     * @param webPublicationDate Publication date for the article
     * @param webTitle           Title of the article
     * @param webTrailText       TrailText of the article
     * @param webUrl             Url of the article
     * @param byLine             Author of the article
     * @param thumbnail          Url to the thumbnail of the article
     */
    public NewsArticle(String webSectionName, String webPublicationDate, String webTitle,
                       String webTrailText, String webUrl, String byLine,String thumbnail) {
        this.webSectionName = webSectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle; //headLine
        this.webTrailText = webTrailText;
        this.webUrl = webUrl;
        this.byLine = byLine;
        this.thumbnail = thumbnail;
    }

    public NewsArticle(){}

    public String getSectionName() {
        return webSectionName;
    }

    public String getPublishedDate() {
        return webPublicationDate;
    }

    public String getTitle() {
        return webTitle;
    }

    public String getTrailText() {
        return webTrailText;
    }

    public String getUrl() {
        return webUrl;
    }

    public String getAuthor() {
        return byLine;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}