package com.example.bluehearts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bluehearts.R;
import com.example.bluehearts.databinding.ActivityStartBinding;
import com.example.bluehearts.fragments.LoginFragment;
import com.example.bluehearts.fragments.SignUpFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class StartActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener{
    private static final String TAG = "StartActivity";
    ActivityStartBinding binding;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "root = " + binding.getRoot());

        //Check if we are already logged in --> automatically go to Home:
        if(ParseUser.getCurrentUser() != null){
            //TODO: For testing purposes log out the user:
            ParseUser.logOut();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
            //toMainActivity();
        }
        else {  //go to Login Fragment by default:
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
        }
    }

    private void toMainActivity(){
        Intent toMainActivity = new Intent(StartActivity.this, MainActivity.class);
        startActivity(toMainActivity);
        finish();
    }

    private void attemptLogin(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(StartActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                }
                else{
                    Log.e(TAG, "could not login: " , e);
                }
            }
        });
    }

    @Override
    public void onLogin() {
        toMainActivity();
    }

    @Override
    public void toSignUpFrag() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new SignUpFragment()).commit();
    }

    @Override
    public void toLoginFrag() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
    }

    @Override
    public void onSuccessfulSignUp() {
        toMainActivity();
    }


}