package com.example.morpion.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class User {

    @PrimaryKey(autoGenerate = false)
    private String name;

    @ColumnInfo(name="image")
    private String base64image;

    @ColumnInfo
    private long victories;

    @ColumnInfo
    private long defeats;

    @ColumnInfo
    private long ties;

    public User(String name, String base64image) {
        this.setName(name);
        this.setBase64image(base64image);
        this.setVictories(0);
        this.setDefeats(0);
        this.setTies(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public long getVictories() {
        return victories;
    }

    public void setVictories(long victories) {
        this.victories = victories;
    }

    public long getDefeats() {
        return defeats;
    }

    public void setDefeats(long defeats) {
        this.defeats = defeats;
    }

    public long getTies() {
        return ties;
    }

    public void setTies(long ties) {
        this.ties = ties;
    }
}
