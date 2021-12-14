package com.myapp.royalcounselling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName, emailID, password, lastName, phoneNumber;
    Button signUp;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.edt_sign_up_first_name);
        lastName = findViewById(R.id.edt_sign_up_last_name);
        emailID = findViewById(R.id.edt_sign_up_email);
        password = findViewById(R.id.edt_sign_up_password);
        phoneNumber = findViewById(R.id.edt_sign_up_phone_number);
        signUp = findViewById(R.id.btn_sign_up_register);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                } else {
                    token = task.getResult();
                }
            }
        });
        signUp.setOnClickListener(v -> {

            String fn = firstName.getText().toString();
            String ln = lastName.getText().toString();
            String em = emailID.getText().toString();
            String pass = password.getText().toString();
            String phone = phoneNumber.getText().toString();
            if (fn.length() < 3) {
                Toast.makeText(SignUpActivity.this, "Please enter name again", Toast.LENGTH_LONG).show();
            } else if (!em.contains("@") && em.length() < 10) {
                Toast.makeText(SignUpActivity.this, "Please enter email again", Toast.LENGTH_LONG).show();
            } else if (pass.length() < 5) {
                Toast.makeText(SignUpActivity.this, "Please enter password more than 5 characters", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("KEY_EMAIL", em);
                editor.putString("KEY_PASSWORD", pass);
                editor.putString("TOKEN", token);
                editor.apply();
//                Toast.makeText(SignUpActivity.this, em, Toast.LENGTH_SHORT).show();
                loadData(fn, ln, em, pass, phone, token);
            }
        });
    }


    private void loadData(String fn, String ln, String em, String pass, String phone, String tokenID) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        String urlPost = Utils.main_url;
        urlPost = urlPost.concat("signUpUser");
        Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {

            Log.e("TAG", "onResponse: " + response);
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    int id = jsonObject.optInt("status");

                    if (id == -1) {
                        Intent intent1 = new Intent(SignUpActivity.this, LoginActivity.class);
                        Toast.makeText(SignUpActivity.this, "Email ID Already Registered", Toast.LENGTH_LONG).show();
                        startActivity(intent1);
                    } else {


                        if (jsonObject.has("firstName")) {
                            String str = jsonObject.optString("firstName");
                            intent.putExtra("firstName", str);
                        }

                        if (jsonObject.has("lastName")) {
                            String str = jsonObject.optString("lastName");
                            intent.putExtra("lastName", str);
                        }

                        if (jsonObject.has("email")) {
                            String str = jsonObject.optString("email");
                            intent.putExtra("email", str);
                        }

                        if (jsonObject.has("password")) {
                            String str = jsonObject.optString("password");
                            intent.putExtra("password", str);
                        }

                        if (jsonObject.has("phoneNumber")) {
                            String str = jsonObject.optString("phoneNumber");
                            intent.putExtra("phoneNumber", str);
                        }
                        if (jsonObject.has("tokenID")) {
                            String str = jsonObject.optString("phoneNumber");
                            intent.putExtra("tokenID", str);
                        }
                        startActivity(intent);
                        finish();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("api error", "something went wrong" + error);
            Toast.makeText(SignUpActivity.this, "Email ID Already Registered", Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("firstName", fn);
                map.put("lastName", ln);
                map.put("emailID", em);
                map.put("password", pass);
                map.put("phoneNumber", phone);
                map.put("tokenID", tokenID);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(SignUpActivity.this).addToRequestQueue(stringRequest);

    }


}