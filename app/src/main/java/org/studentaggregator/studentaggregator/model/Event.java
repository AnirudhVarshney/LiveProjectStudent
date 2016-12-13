package org.studentaggregator.studentaggregator.model;

/**
 * Created by ABHINAV on 20-05-2016.
 */
    /*    id    title    description    date    images    */
public class Event {

    private String id;
    private String title;
    private String description;
    private String date;
    private String images;

    public Event() {
        id = "";
        title = "";
        description = "";
        date = "";
        images = "";
    }

    public Event(String id, String title, String description, String date, String images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String datetime) {
        this.date = datetime;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "id = " + id + " : title = " + title + " : description = " + description + " : datetime = " + date + " : images = " + images;
    }
}
