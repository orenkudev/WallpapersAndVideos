
package com.gcoders.wallpaper.hqwallpapersdaily.model.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediumVideo {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("height")
    @Expose
    private Integer height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediumVideo withUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public MediumVideo withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public MediumVideo withSize(Integer size) {
        this.size = size;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public MediumVideo withHeight(Integer height) {
        this.height = height;
        return this;
    }

}
