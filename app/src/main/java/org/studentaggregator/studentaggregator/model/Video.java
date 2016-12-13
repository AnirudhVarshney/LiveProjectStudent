package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 12-Jun-16.
 */
public class Video {

    private String url;
    private String title;
    private String description;
    private String id;


    public Video() {
        this.url = "";
        this.title = "";
        this.description = "";
        this.id = "";
    }

    public Video(String url, String title, String description, String id) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "url = " + url + " : title = " + title + " : description = " + description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
