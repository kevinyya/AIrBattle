package com.example.airbattle.ui.normal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airbattle.PlayerAdapter;
import com.example.airbattle.PlayerDatabase.PlayerDao;
import com.example.airbattle.PlayerDatabase.PlayerData;
import com.example.airbattle.PlayerDatabase.PlayerDatabase;
import com.example.airbattle.R;
import com.example.airbattle.RankActivity;
import com.example.airbattle.databinding.FragmentNormalRankBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NormalRankFragment extends Fragment {
    private FragmentNormalRankBinding binding;
    private PlayerDao playerDao;
    private List<PlayerData> playerList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NormalRankViewModel normalRankViewModel =
                new ViewModelProvider(this).get(NormalRankViewModel.class);

        binding = FragmentNormalRankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get PlayerDao
        playerDao = PlayerDatabase.getInstance(getContext()).playerDao();

        // Return in Toolbar
        Toolbar toolbar = binding.normalRankTB;

        // Draw Normal Ranking Table
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get Player List
                playerList = playerDao.getAllPlayer();

                // Sort
                Collections.sort(playerList, new Comparator<PlayerData>() {
                    @Override
                    public int compare(PlayerData player1, PlayerData player2) {
                        Integer score1 = player1.getNScore();
                        Integer score2 = player2.getNScore();
                        return score2.compareTo(score1);
                    }
                });

                // Display Normal Rank Table by RecyclerView
                RecyclerView rankRV = binding.normalRankRV;
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rankRV.setLayoutManager(layoutManager);
                PlayerAdapter adapter = new PlayerAdapter(playerList, false);
                rankRV.setAdapter(adapter);
            }
        }).start();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}