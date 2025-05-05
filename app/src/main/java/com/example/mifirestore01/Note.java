package com.example.mifirestore01;

import androidx.annotation.NonNull;

public class Note {

    private String id;
    private String idNote;
    private String title;
    private String description;
    private int priority;

    private String imageUrl;
    private String imageKey;

    public Note(){

    }

    public Note(String id, String idNote, String title, String description, int priority, String imageUrl, String imageKey){

        this.id = id;
        this.idNote = idNote;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.imageUrl = imageUrl;
        this.imageKey = imageKey;
    }

    public Note(String id, String idNote, String title, String description, int priority){

        this.id = id;
        this.idNote = idNote;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageKey() {
        return this.imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    @NonNull
    @Override
    public String toString() {
        return id +" "+ idNote +" "+ title +" "+ description +" "+ priority +" "+ imageUrl +" "+ imageKey;
    }
}
