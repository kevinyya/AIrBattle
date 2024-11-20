package com.example.airbattle;

import com.example.airbattle.PlayerDatabase.PlayerDao;

public class PlayerData {
    private String rank;
    private String username;
    private String score;

    // Constructor
    public PlayerData(String rank, String username, String score) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public String getRank() {return rank;}
    public String getUsername() {return username;}
    public String getScore() {return score;}

    public void setRank(String rank) {this.rank = rank;}
}
