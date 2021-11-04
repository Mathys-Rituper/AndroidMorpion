package com.example.morpion.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName="users",
indices={@Index(value={"name"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = false) @NonNull
    private String name;

    @ColumnInfo(name="image")
    private String base64image;

    @ColumnInfo
    private long victories;

    @ColumnInfo
    private long defeats;

    @ColumnInfo
    private long ties;

    @Ignore
    public User(String name, String base64image) {
        this.setName(name);
        this.setBase64image(base64image);
        this.setVictories(0);
        this.setDefeats(0);
        this.setTies(0);
    }

    public User(String name, String base64image, long victories, long defeats, long ties){
        this.name = name;
        this.base64image=base64image;
        this.victories=victories;
        this.defeats=defeats;
        this.ties=ties;
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

    @Override
    public String toString() {
        return getName() + " ("+getVictories()+"/"+getTies()+"/"+getDefeats()+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
