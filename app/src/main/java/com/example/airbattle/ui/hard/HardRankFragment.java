package com.example.airbattle.ui.hard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.airbattle.databinding.FragmentHardRankBinding;

public class HardRankFragment extends Fragment {

    private FragmentHardRankBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HardRankViewModel hardRankViewModel =
                new ViewModelProvider(this).get(HardRankViewModel.class);

        binding = FragmentHardRankBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Return in Toolbar
        Toolbar toolbar = binding.hardRankTB;



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