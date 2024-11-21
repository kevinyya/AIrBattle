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
    @ColumnInfo(name = "nScore")
    private int nScore;
    @ColumnInfo(name = "hScore")
    private int hScore;
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

    public int getNScore() { return this.nScore; }
    public void setNScore(int nScore) { this.nScore = nScore; }

    public int getHScore() { return this.hScore; }
    public void setHScore(int hScore) { this.hScore = hScore; }

    public boolean getStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

}

