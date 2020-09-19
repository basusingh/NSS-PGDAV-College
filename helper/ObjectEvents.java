package com.basusingh.nsspgdav.helper;

/**
 * Created by Basu Singh on 10/8/2016.
 */
public class ObjectEvents {

    String id;
    String heading;
    String description;
    String time_stamp;
    String type;
    String imageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTimeStamp() {
        return time_stamp;
    }

    public void setTimeStamp(String date) {
        this.time_stamp = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
