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

public class MyPersonalRequestAdapter extends BaseAdapter {

    Context context;
    ArrayList<PersonalCounsellingBean> requestList;
    String seminarIntent;
    int[] blurImages = {R.drawable.ic_baseline_check_circle_24, R.drawable.ic_baseline_cancel_24};

    public MyPersonalRequestAdapter(Context context, ArrayList<PersonalCounsellingBean> queryList) {
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
        convertView = layoutInflater.inflate(R.layout.raw_personal_counselling_request, null);

//        TextView requestID = convertView.findViewById(R.id.tv_personal_request_id);
        TextView requestTime = convertView.findViewById(R.id.tv_personal_request_time);
        TextView counsellingType = convertView.findViewById(R.id.tv_personal_counselling_type);
        TextView startTime = convertView.findViewById(R.id.tv_personal_request_start_time);
        ImageView requestStatus = convertView.findViewById(R.id.img_request_status_counselling);

//        requestID.setText(requestList.get(position).getPersonalCounsellingID());
        requestTime.setText(requestList.get(position).getRequestTime());
        counsellingType.setText(requestList.get(position).getCounsellingType());
        startTime.setText(requestList.get(position).getStartTime());
        if (requestList.get(position).isAccepted()) {
            requestStatus.setImageResource(blurImages[0]);
        } else {
            requestStatus.setImageResource(blurImages[1]);
        }

        return convertView;

    }
}

