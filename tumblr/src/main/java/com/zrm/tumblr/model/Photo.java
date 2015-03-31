package com.zrm.tumblr.model;

/**
 * Created by zhangrm on 2015/3/15 0015.
 */
public class Photo{
    private Long id;
    private String name;
    private String url;

    public Photo() {
    }

    public Photo(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Photo(long id, String url,String name) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
