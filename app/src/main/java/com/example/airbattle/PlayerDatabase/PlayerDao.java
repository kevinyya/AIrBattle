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
    void insert(Player ... player);
    @Update
    void update(Player player);

    @Query("UPDATE players SET score = :score WHERE username = :username")
    void updateScore(String username, int score);

    @Query("UPDATE players SET status = true WHERE username = :username")
    void enableActivePlayer(String username);

    @Query("UPDATE players SET status = false WHERE status = true")
    void disableActivePlayer();

    @Delete
    void delete(Player status);

    @Query("SELECT * FROM players WHERE username = :username")
    Player getPlayer(String username);

    @Query("SELECT * FROM players WHERE status = true")
    Player getActivePlayer();

    @Query("SELECT * FROM players")
    List<Player> getAllPlayer();

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
