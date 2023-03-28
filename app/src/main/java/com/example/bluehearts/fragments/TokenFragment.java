package com.example.bluehearts.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bluehearts.R;
import com.example.bluehearts.databinding.FragmentTokenBinding;
import com.parse.Parse;
import com.parse.ParseUser;

import models.User;


public class TokenFragment extends Fragment {
    private static final String TAG = "TokenFragment";

    FragmentTokenBinding binding;
    User user;

    //Views from Layout file:
    TextView tvBalanceValue;
    RecyclerView rvTokenHistory;

    public TokenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTokenBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBalanceValue = binding.tvBalanceValue;
        rvTokenHistory = binding.rvTokenHistory;
        user = new User(ParseUser.getCurrentUser());
        
        tvBalanceValue.setText(String.valueOf(user.getTokenBalance()));
    }
}