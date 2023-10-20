package com.example.selfnotes;

import com.google.firebase.firestore.FieldValue;

// this is model class
public class Note {
    String title;
    String content;
    //Timestamp is firebase class
    FieldValue timestamp;

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue timestamp) {
        this.timestamp = timestamp;
    }
}
