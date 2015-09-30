package com.daviddetena.everpobre.model;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by daviddetena on 28/09/15.
 */
public class Note {

    private long id;
    private String text;
    private Date creationDate;
    private Date modificationDate;
    private String photoUrl;
    private WeakReference<Notebook> notebook;   // The n in 1-n relationship puts its relation as weak
    private double longitude;
    private double latitude;
    private boolean hasCoordinates;
    private String address;


    public Note(Notebook notebook, String text){
        // Reference as a copy
        this.notebook = new WeakReference<Notebook>(notebook);
        this.text = text;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isHasCoordinates() {
        return hasCoordinates;
    }

    public void setHasCoordinates(boolean hasCoordinates) {
        this.hasCoordinates = hasCoordinates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Notebook getNotebook() {
        return notebook.get();
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = new WeakReference<Notebook>(notebook);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
