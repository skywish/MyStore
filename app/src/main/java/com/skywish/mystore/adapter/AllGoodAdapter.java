package com.skywish.mystore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skywish.mystore.R;
import com.skywish.mystore.model.Good;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by skywish on 2015/4/8.
 */
public class AllGoodAdapter extends ArrayAdapter<Good>{

    static class ViewHolder {
        DynamicHeightImageView dynamicHeightImageView;
        Button btnLike;
        TextView title;
        TextView describe;
        TextView price;
    }

    private LayoutInflater mLayoutInflater;
    private Random mRandom;
    private ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    private int resourceId;

    public AllGoodAdapter(Context context, int textViewResourceId, List<Good> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.purple);
        mBackgroundColors.add(R.color.red);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Good good = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.dynamicHeightImageView = (DynamicHeightImageView)
                    view.findViewById(R.id.main_goodList_pic);
            viewHolder.btnLike = (Button) view.findViewById(R.id.main_goodList_like);
            viewHolder.title = (TextView) view.findViewById(R.id.main_goodList_title);
            viewHolder.describe = (TextView) view.findViewById(R.id.main_goodList_describe);
            viewHolder.price = (TextView) view.findViewById(R.id.main_goodList_price);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        view.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
        //根据position得到一个项高
        double positionHeight = getPositionRatio(position);
        viewHolder.dynamicHeightImageView.setHeightRatio(positionHeight);
        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getContext(), "Button Clicked Position ",
                        Toast.LENGTH_SHORT).show();
            }
        });

//        String url = good.getPic().getUrl();
//        if (url != null) {
//            try {
//                viewHolder.dynamicHeightImageView.setImageBitmap(loadImageFromUrl(url));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        String url = null;
        if(good.getPic() != null){
            url = good.getPic().getFileUrl(getContext());
        }
        ImageLoader.getInstance().displayImage(url, viewHolder.dynamicHeightImageView);
        viewHolder.title.setText(good.getTitle());
        viewHolder.describe.setText(good.getDescribe());
        viewHolder.price.setText("￥"+good.getPrice()+" RMB");
        return view;
    }

    //得到随机项高
    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 2.0 the width
    }

    public Bitmap loadImageFromUrl(String url) throws Exception  {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK)  {
            Log.e("PicShow", "Request URL failed, error code =" + statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            Log.e("PicShow", "HttpEntity is null");
        }
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = entity.getContent();
            byte[] buf = new byte[1024];
            int readBytes = -1;
            while ((readBytes = is.read(buf)) != -1) {
                baos.write(buf, 0, readBytes);
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        byte[] imageArray = baos.toByteArray();
        return BitmapFactory.decodeByteArray(
                imageArray, 0, imageArray.length);
    }
}
