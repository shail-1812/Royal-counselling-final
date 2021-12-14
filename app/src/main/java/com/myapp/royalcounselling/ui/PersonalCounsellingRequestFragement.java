package com.myapp.royalcounselling.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.myapp.royalcounselling.MyPersonalRequestAdapter;
import com.myapp.royalcounselling.PersonalCounsellingBean;
import com.myapp.royalcounselling.R;
import com.myapp.royalcounselling.Utils;
import com.myapp.royalcounselling.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PersonalCounsellingRequestFragement extends Fragment {
    ArrayList<PersonalCounsellingBean> requestList = new ArrayList<PersonalCounsellingBean>();
    ListView listView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragement_personal_counselling_request, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");
        String urlPost = Utils.main_url + "requestForPersonalCounsellingByUser/" + email;
        textView = rootView.findViewById(R.id.txt_counselling_request);

//        Toast.makeText(getActivity(), urlPost, Toast.LENGTH_LONG).show();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPost, response -> {


            Log.e("TAG", "onResponse: " + response);
            String requestID;
            boolean accepted;
            String startTime;
            String requestTime;
            String counsellingType;

            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    PersonalCounsellingBean request = new PersonalCounsellingBean();
                    JSONObject object = jsonArray.getJSONObject(i);
                    requestID = (String.valueOf(object.getInt("personalCID")));
                    counsellingType = (String.valueOf(object.getString("counsellingType")));

                    startTime = (String.valueOf(object.getString("startTime"))).replace('T', ' ').replace('+', ' ');
                    requestTime = (String.valueOf(object.getString("requestedAt"))).replace('T', ' ').replace('+', ' ');
                    String acceptString = (String.valueOf(object.getString("accepted")));

                    try {
                        Log.w("ACCEPT STATUS", requestID + "     " + acceptString);
                        if (acceptString.equals(null)) {
                            accepted = false;
                        } else if (acceptString.equals("f")) {
                            accepted = false;
                        } else if (acceptString.equals("t")) {
                            accepted = true;
                        } else {
                            accepted = false;
                        }
                    } catch (Exception e) {
                        accepted = false;
                    }
                    request.setAccepted(accepted);
                    request.setCounsellingType(counsellingType);
                    request.setRequestTime(requestTime);
                    request.setStartTime(startTime);
                    request.setPersonalCounsellingID(requestID);
                    Log.e("requestAccepted", String.valueOf(accepted));
                    requestList.add(request);
                }
                if (requestList.isEmpty()) {
                    textView.setText("No counselling request made");
                } else {
                    Log.e("checking", "inside else");
                    textView.setText(" ");
                }

                listView = rootView.findViewById(R.id.list);
                MyPersonalRequestAdapter myRequestAdapter = new MyPersonalRequestAdapter(getContext(), requestList);
                listView.setAdapter(myRequestAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.e("api error", "something went wrong" + error)) {


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        return rootView;
    }
}
