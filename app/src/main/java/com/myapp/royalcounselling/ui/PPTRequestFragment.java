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
import com.myapp.royalcounselling.MyRequestAdapter;
import com.myapp.royalcounselling.PPTRequestBean;
import com.myapp.royalcounselling.R;
import com.myapp.royalcounselling.Utils;
import com.myapp.royalcounselling.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PPTRequestFragment extends Fragment {
    ArrayList<PPTRequestBean> requestList = new ArrayList<PPTRequestBean>();
    ListView listView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragement_ppt_request, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");
        String urlPost = Utils.main_url + "powerPointRequestUserWise/" + email;
        textView = rootView.findViewById(R.id.txt_ppt_request);

//        Toast.makeText(getActivity(), urlPost, Toast.LENGTH_LONG).show();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPost, response -> {


            Log.e("TAG", "onResponse: " + response);
            String comment;
            boolean queryOver;
            String requestQuery;
            String queryTime;

            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    PPTRequestBean request = new PPTRequestBean();
                    JSONObject object = jsonArray.getJSONObject(i);
                    comment = (String.valueOf(object.getString("comment")));
                    if (comment.equals("null")) {
                        comment = "No Update From Admin";
                    }
                    queryTime = (String.valueOf(object.getString("requestAt"))).replace('T', ' ').replace('+', ' ');
                    requestQuery = (String.valueOf(object.getString("requestQuery")));
                    String queryOverString = (String.valueOf(object.getString("queryOver")));
                    try {
                        queryOver = Boolean.parseBoolean(queryOverString);
                    } catch (Exception e) {
                        queryOver = false;
                    }
                    request.setComment(comment);
                    request.setRequestQuery(requestQuery);
                    request.setQueryTime(queryTime);
                    request.setQueryOver(queryOver);
                    requestList.add(request);
                }
                if (requestList.isEmpty()) {
                    textView.setText("No request made at the moment.");
                } else {
                    textView.setText("");
                }
                listView = rootView.findViewById(R.id.list);
                MyRequestAdapter myRequestAdapter = new MyRequestAdapter(getContext(), requestList);
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
