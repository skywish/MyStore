package com.skywish.mystore;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * Created by skywish on 2015/4/9.
 */
public class MainTabActivity extends TabActivity implements
        CompoundButton.OnCheckedChangeListener {

    private TabHost mTabHost;
    private Intent homeIntent;
    private Intent shopIntent;
    private Intent profileIntent;

    public static void activityStart(Context context, String data) {
        Intent intent = new Intent(context, MainTabActivity.class);
        intent.putExtra("username", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabs);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        this.homeIntent = new Intent(this, HomeActivity.class);
        this.profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("username", username);
        this.shopIntent = new Intent(this, MyGoodActivity.class);
        shopIntent.putExtra("username", username);

        ((RadioButton) findViewById(R.id.radio_home))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_shop))
                .setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_profile))
                .setOnCheckedChangeListener(this);

        setupIntent();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_home:
                    this.mTabHost.setCurrentTabByTag("HOME_TAB");
                    break;
                case R.id.radio_shop:
                    this.mTabHost.setCurrentTabByTag("SHOP_TAB");
                    break;
                case R.id.radio_profile:
                    this.mTabHost.setCurrentTabByTag("PROFILE_TAB");
                    break;
                default:
                    break;
            }
        }
    }

    private void setupIntent() {

        this.mTabHost = getTabHost();
        TabHost localTabHost = this.mTabHost;

        localTabHost.addTab(buildTabSpec("HOME_TAB", R.string.tab_home,
                R.drawable.tab_btn_home, this.homeIntent));

        localTabHost.addTab(buildTabSpec("SHOP_TAB", R.string.tab_shop,
                R.drawable.tab_btn_shop, this.shopIntent));

        localTabHost.addTab(buildTabSpec("PROFILE_TAB",R.string.tab_profile,
                R.drawable.tab_btn_profile, this.profileIntent));

        this.mTabHost.setCurrentTabByTag("HOME_TAB");
    }

    private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
                                         final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
                getResources().getDrawable(resIcon)).setContent(content);
    }
}
