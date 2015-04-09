package com.skywish.mystore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skywish.mystore.R;
import com.skywish.mystore.model.Good;

import java.util.List;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by skywish on 2015/4/8.
 */
public class MyGoodAdapter extends ArrayAdapter<Good>{

    private int resourceId;
    private String objectId;

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
            viewHolder.deleteBtn = (Button) view.findViewById(R.id.myGoodList_delete);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.stock.setText(good.getStock()+"");
        viewHolder.sales.setText(good.getSales()+"");
        viewHolder.title.setText(good.getTitle());
        objectId = good.getObjectId();

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("确认");
//                builder.setMessage("确定吗？");
//                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteItem();
//                    }
//                });
//                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });

            }
        });
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView sales;
        TextView stock;
        Button deleteBtn;
    }

    public void deleteItem() {
        Good good = new Good();
        good.setObjectId(objectId);
        good.delete(getContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "删除成功，请刷新",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getContext(), "删除失败，请重试",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
