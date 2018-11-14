package com.apiconnection.diegoarmando.api_connection.Business;

public class News {
    private String title;
    private String description;
    private String date;
    private boolean approved;

    public News(String title, String description, String date, boolean approved) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.approved = approved;
    }

    public News() {
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

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
