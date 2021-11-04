package com.example.morpion.model;

import android.app.Application;
import android.os.AsyncTask;

import com.example.morpion.db.DatabaseClient;

import java.util.HashMap;

public class ApplicationMorpion extends Application {
    private Partie game;

    public ApplicationMorpion(){

        super();
        setGame(null);
    }

    public Partie getGame() {
        return game;
    }

    public void setGame(Partie game) {
        this.game = game;
    }

}
