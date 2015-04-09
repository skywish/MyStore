package com.skywish.mystore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by skywish on 2015/4/9.
 */
public class GoodDetailActivity extends Activity {

    private TextView tv_title;
    private TextView tv_price;
    private TextView tv_describe;
    private ImageView iv_pic;
    private String url;

    private String title="";
    private String price="";
    private String describe="";
    private String address="";
    private String phone="";
    private String qq="";

    public static void activityStart(Context context, String title,
                                     String describe, String price) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("describe", describe);
        intent.putExtra("price", price);
//        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gooddetail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        describe = intent.getStringExtra("describe");
        price = intent.getStringExtra("price");
//        url = intent.getStringExtra("url");

        tv_title = (TextView) findViewById(R.id.goodDetail_title);
        tv_price = (TextView) findViewById(R.id.goodDetail_price);
        tv_describe = (TextView) findViewById(R.id.goodDetail_describe);
        iv_pic = (ImageView) findViewById(R.id.goodDetail_pic);

        tv_title.setText(title);
        tv_price.setText("￥"+price+" RMB");
        tv_describe.setText(describe);
        HomeActivity.pic.loadImage(GoodDetailActivity.this, iv_pic, 300, 300);
    }
}
