package com.example.airbattle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airbattle.PlayerDatabase.PlayerData;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private List<PlayerData> playerDataList;
    private int rowCnt = 1;
    private boolean isHard;

    public PlayerAdapter(List<PlayerData> playerList, boolean isHard) {
        playerDataList = playerList;
        this.isHard = isHard;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, username, score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = (TextView)itemView.findViewById(R.id.rankTV);
            username = (TextView)itemView.findViewById(R.id.usernameTV);
            score = (TextView)itemView.findViewById(R.id.scoreTV);
        }
    }

    @NonNull
    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_row_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.ViewHolder holder, int position) {
        PlayerData playerData = playerDataList.get(position);
        holder.rank.setText(Integer.toString(rowCnt++));
        holder.username.setText(playerData.getUsername());
        if (this.isHard) {
            holder.score.setText(Integer.toString(playerData.getHScore()));
        } else {
            holder.score.setText(Integer.toString(playerData.getNScore()));
        }
    }

    @Override
    public int getItemCount() {
        return playerDataList.size();
    }
}
