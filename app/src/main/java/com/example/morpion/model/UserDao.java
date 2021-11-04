package com.example.morpion.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE name LIKE :name LIMIT 1")
    User getUserByName(String name);

    @Query("SELECT name FROM users")
    List<String> getUserNames();

    @Query("SELECT count(*) FROM users")
    Integer getUserCount();

    @Insert
    void insert(User user);

    @Update
    void update(User user);
}
