package com.skywish.mystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skywish.mystore.R;
import com.skywish.mystore.model.Good;

import java.util.List;

/**
 * Created by skywish on 2015/4/8.
 */
public class MyGoodAdapter extends ArrayAdapter<Good>{

    private int resourceId;

    public MyGoodAdapter(Context context, int textViewResourceId, List<Good> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Good good = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.sales = (TextView) view.findViewById(R.id.tv_sales);
            viewHolder.stock = (TextView) view.findViewById(R.id.tv_stock);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.stock.setText(good.getStock()+"");
        viewHolder.sales.setText(good.getSales()+"");
        viewHolder.title.setText(good.getTitle());
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView sales;
        TextView stock;
    }
}
