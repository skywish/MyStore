package com.skywish.mystore;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import cn.bmob.v3.Bmob;

/**
 * Created by skywish on 2015/4/5.
 */
public class BaseActivity extends Activity {

    public final static String Bmob_APPID="31881979bd18cef03766e0a4123ca006";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Bmob_APPID);
        Log.d("BaseActivity", getClass().getSimpleName());
    }
}
