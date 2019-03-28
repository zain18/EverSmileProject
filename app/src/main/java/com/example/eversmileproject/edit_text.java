package com.example.eversmileproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class edit_text extends AppCompatActivity {
    static SimpleDateFormat s = new SimpleDateFormat("MMddyyyyhhmmss");
    static String format = s.format(new Date());
    private static final String FILE_NAME = format+".txt";
    String path = Environment.getExternalStorageDirectory() + "/" + FILE_NAME;
    File tempFile = new File(path);
    private Button saveBtn;
    private Button backBtn;

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        mEditText = findViewById(R.id.edit_text);
        saveBtn = (Button) findViewById(R.id.button_save);
        backBtn = (Button) findViewById(R.id.button_back);

        //Create Firebase storage references
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid(); // unique reference for user
        StorageReference userRef = storageRef.child(currentUser);
        StorageReference noteUserRef = userRef.child("notes");
        final StorageReference noteRef = noteUserRef.child(FILE_NAME);
        final Uri noteFile = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/" +FILE_NAME));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(tempFile);
                    fos.write(text.getBytes());
                    mEditText.getText().clear();
                    Toast.makeText(edit_text.this, "Saved to " + Environment.getExternalStorageDirectory() + "/" + FILE_NAME,
                            Toast.LENGTH_LONG).show();

                    noteRef.putFile(noteFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(edit_text.this, view_pics.class));
            }
        });
    }
}
