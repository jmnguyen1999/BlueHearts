package com.example.bluehearts.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluehearts.R;
import com.example.bluehearts.activities.MainActivity;
import com.example.bluehearts.databinding.FragmentAddReportBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import org.w3c.dom.Text;

import java.io.File;
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

    //For camera access and photo taking
    public final static int READ_EXTERNAL_REQUEST_CODE = 100;
    public String photoFileName = "photo.jpg";
    File photoFile;
    Uri imageUri;
    Uri fileProvider;
    boolean imageTaken;
    ActivityResultLauncher<Intent> resultLauncher;

    //Views from Layout:
    EditText etTitle;
    EditText etDescription;
    TextView tvCategoryList;
    Button btnSubmit;
    ImageView ivImages;
    ImageView ivAddImages;

    List<String> categoryChoices = Arrays.asList("Energy", "Environment", "Oil and Gas", "Waste Disposal", "Fisheries", "Aquaculture", "Other");

    public AddReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To launch intent to gallery + handle it later
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        /*if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            ivImages.setImageURI(imageUri);
                            imageTaken = true;
                            if(photoFile.exists()){
                                Log.d(TAG, "photofile exists");
                            }
                            else{
                                Log.d(TAG, "photofile DOES NOT exist");
                            }
                        }
                        else{
                            imageTaken = false;
                            Log.d(TAG, "picture wasn't taken");
                        }*/

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            if(ivImages != null) {
                                ivImages.setImageBitmap(takenImage);
                                imageTaken = true;
                            }
                            else{
                                imageTaken = false;
                                Log.e(TAG,"imageView was null!");
                            }
                        }
                        else{
                            imageTaken= false;
                            Log.d(TAG, "picture wasn't taken");
                        }
                    }
                });
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
        ivImages= binding.ivImages;
        ivAddImages = binding.ivAddPhotos;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Report model to save into Parse database:
                Report report = new Report();
                report.setDescription(etDescription.getText().toString());
                report.setTitle(etTitle.getText().toString());

                //get list of themes: Right now only 1 theme possible
                List<String> themes = new ArrayList<>();
                themes.add(tvCategoryList.getText().toString());
                report.setThemes(themes);

                //Get list of photos: Right now only 1 photo possible
                if(photoFile.exists()) {
                    List<ParseFile> reportImages = new ArrayList<>();
                    reportImages.add(new ParseFile(photoFile));
                    report.setImages(reportImages);
                }

                //Tie to current user ID:
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

        imageTaken = false;
        //1.) Set up onclick listeners:
        //1a.) Initialize a file to store the taken pictures:
        photoFile = getPhotoFileUri(photoFileName);          // creates a file reference for future access!
        //Encapsulate photoFile in fileprovider (Parcelable) --> able to be passed into an Intent!
        fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.bluehearts", photoFile);
        ivAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

                //Do we have the permission to read the gallery --> go intent to gallery
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    //Request for the permission --> check permission again
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST_CODE);
                    ivAddImages.callOnClick();
                }
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

        //TODO: Go back to TokenFrag
    }

    //Purpose:      Goes to camera for user to take a picture --> activityResultListener handles it!
    public void launchCamera() {
        //Initialize intent to go to camera for taking a pic --> pass in the file to store the taken pic in --> launch intent
        Intent toCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        toCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        resultLauncher.launch(toCamera);
    }

    //Purpose:      Returns a file to store the picture in! Gets a file in the phone's external files directory where images are stored.
    public File getPhotoFileUri(String fileName) {
        File pathToDirectory = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);                 //get the path to our directory

        // Create the storage directory if it does not exist
        if (!pathToDirectory.exists() && !pathToDirectory.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(pathToDirectory.getPath() + File.separator + fileName);             //Make a file in this path! Named whatever name was passed in
    }
}