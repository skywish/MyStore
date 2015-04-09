package com.skywish.mystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by skywish on 2015/4/9.
 */
public class ProfileActivity extends Activity {

    private TextView tv_username;
    private String username = "无名氏";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        tv_username = (TextView) findViewById(R.id.profile_username);
        tv_username.setText(username);
    }
}
