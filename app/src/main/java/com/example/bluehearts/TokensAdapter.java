package com.example.bluehearts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.w3c.dom.Text;

import java.util.List;

import models.Token;

public class TokensAdapter extends RecyclerView.Adapter<TokensAdapter.ViewHolder> {

    com.example.bluehearts.databinding.ItemTokenBinding binding;
    Context context;
    List<Token> tokens;

    public TokensAdapter(Context context, List<Token> tokens){
        this.context = context;
        this.tokens = tokens;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = com.example.bluehearts.databinding.ItemTokenBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull TokensAdapter.ViewHolder holder, int position) {
        Token token = tokens.get(position);
        holder.bind(token);
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTokenValue;
        TextView tvTimestamp;
        TextView tvReportTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTokenValue = binding.tvTokenValue;
            tvTimestamp = binding.tvTimestamp;
            tvReportTitle = binding.tvReportTitle;
        }

        public void bind(Token token){
            tvTokenValue.setText(String.valueOf(token.getValue()));
            tvTimestamp.setText(token.getFormattedTimestamp());
            tvReportTitle.setText(token.getReportObj().getTitle());
        }

    }
}
