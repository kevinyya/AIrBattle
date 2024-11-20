package com.example.airbattle.PlayerDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PlayerData.class}, version = 2)
public abstract class PlayerDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
    private static volatile PlayerDatabase INSTANCE;
    public static PlayerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlayerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PlayerDatabase.class,
                                    "test_player_database1"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}