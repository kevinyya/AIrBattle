package com.example.airbattle.PlayerDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(PlayerData... player);
    @Update
    void update(PlayerData player);

    @Query("UPDATE players SET score = :score WHERE username = :username")
    void updateScore(String username, int score);

    @Query("UPDATE players SET status = true WHERE username = :username")
    void enableActivePlayer(String username);

    @Query("UPDATE players SET status = false WHERE status = true")
    void disableActivePlayer();

    @Delete
    void delete(PlayerData status);

    @Query("SELECT * FROM players WHERE username = :username")
    PlayerData getPlayer(String username);

    @Query("SELECT * FROM players WHERE status = true")
    PlayerData getActivePlayer();

    @Query("SELECT * FROM players")
    List<PlayerData> getAllPlayer();

    @Query("SELECT COUNT(*) FROM players")
    int getCnt();

    @Query("SELECT COUNT(*) FROM players WHERE username = :username")
    int getExisted(String username);

    @Query("SELECT COUNT(*) FROM players WHERE status = true")
    int getActive();

    @Query("SELECT score FROM players WHERE status = true")
    int getScore();

    @Query("SELECT score FROM players WHERE username = :username")
    int getScore(String username);
}
