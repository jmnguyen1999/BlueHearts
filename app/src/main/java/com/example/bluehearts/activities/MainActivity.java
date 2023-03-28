package com.example.bluehearts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.bluehearts.R;
import com.example.bluehearts.databinding.ActivityMainBinding;
import com.example.bluehearts.fragments.AddReportFragment;
import com.example.bluehearts.fragments.TokenFragment;
import com.example.bluehearts.fragments.ViewReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    ParseUser currUser = ParseUser.getCurrentUser();
    BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //1. Set up bottom nav menu:
        bottomMenu = binding.bottomNavigation;
        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_token:
                        toTokenFrag();
                        break;
                    case R.id.action_report:
                        toAddReportFrag();
                        break;
                    default:
                        toViewReportsFrag();
                        break;
                }
                return true;
            }
        });
        bottomMenu.setSelectedItemId(R.id.action_token);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_log_out:
                ParseUser.logOut();
                Intent toStartActivity = new Intent(MainActivity.this, StartActivity.class);
                startActivity(toStartActivity);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toViewReportsFrag() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ViewReportsFragment()).commit();
    }

    private void toTokenFrag() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new TokenFragment()).commit();
    }

    private void toAddReportFrag() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new AddReportFragment()).commit();
    }
}