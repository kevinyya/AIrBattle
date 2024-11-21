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

    @Query("UPDATE players SET nScore = :nScore WHERE username = :username")
    void updateNormalScore(String username, int nScore);

    @Query("UPDATE players SET hscore = :hScore WHERE username = :username")
    void updateHardScore(String username, int hScore);

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

    @Query("SELECT nScore FROM players WHERE status = true")
    int getNormalScore();

    @Query("SELECT nScore FROM players WHERE username = :username")
    int getNormalScore(String username);

    @Query("SELECT hScore FROM players WHERE status = true")
    int getHardScore();

    @Query("SELECT hScore FROM players WHERE username = :username")
    int getHardScore(String username);
}
