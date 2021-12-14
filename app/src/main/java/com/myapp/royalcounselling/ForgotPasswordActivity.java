package com.myapp.royalcounselling;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {


    Button forgot;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.edt_forgot_email);
        forgot = findViewById(R.id.btn_forgot_recover);

        forgot.setOnClickListener(v -> loadData(email.getText().toString()));

    }

    private void loadData(String em) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String urlPost = Utils.main_url;
        urlPost = urlPost.concat("forgotPasswordByUser");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {

            Log.e("TAG", "onResponse: " + response);
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");

                if (status == 200) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password sent on your email", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid Email Id", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                progressDialog.dismiss();
                SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("KEY_EMAIL");
                editor.remove("KEY_PASSWORD");
                editor.apply();
                Toast.makeText(ForgotPasswordActivity.this, "Invalid Email Id ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast invalid_email_id;
            invalid_email_id = Toast.makeText(ForgotPasswordActivity.this, "Invalid Email Id", Toast.LENGTH_LONG);
            invalid_email_id.show();
            //       Log.e("Invalid Email", error.getMessage());

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("emailID", em);
                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ForgotPasswordActivity.this).addToRequestQueue(stringRequest);

    }

}