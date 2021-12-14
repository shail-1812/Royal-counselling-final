package com.myapp.royalcounselling.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.myapp.royalcounselling.R;
import com.myapp.royalcounselling.Utils;
import com.myapp.royalcounselling.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PersonalCounsellingFragment extends Fragment {


    CalendarView calendar;
    String date;
    Spinner spinner;
    ArrayList<String> counsellingTimeOnline = new ArrayList<String>();
    ArrayList<String> counsellingTimeOffline = new ArrayList<String>();
    TextView textView;
    Button counsellingBook;
    String strData;
    RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_counselling, container, false);
        spinner = rootView.findViewById(R.id.spinner_time);
        calendar = rootView.findViewById(R.id.calendar);
        textView = rootView.findViewById(R.id.tv_moc);
        radioGroup = rootView.findViewById(R.id.radio_group);
        counsellingBook = rootView.findViewById(R.id.btn_register_personal);
        radioGroup.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        counsellingBook.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
//        spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        calendar.setOnDateChangeListener(

                (view, year, month, dayOfMonth) -> {
                    date
                            = dayOfMonth + "-"
                            + (month + 1) + "-" + year;

                    //Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
                    radioGroup.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    counsellingBook.setVisibility(View.VISIBLE);
                    radioGroup.clearCheck();


                    String urlGet = Utils.main_url + "getAllActiveCounsellingSlots/" + date;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGet, response -> {

                        Log.e("TAG", "onResponse: " + response);
                        try {
                            counsellingTimeOnline.clear();
                            counsellingTimeOffline.clear();

                            counsellingTimeOnline.add("Time Slots");
                            counsellingTimeOffline.add("Time Slots");

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String counsellingStart = (object.getString("startTime"));
                                String counsellingId = (object.getString("timeSlotID"));
                                String counsellingType = (object.getString("counsellingType"));
                                String[] parts = counsellingStart.split("T");
                                counsellingStart = parts[1];
                                if (counsellingType.equals("Online")) {
                                    counsellingTimeOnline.add(counsellingId + ") " + counsellingStart);
                                }
                                if (counsellingType.equals("Offline")) {
                                    counsellingTimeOffline.add(counsellingId + ") " + counsellingStart);
                                }
                            }
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    spinner.setVisibility(View.VISIBLE);

                                    if (checkedId == R.id.radio_online) {
                                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, counsellingTimeOnline) {
                                            @Override
                                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                                TextView tvData = (TextView) super.getDropDownView(position, convertView, parent);

                                                if (position == 0) {
                                                    tvData.setTextColor(Color.GRAY);
                                                } else {
                                                    tvData.setTextColor(Color.WHITE);
                                                }

                                                return tvData;
                                            }
                                        };
                                        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(stringArrayAdapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                if (position != 0) {
                                                    strData = "";
                                                    strData = parent.getItemAtPosition(position).toString();
//                                        Toast.makeText(getActivity(), strData, Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    } else if (checkedId == R.id.radio_offline) {
                                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, counsellingTimeOffline) {
                                            @Override
                                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                                TextView tvData = (TextView) super.getDropDownView(position, convertView, parent);

                                                if (position == 0) {
                                                    tvData.setTextColor(Color.GRAY);
                                                } else {
                                                    tvData.setTextColor(Color.WHITE);


                                                }

                                                return tvData;
                                            }
                                        };
                                        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(stringArrayAdapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                if (position != 0) {
                                                    strData = "";
                                                    strData = parent.getItemAtPosition(position).toString();
//                                        Toast.makeText(getActivity(), strData, Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                }
                            });


                            //Toast.makeText(getContext(), counsellingTime.toString(), Toast.LENGTH_LONG).show();
                            counsellingBook.setOnClickListener(v -> {
                                try{
                                    String[] couID = strData.split("\\)");
                                    String ID = null;
                                    ID = couID[0];
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("counsellingID", ID);
                                    editor.putInt("flag", 1);
                                    editor.apply();

                                    Fragment aboutUs = new AboutRoyalActivity();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, aboutUs);
                                    fragmentTransaction.commit();
                                }catch(Exception e){
                                    Toast.makeText(getActivity(),"No Time Slot Selected",Toast.LENGTH_SHORT).show();
                                }

                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }, error -> {

                    });

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                });

        return rootView;
    }
}