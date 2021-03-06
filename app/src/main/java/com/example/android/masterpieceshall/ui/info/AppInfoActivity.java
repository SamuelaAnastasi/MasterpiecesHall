package com.example.android.masterpieceshall.ui.info;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.masterpieceshall.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppInfoActivity extends AppCompatActivity {

    ActionBar actionBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindString(R.string.application_info_title)
    String applicationInfoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(applicationInfoTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
