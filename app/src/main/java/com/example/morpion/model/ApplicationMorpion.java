package com.example.morpion.model;

import android.app.Application;

import java.util.HashMap;

public class ApplicationMorpion extends Application {
    private HashMap<String, User> players;
    private Partie game;

    public ApplicationMorpion(){
        super();
        players = new HashMap<>();
        setGame(null);
        User toto = new User("toto","");
        User tata = new User("tata","");
        addPlayer(toto);
        addPlayer(tata);
    }

    public HashMap<String, User> getPlayers(){
        return this.players;
    }

    public void addPlayer(User player){
        this.players.put(player.getName(),player);
    }

    public Partie getGame() {
        return game;
    }

    public void setGame(Partie game) {
        this.game = game;
    }
}
