package com.skywish.mystore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.skywish.mystore.adapter.AllGoodAdapter;
import com.skywish.mystore.model.Good;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private StaggeredGridView mGridView;
    private AllGoodAdapter allGoodAdapter;
    private Button refresh;
    private Button search;
    private EditText et_search;
    private String searchText = "";

    public static void activityStart(Context context, String data) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        et_search = (EditText) findViewById(R.id.main_bar_searchEdit);

        refresh = (Button) findViewById(R.id.main_bar_refresh);
        refresh.setOnClickListener(this);

        search = (Button) findViewById(R.id.main_bar_search);
        search.setOnClickListener(this);

//        getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
//        Activity.this的context 返回当前activity的上下文
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        queryAllGood();
    }

    public void queryAllGood() {
        BmobQuery<Good> query = new BmobQuery<Good>();
        query.order("-createdAt");      //按照时间降序
        query.addQueryKeys("title,describe,price,pic");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> goods) {
                if (goods == null || goods.size() == 0) {
                    Toast.makeText(HomeActivity.this, "没有货品",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//                progressBar.setVisibility(View.GONE);
//                tv_no.setVisibility(View.GONE);
                if (allGoodAdapter != null) {
                    allGoodAdapter.clear();
                    allGoodAdapter.notifyDataSetChanged();
                }
                allGoodAdapter = new AllGoodAdapter(getApplicationContext(),
                        R.layout.main_goodlist, goods);
                mGridView.setAdapter(allGoodAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                Toast.makeText(HomeActivity.this, "没有货品",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchGood() {
        searchText = et_search.getText().toString();
        BmobQuery<Good> query1 = new BmobQuery<Good>();
        query1.addWhereEqualTo("title", searchText);
        query1.addQueryKeys("title,describe,price,pic");

        BmobQuery<Good> query2 = new BmobQuery<Good>();
        query2.addWhereEqualTo("describe", searchText);
        query2.addQueryKeys("title,describe,price,pic");

        List<BmobQuery<Good>> queries = new ArrayList<BmobQuery<Good>>();
        queries.add(query1);
        queries.add(query2);

        BmobQuery<Good> query = new BmobQuery<Good>();
        query.or(queries);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> goods) {
                if (goods == null || goods.size() == 0) {
                    Toast.makeText(HomeActivity.this, "没有货品",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//                progressBar.setVisibility(View.GONE);
//                tv_no.setVisibility(View.GONE);
                if (allGoodAdapter != null) {
                    allGoodAdapter.clear();
                    allGoodAdapter.notifyDataSetChanged();
                }
                allGoodAdapter = new AllGoodAdapter(getApplicationContext(),
                        R.layout.main_goodlist, goods);
                mGridView.setAdapter(allGoodAdapter);
            }

            @Override
            public void onError(int code, String arg0) {
                Toast.makeText(HomeActivity.this, "没有货品",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bar_refresh:
                queryAllGood();
                break;
            case R.id.main_bar_search:
                searchGood();
                break;
            default:
                break;
        }
    }

    //    private void showErrorView(int tag) {
//        progressBar.setVisibility(View.GONE);
//        list_lost.setVisibility(View.GONE);
//        tv_no.setVisibility(View.VISIBLE);
//        if (tag == 0) {
//            tv_no.setText("list_no_data_lost");
//        } else {
//            tv_no.setText("list_no_data_found");
//        }
//    }
}
