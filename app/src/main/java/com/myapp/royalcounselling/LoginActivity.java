package com.myapp.royalcounselling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signIn;
    TextView signUp;
    TextView forgotPassword;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edt_login_email);
        password = findViewById(R.id.edt_login_password);
        signIn = findViewById(R.id.btn_login_sign_in);
        signUp = findViewById(R.id.btn_login_sign_up);
        forgotPassword = findViewById(R.id.tv_login_forgot);
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
//        Toast.makeText(LoginActivity.this,"TOken Out : "+token,Toast.LENGTH_SHORT).show();

        signIn.setOnClickListener(v -> {
            String em = email.getText().toString();
            String pass = password.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("KEY_EMAIL", em);
            editor.putString("KEY_PASSWORD", pass);
            editor.putString("TOKEN", token);
            editor.apply();

            loadData(em, pass, token);
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadData(String em, String pass, String token) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String urlPost = Utils.main_url;
        urlPost = urlPost.concat("loginUser");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPost, response -> {

            Log.e("TAG", "onResponse: " + response);
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");

                if (status == -1) {
                    throw new ArrayIndexOutOfBoundsException();


                }
                if (status == 200) {
                    Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }


            } catch (Exception e) {
                progressDialog.dismiss();
                SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("KEY_EMAIL");
                editor.remove("KEY_PASSWORD");
                editor.apply();
                Toast.makeText(LoginActivity.this, "Invalid Email Id or password", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast invalid_email_id;
            invalid_email_id = Toast.makeText(LoginActivity.this, "Invalid Email Id", Toast.LENGTH_LONG);
            invalid_email_id.show();
            //       Log.e("Invalid Email", error.getMessage());

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("emailID", em);
                map.put("password", pass);
                map.put("tokenID", token);
                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);

    }
}