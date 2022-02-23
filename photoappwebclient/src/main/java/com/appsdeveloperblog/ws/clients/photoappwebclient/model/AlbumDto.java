package com.appsdeveloperblog.ws.clients.photoappwebclient.model;

public class AlbumDto {
    private String userId;
    private String id;
    private String title;
    private String description;
    private String url;

    public AlbumDto(String userId, String id, String title, String description, String url) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public AlbumDto() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
