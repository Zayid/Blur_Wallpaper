package com.whackyard.whatsappwalls;

/**
 * Created by Nazila on 23/10/2016.
 */

public class Walls {

    private String link;
    private String title;

    public Walls(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public Walls(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }




}
