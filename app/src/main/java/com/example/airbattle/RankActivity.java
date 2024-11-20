package com.example.airbattle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.airbattle.PlayerDatabase.PlayerData;
import com.example.airbattle.PlayerDatabase.PlayerDao;
import com.example.airbattle.PlayerDatabase.PlayerDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    private PlayerDao playerDao;
    private List<PlayerData> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        // Return in ActionBar
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // Get PlayerDao
        playerDao = PlayerDatabase.getInstance(this).playerDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get Player List
                playerList = playerDao.getAllPlayer();

                // Sort
                Collections.sort(playerList, new Comparator<PlayerData>() {
                    @Override
                    public int compare(PlayerData player1, PlayerData player2) {
                        Integer score1 = player1.getScore();
                        Integer score2 = player2.getScore();
                        return score2.compareTo(score1);
                    }
                });

                for (PlayerData player : playerList) {
                    Log.d("Debug", player.getUsername());
                    Log.d("Debug", Integer.toString(player.getScore()));
                }

                // Display Rank Table by RecyclerView
                RecyclerView rankRV = (RecyclerView) findViewById(R.id.rankRV);
                LinearLayoutManager layoutManager = new LinearLayoutManager(RankActivity.this);
                rankRV.setLayoutManager(layoutManager);
                PlayerAdapter adapter = new PlayerAdapter(playerList);
                rankRV.setAdapter(adapter);
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Return to MenuActivity
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}