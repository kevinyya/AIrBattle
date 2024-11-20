package com.example.airbattle.PlayerDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "players", indices = {@Index(value = {"username"}, unique = true)})
public class PlayerData {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "score")
    private int score;
    @ColumnInfo(name = "status")
    private boolean status;

    // Constructor
    public PlayerData(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = false;
    }

    public int getId() { return this._id; }

    public void setId(int _id) { this._id = _id; }

    public String getUsername() { return this.username; }

    public String getPassword() { return this.password; }

    public int getScore() { return this.score; }

    public void setScore(int score) { this.score = score; }

    public boolean getStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

}
