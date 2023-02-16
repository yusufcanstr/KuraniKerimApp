package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yusufcansenturk.ux_1_kuran_kerim_app.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}