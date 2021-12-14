package com.myapp.royalcounselling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IndividualSeminarActivity extends AppCompatActivity {

    TextView seminarName, seminarDescription, sStart, sEnd, rStart, rEnd, type;
    Button registerButton;
    EditText purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_seminar_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("seminarName");
        String description = intent.getStringExtra("seminarDescription");
        String registrationStart = intent.getStringExtra("registrationStart");
        String registrationEnd = intent.getStringExtra("registrationEnd");
        String seminarType = intent.getStringExtra("seminarType");
        String seminarStart = intent.getStringExtra("seminarStart");
        String seminarEnd = intent.getStringExtra("seminarEnd");
        String seminarId = intent.getStringExtra("seminarId");
        seminarName = findViewById(R.id.tv_seminar_name);
        seminarDescription = findViewById(R.id.tv_seminar_description);
        sStart = findViewById(R.id.tv_seminar_start);
        sEnd = findViewById(R.id.tv_seminar_end);
//        rStart = findViewById(R.id.tv_registration_start);
        rEnd = findViewById(R.id.tv_registration_end);
        type = findViewById(R.id.tv_seminar_type);
        purpose = findViewById(R.id.edt_purpose);
        registerButton = findViewById(R.id.btn_register_seminar);

        SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");


        seminarName.setText(name);
        seminarDescription.setText(description);
        sStart.setText(seminarStart);
        sEnd.setText(seminarEnd);
//        rStart.setText(registrationStart);
        rEnd.setText(registrationEnd);
        type.setText(seminarType);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(purpose.getText().toString(), email, seminarId);
            }
        });

    }

    private void loadData(String purpose, String email, String seminarId) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String urlPost = Utils.main_url;
        urlPost = urlPost.concat("registerSeminar");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {

            Log.e("TAG", "onResponse: " + response);
            try {

                progressDialog.dismiss();
                Intent intent = new Intent(this, NavigationDrawerActivity.class);
                JSONObject jsonObject = new JSONObject(response);


                if (jsonObject.has("emailID")) {
                    String str = jsonObject.optString("emailID");
                    intent.putExtra("emailID", str);
                }


                if (jsonObject.has("question")) {
                    String str = jsonObject.optString("question");
                    intent.putExtra("question", str);
                }


                if (jsonObject.has("seminarID")) {
                    String str = jsonObject.optString("seminarID");
                    intent.putExtra("seminarID", str);
                }

                startActivity(intent);
                Toast.makeText(IndividualSeminarActivity.this, "Seminar link sent on your email.", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.e("api error", "something went wrong" + error)) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("question", purpose);
                map.put("emailID", email);
                map.put("seminarID", seminarId);
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}