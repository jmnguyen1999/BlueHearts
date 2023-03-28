package com.example.bluehearts.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehearts.R;
import com.example.bluehearts.databinding.FragmentLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginFragment extends Fragment {
    private static String TAG = "LoginFragment";

    FragmentLoginBinding binding;
    LoginListener listener;

    //Views from layout:
    TextView tvTitle;
    EditText etUsername;
    EditText etPassword;
    Button btnSubmit;
    TextView tvToSignUpFrag;

    public LoginFragment() {
        // Required empty public constructor
    }

    public interface LoginListener{
        void onLogin();
        void toSignUpFrag();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginListener){
            listener = (LoginListener) context;
        }
        else{
            Log.e(TAG, "Context is NOT implementing LoginListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnSubmit = binding.btnSubmit;
        tvToSignUpFrag = binding.tvToSignUpFrag;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(!(username.isEmpty() && password.isEmpty())){
                    login(username, password);
                }
                else{
                    Toast.makeText(getContext(), "Please fill out all the inputs.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvToSignUpFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.toSignUpFrag();
            }
        });
    }

    public void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(getContext(), "Successfully logged in!", Toast.LENGTH_SHORT).show();
                    listener.onLogin();
                }
                else{
                    Toast.makeText(getContext(), "Invalid username/password", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Login unsucessful: " , e);
                }
            }
        });
    }
}