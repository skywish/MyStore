package com.skywish.mystore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skywish.mystore.adapter.MyGoodAdapter;
import com.skywish.mystore.model.Good;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by skywish on 2015/4/7.
 */
public class MyGoodActivity extends BaseActivity implements View.OnClickListener{

    private Button addGood;
    private Button btn_refresh;
    private ProgressBar progressBar;
    private ListView list_mygood;
    private TextView tv_no;
    private MyGoodAdapter myGoodAdapter;
    private String username;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, MyGoodActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygood);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        list_mygood = (ListView) findViewById(R.id.lv_goodlist);
        tv_no = (TextView) findViewById(R.id.tv_no);

        btn_refresh = (Button) findViewById(R.id.myGood_btn_refresh);
        btn_refresh.setOnClickListener(this);
        addGood = (Button) findViewById(R.id.btn_addgood);
        addGood.setOnClickListener(this);
        queryMyGood();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addgood:
                AddgoodActivity.activityStart(MyGoodActivity.this, username);
                break;
            case R.id.myGood_btn_refresh:
                queryMyGood();
                break;
            default:
                break;
        }
    }

    public void queryMyGood() {
        BmobQuery<Good> query = new BmobQuery<Good>();
        query.addWhereEqualTo("username", username);
        query.order("-createdAt");      //按照时间降序
        query.addQueryKeys("title,sales,stock");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> goods) {
                if (goods == null || goods.size() == 0) {
                    showErrorView(0);
                    return;
                }
                progressBar.setVisibility(View.GONE);
                tv_no.setVisibility(View.GONE);
                list_mygood.setVisibility(View.VISIBLE);
                if (myGoodAdapter != null) {
                    myGoodAdapter.clear();
                    myGoodAdapter.notifyDataSetChanged();
                }
                myGoodAdapter = new MyGoodAdapter(getApplicationContext(),
                        R.layout.mygood_goodlist, goods);
                list_mygood.setAdapter(myGoodAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                showErrorView(0);
            }
        });
    }

    private void showErrorView(int tag) {
        progressBar.setVisibility(View.GONE);
        list_mygood.setVisibility(View.GONE);
        tv_no.setVisibility(View.VISIBLE);
        if (tag == 0) {
            tv_no.setText("没有商品");
        }
    }
}
