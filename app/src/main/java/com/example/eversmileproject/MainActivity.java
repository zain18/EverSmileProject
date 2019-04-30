package com.example.eversmileproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    CircleImageView profileView;

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner); // start authentication at startup
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4788F6)); // main banner
        Spannable text = new SpannableString(getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        setTitle(text);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.signout);
        Button ShareBtn = findViewById(R.id.ShareBtn);
        Button seeBtn = findViewById(R.id.SeeBtn);
        Button findBtn = findViewById(R.id.FindBtn);
        Button appointment = findViewById(R.id.appointment);

        profileView = findViewById(R.id.profile_image);

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");

        if (imgFile.exists()) {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(profileView);
        }

        mAuth = FirebaseAuth.getInstance(); // create instance of Firebase Authentication
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) { // check that user has credentials
                String getEmail = "Welcome to SmileKnect! Please Log in.";
                if (firebaseAuth.getCurrentUser() == null) { // if not current user, redirect to sign in
                    startActivity(new Intent(MainActivity.this, signin.class));
                } else { // else, get user email
                    getEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }
                setTitle(getEmail); // display email as user name
                Spannable text = new SpannableString(getTitle());
                text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                setTitle(text);

            }
        };

        // button to sign out of app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        // button to move to see your smile
        seeBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, see_eversmile.class));
            }
        }));
        // button to move to social media sharing
        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, share_eversmile.class));
            }
        });
        // button to move to find smile map
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Maps.class));
            }
        });
        // button to go to appointment system
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Appointments.class));
            }
        });
        // click avatar to move to user info
        profileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInfo.class));
            }

        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        profileView = findViewById(R.id.profile_image);

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/profile.jpg");

        if (imgFile.exists()) {
            Glide.with(MainActivity.this)
                    .load(imgFile)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(profileView);
        }
    }
}
