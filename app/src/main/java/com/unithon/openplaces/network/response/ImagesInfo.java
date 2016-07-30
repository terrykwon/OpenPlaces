package com.unithon.openplaces.network.response;

/**
 * Created by choi on 2016-07-31.
 */
public class ImagesInfo {
    private String title;
    private String link;
    private String thumbnail;
    private long sizeheight;
    private long sizewidth;

    /*public ImagesInfo(String title, String link, String thumbnail, long sizeheight, long sizewidth) {
        this.title = title;
        this.link = link;
        this.thumbnail = thumbnail;
        this.sizeheight = sizeheight;
        this.sizewidth = sizewidth;
    }*/

    public String getTitle() {
        return title;
    }
    public String getLink() {
        return link;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public long getSizeheight() {
        return sizeheight;
    }
    public long getSizewidth() {
        return sizewidth;
    }
}
