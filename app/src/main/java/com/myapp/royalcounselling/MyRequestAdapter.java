package com.myapp.royalcounselling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRequestAdapter extends BaseAdapter {

    Context context;
    ArrayList<PPTRequestBean> requestList;
    String seminarIntent;
    int[] blurImages = {R.drawable.ic_baseline_check_circle_24, R.drawable.ic_baseline_cancel_24};

    public MyRequestAdapter(Context context, ArrayList<PPTRequestBean> queryList) {
        this.context = context;
        this.requestList = queryList;
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.raw_ppt_request, null);
        TextView comment = convertView.findViewById(R.id.tv_ppt_comment);
        TextView requestTime = convertView.findViewById(R.id.tv_ppt_request_time);
        TextView requestQuery = convertView.findViewById(R.id.tv_ppt_request_query);

        ImageView requestStatus = convertView.findViewById(R.id.img_request_status);
        comment.setText(requestList.get(position).getComment());
        requestTime.setText(requestList.get(position).getQueryTime());
        requestQuery.setText(requestList.get(position).getRequestQuery());
        if (requestList.get(position).isQueryOver()) {
            requestStatus.setImageResource(blurImages[0]);
        } else {
            requestStatus.setImageResource(blurImages[1]);
        }

        return convertView;

    }
}

