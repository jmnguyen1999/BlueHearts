package com.example.bluehearts.fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.bluehearts.activities.MainActivity;
import com.example.bluehearts.databinding.FragmentAddReportBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Random;

import models.Report;
import models.User;


public class AddReportFragment extends Fragment {
    private static final String TAG = "AddReportFrag";
    private static final int MIN_TOKEN_EARNED = 1;  //(Inclusive)
    private static final int MAX_TOKEN_EARNED = 4; //(exclusive)
    FragmentAddReportBinding binding;

    //Views from Layout:
    EditText etTitle;
    EditText etDescription;
    TextView tvCategoryList;
    Button btnSubmit;

    List<String> categoryChoices = Arrays.asList("Energy", "Environment", "Oil and Gas", "Waste Disposal", "Fisheries", "Aquaculture", "Other");

    public AddReportFragment() {
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
        binding = FragmentAddReportBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCategoryList = binding.tvCategoryList;
        etDescription = binding.etDescriptionInput;
        etTitle = binding.etTitleInput;
        btnSubmit = binding.btnSubmit;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report report = new Report();
                report.setDescription(etDescription.getText().toString());
                report.setTitle(etTitle.getText().toString());
                List<String> themes = new ArrayList<>();
                themes.add(tvCategoryList.getText().toString());
                report.setThemes(themes);
                report.setUserId(ParseUser.getCurrentUser().getObjectId());

                Log.d(TAG, "title: " + etTitle.getText().toString() + "desc: " + etDescription.getText().toString());
                report.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Log.d(TAG, "New Report row created.");
                            Toast.makeText(getContext(), "Report saved!", Toast.LENGTH_SHORT).show();

                            onSuccessfulReport(report);
                        }
                        else{
                            Log.e(TAG, "New Report couldn't be created", e);
                        }
                    }
                });
            }
        });
    }

    //Purpose:  "Algorithm" invoked to determine how make tokens should be rewarded to the user based on the report. This just randomly picks a value from given range
    public void onSuccessfulReport(Report report){
        Random random = new Random();
        int earnedTokens = random.nextInt(MAX_TOKEN_EARNED - MIN_TOKEN_EARNED) + MIN_TOKEN_EARNED;
        ParseUser pUser = ParseUser.getCurrentUser();
        User user = new User(pUser);
        int currTokens = user.getTokenBalance();
        Log.d(TAG, "earnedTokens = " + earnedTokens + "currTokens = " + currTokens);

        pUser.put(User.KEY_TOKENBALANCE, earnedTokens+currTokens);
        pUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d(TAG, "User's tokenBalance updated");
                    Toast.makeText(getContext(), "Your new balance is: " + (earnedTokens+currTokens), Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.e(TAG, "User's tokenBalance couldn't be updated", e);
                }
            }
        });

        //Update Report now that token worth has been evaluated:
        report.setTokenWorth(earnedTokens);
        report.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d(TAG, "Report object saved token worth successfully");
                }
                else{
                    Log.e(TAG, "New Report object could not save token worth", e);
                }
            }
        });


    }
}