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
import com.example.bluehearts.databinding.FragmentSignUpBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class SignUpFragment extends Fragment {
    private static String TAG = "SignUpFragment";

    FragmentSignUpBinding binding;
    SignUpListener listener;

    //Views from Layout file:
    TextView tvTitle;
    EditText etUsername;
    EditText etEmail;
    EditText etFname;
    EditText etLname;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btnSubmit;
    TextView tvToLoginFrag;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public interface SignUpListener {
        void toLoginFrag();
        void onSuccessfulSignUp();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SignUpListener){
            listener = (SignUpListener) context;
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
        binding = FragmentSignUpBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = binding.tvTitle;
        etUsername = binding.etUsername;
        etFname = binding.etFname;
        etLname = binding.etLname;
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        btnSubmit = binding.btnSubmit;
        tvToLoginFrag = binding.tvToLoginFrag;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Try creating new account here:
                String fname = etFname.getText().toString();
                String lname = etLname.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String cPassword = etConfirmPassword.getText().toString();


                if((!fname.isEmpty() && !lname.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !cPassword.isEmpty())){
                    if(!password.equals(cPassword)){
                        Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        attemptSignUp(username, email, password);
                    }

                }
                else{
                    Toast.makeText(getContext(), "Please fill out all the inputs.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvToLoginFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.toLoginFrag();
            }
        });
    }

    //Purpose: Attempt to sign up a new ParseUser given the info inputted in the Views.
    public void attemptSignUp(String username, String email, String password){
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        newUser.signUpInBackground(e -> {
            if (e == null) {
                // Here you should tell the user to verify the e-mail
                Toast.makeText(getContext(), "Successfully signed up!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Sign up successful");
                attemptLogin(username, password);
            } else {
                Log.e(TAG, "user sign up failed.", e);
                //If error = email invalid format --> show corresponding toast
                if(e.getLocalizedMessage().equals("Email address format is invalid.")){
                    Toast.makeText(getContext(), "Email is invalid.", Toast.LENGTH_SHORT).show();
                }
                //if error = user with same credentials already exist.
                else if(e.getLocalizedMessage().equals("Account already exists for this username.")){
                    Toast.makeText(getContext(), "Sorry, that username is taken.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Purpose:  Invoked after attemptedSignUp is successful
    private void attemptLogin(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    listener.onSuccessfulSignUp();
                }
                else{
                    Log.e(TAG, "could not login: " , e);
                }
            }
        });
    }
}