package com.android.fundamentals.standup.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.android.fundamentals.standup.R;

public class BoardSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}