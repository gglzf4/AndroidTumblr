package com.zrm.tumblr.model;

/**
 * Created by zhangrm on 2015/3/15 0015.
 */
public class Photo{
    private long id;
    private String name;
    private String url;

    public Photo() {
    }

    public Photo(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Photo(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
