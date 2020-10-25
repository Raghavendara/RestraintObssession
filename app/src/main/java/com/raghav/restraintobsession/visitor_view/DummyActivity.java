package com.raghav.restraintobsession.visitor_view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.raghav.restraintobsession.R;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);


        String postImage = getIntent().getExtras().getString("postImage") ;
        String postTitle = getIntent().getExtras().getString("title");
        String postDescription = getIntent().getExtras().getString("description");
    }
}