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


    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImages(List<ImagesInfo> images) {
        this.images = images;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setMapX(long mapX) {
        this.mapX = mapX;
    }

    public void setMapY(long mapY) {
        this.mapY = mapY;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCloseAt(long closeAt) {
        this.closeAt = closeAt;
    }

    public void setOpenAt(long openAt) {
        this.openAt = openAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public String getTel() {
        return tel;
    }
    public String getAddress() {
        return address;
    }
    public List<ImagesInfo> getImages() {
        return images;
    }
    public String getStatus() {
        return status;
    }
    public String getLink() {
        return link;
    }
    public long getMapX() {
        return mapX;
    }
    public long getMapY() {
        return mapY;
    }
    public double getLat(){
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public String getCategory() {
        return category;
    }
    public long getCloseAt() {
        return closeAt;
    }
    public long getOpenAt() {
        return openAt;
    }
    public String getDescription() {
        return description;
    }
}
