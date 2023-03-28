package com.example.bluehearts.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bluehearts.R;
import com.example.bluehearts.TokensAdapter;
import com.example.bluehearts.databinding.FragmentTokenBinding;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import models.Token;
import models.User;


public class TokenFragment extends Fragment {
    private static final String TAG = "TokenFragment";

    FragmentTokenBinding binding;
    User user;

    //Views from Layout file:
    TextView tvBalanceValue;
    RecyclerView rvTokenHistory;
    TextView tvNoTokens;

    //For RV:
    List<Token> tokens;
    TokensAdapter adapter;

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
        tvNoTokens = binding.tvNoTokens;
        tvNoTokens.setVisibility(View.VISIBLE);
        user = new User(ParseUser.getCurrentUser());
        
        tvBalanceValue.setText(String.valueOf(user.getTokenBalance()));

        tokens = new ArrayList<>();
        adapter = new TokensAdapter(getContext(), tokens);
        rvTokenHistory.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTokenHistory.setLayoutManager(linearLayoutManager);
        queryTokens();
    }

    public void queryTokens(){
        ParseQuery<Token> query = ParseQuery.getQuery(Token.class);
        query.whereEqualTo(Token.KEY_USERID, ParseUser.getCurrentUser().getObjectId());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Token>() {
            @Override
            public void done(List<Token> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() == 0){
                        tvNoTokens.setVisibility(View.VISIBLE);
                    }
                    else{
                        tvNoTokens.setVisibility(View.INVISIBLE);
                    }
                    Log.d(TAG, "success getting tokens! token.size() = " + objects.size());
                    tokens.clear();
                    tokens.addAll(objects);
                    adapter.notifyDataSetChanged();
                    //tells swipe container that we've refreshed!
                }
                else{
                    Log.e(TAG, "failed getting tokens:", e);
                }
            }
        });
    }
}