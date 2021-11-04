package com.example.morpion.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.morpion.model.User;
import com.example.morpion.model.UserDao;

// Le numéro de version doit être incrémenté chaque fois que vous modifiez le schéma relationnel

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

}