package com.myapp.royalcounselling;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PowerPointPresentationActivity extends AppCompatActivity implements PPTRecycler.OnNoteListener {

    PPTRecycler mNoteRecyclerAdapter;
    ArrayList<PPTBean> recyclerDataArrayList = new ArrayList<PPTBean>();
    RecyclerView mRecyclerView;
    Button searchPPT, requestPPT, btnCancel, btnSearch, btnRequest;
    EditText searchEdt, requestEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_point_presentation);

        searchPPT = findViewById(R.id.btn_search);
        requestPPT = findViewById(R.id.btn_request);

        searchPPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View myview = layoutInflater.inflate(R.layout.raw_search_ppt_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(PowerPointPresentationActivity.this);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setView(myview);
                alertDialog.show();
                btnCancel = (Button) myview.findViewById(R.id.btn_cancel);
                btnSearch = (Button) myview.findViewById(R.id.btn_search);
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchEdt = (EditText) myview.findViewById(R.id.edt_search_ppt);
                        String query = searchEdt.getText().toString();
                        if (query != null && query.trim().length() != 0) {
                            searchPPT(query.trim());
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(PowerPointPresentationActivity.this, "Please enter valid Search", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        requestPPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View myview = layoutInflater.inflate(R.layout.raw_request_ppt_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(PowerPointPresentationActivity.this);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setView(myview);
                alertDialog.show();
                btnCancel = (Button) myview.findViewById(R.id.btn_cancel);
                btnRequest = (Button) myview.findViewById(R.id.btn_request);
                btnRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestEdt = (EditText) myview.findViewById(R.id.edt_request_ppt);
                        String query = requestEdt.getText().toString();
                        if (query != null && query.trim().length() != 0) {
                            requestPPT(query.trim());
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(PowerPointPresentationActivity.this, "Please enter valid request", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });


        String urlPost = Utils.main_url + "getAllPowerPoint/";

        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        ProgressDialog progressDialog = new ProgressDialog(PowerPointPresentationActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPost, response -> {
            Log.e("TAG", "onResponse: " + response);
            String pptURL;
            String fileName;
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    PPTBean ppt = new PPTBean();
                    JSONObject object = jsonArray.getJSONObject(i);
                    pptURL = (String.valueOf(object.getString("pptURL")));
                    fileName = (String.valueOf(object.getString("fileName")));
                    ppt.setFileName(fileName);
                    ppt.setPptURL(pptURL);
                    recyclerDataArrayList.add(ppt);
                }
                initRecyclerView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.e("api error", "something went wrong" + error)) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PowerPointPresentationActivity.this).addToRequestQueue(stringRequest);

    }

    private void searchPPT(String searchQuery) {
        String urlPost = Utils.main_url + "getAllSearchedPPT/";
        ProgressDialog progressDialog = new ProgressDialog(PowerPointPresentationActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {
            Log.e("TAG", "onResponse: " + response);
            String pptURL;
            String fileName;
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                recyclerDataArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    PPTBean ppt = new PPTBean();
                    JSONObject object = jsonArray.getJSONObject(i);
                    pptURL = (String.valueOf(object.getString("pptURL")));
                    fileName = (String.valueOf(object.getString("fileName")));
                    ppt.setFileName(fileName);
                    ppt.setPptURL(pptURL);
                    recyclerDataArrayList.add(ppt);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("api error", "something went wrong" + error);
        }

        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("searchQuery", searchQuery);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PowerPointPresentationActivity.this).addToRequestQueue(stringRequest);
    }

    private void requestPPT(String query) {
        SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");

        String urlPost = Utils.main_url + "requestPowerPoint/";
        ProgressDialog progressDialog = new ProgressDialog(PowerPointPresentationActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {
            Log.e("TAG", "onResponse: " + response);
            String pptURL;
            String fileName;
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                Toast.makeText(PowerPointPresentationActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("api error", "something went wrong" + error);
        }

        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("emailID", email);
                map.put("requestQuery", query);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PowerPointPresentationActivity.this).addToRequestQueue(stringRequest);

    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mNoteRecyclerAdapter = new PPTRecycler(recyclerDataArrayList, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        String[] url = recyclerDataArrayList.get(position).getPptURL().split("=");
        String u = "https://drive.google.com/file/d/" + url[2] + "/view?usp=sharing";
        Intent i = new Intent(PowerPointPresentationActivity.this, PDFActivity.class);

        i.putExtra("PDF", u);
        startActivity(i);
    }

}