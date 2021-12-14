package com.myapp.royalcounselling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MySeminarAdapter extends BaseAdapter {

    Context context;
    ArrayList<Seminar> seminarArrayList;
    String seminarIntent;
    int[] blurImages = {R.drawable.seminarimage};

    public MySeminarAdapter(Context context, ArrayList<Seminar> seminarArrayList, String seminarIntent) {
        this.context = context;
        this.seminarArrayList = seminarArrayList;
        this.seminarIntent = seminarIntent;
    }

    @Override
    public int getCount() {
        return seminarArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return seminarArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.raw_seminar_list, null);
        TextView seminarName = convertView.findViewById(R.id.tv_seminar_name_card);
        TextView seminarDate = convertView.findViewById(R.id.tv_seminar_date_card);

        ImageView seminarImage = convertView.findViewById(R.id.img_seminar);
        seminarName.setText(seminarArrayList.get(position).getSeminarName());
        int b = (int) (Math.random() * (blurImages.length));
        seminarImage.setImageResource(blurImages[b]);
        seminarDate.setText(seminarArrayList.get(position).getSeminarStart());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seminar seminar = seminarArrayList.get(position);
                String seminarName = seminar.getSeminarName();
                String seminarDescription = seminar.getSeminarDescription();
                String registrationStart = seminar.getSeminarRegistrationStart();
                String registrationEnd = seminar.getSeminarRegistrationEnd();
                String seminarType = seminar.getSeminarType();
                String seminarStart = seminar.getSeminarStart();
                String seminarEnd = seminar.getSeminarEnd();
                String seminarID = seminar.getSeminarId();
                if (seminarIntent.equals("nonRegistered")) {
                    Intent i = new Intent(context, IndividualSeminarActivity.class);
                    i.putExtra("seminarName", seminarName);
                    i.putExtra("seminarDescription", seminarDescription);
                    i.putExtra("registrationStart", registrationStart);
                    i.putExtra("registrationEnd", registrationEnd);
                    i.putExtra("seminarType", seminarType);
                    i.putExtra("seminarStart", seminarStart);
                    i.putExtra("seminarEnd", seminarEnd);
                    i.putExtra("seminarId", seminarID);
                    context.startActivity(i);
                } else if (seminarIntent.equals("registered")) {
                    Intent i = new Intent(context, IndividualRegisteredActivity.class);
                    i.putExtra("seminarName", seminarName);
                    i.putExtra("seminarDescription", seminarDescription);
                    i.putExtra("registrationStart", registrationStart);
                    i.putExtra("registrationEnd", registrationEnd);
                    i.putExtra("seminarType", seminarType);
                    i.putExtra("seminarStart", seminarStart);
                    i.putExtra("seminarEnd", seminarEnd);
                    i.putExtra("seminarId", seminarID);
                    context.startActivity(i);
                }
            }
        });
        return convertView;

    }
}

