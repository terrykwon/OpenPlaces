package com.unithon.openplaces.network.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-07-30.
 */
public class SearchResponse {
    // titles
    public String title;
    private String tel;
    private String address;
    private List<ImagesInfo> images = new ArrayList<>();
    private String status;
    private String link;
    private long mapX;
    private long mapY;
    // map
    public double lat;
    public double lng;

    private String category;
    private long closeAt;
    private long openAt;
    private String description;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ImagesInfo> getImages() {
        return images;
    }

    public void setImages(List<ImagesInfo> images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getMapX() {
        return mapX;
    }

    public void setMapX(long mapX) {
        this.mapX = mapX;
    }

    public long getMapY() {
        return mapY;
    }

    public void setMapY(long mapY) {
        this.mapY = mapY;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(long closeAt) {
        this.closeAt = closeAt;
    }

    public long getOpenAt() {
        return openAt;
    }

    public void setOpenAt(long openAt) {
        this.openAt = openAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
